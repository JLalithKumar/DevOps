# Exercise 10 -- Loki Logging Failure: Multi-Tenancy Authentication Incident

## Objective

The objective of this exercise is to investigate and resolve a
production-style logging failure in a Grafana Loki environment.

A simple application continuously generates logs. Grafana Alloy is
responsible for collecting these logs and forwarding them to Grafana
Loki. Loki stores the logs, which can later be queried by Grafana.

The exercise intentionally introduces an authentication failure between
Alloy and Loki to simulate a real production incident.

------------------------------------------------------------------------

# Architecture

``` text
Application
     │
     ▼
Shared Log File (/var/log/app.log)
     │
     ▼
Grafana Alloy
     │
HTTP Push (/loki/api/v1/push)
     │
     ▼
Grafana Loki
     │
     ▼
Grafana
```

------------------------------------------------------------------------

# Task

1.  Deploy the logging stack using Docker Compose.
2.  Verify all containers are running.
3.  Configure Loki with multi-tenancy enabled.
4.  Observe why logs are not reaching Loki.
5.  Investigate the failure using logs and API calls.
6.  Identify the root cause.
7.  Apply the required fix.
8.  Verify successful log ingestion.

------------------------------------------------------------------------

# Environment

-   Docker
-   Docker Compose
-   Grafana Alloy
-   Grafana Loki
-   Alpine Linux (Fake Application)

Files used:

-   docker-compose.yaml
-   alloy-config.river
-   loki-config.yaml

------------------------------------------------------------------------

# Incident Description

Loki was configured with:

``` yaml
auth_enabled: true
```

When multi-tenancy is enabled, every request sent to Loki must include
the HTTP header:

``` text
X-Scope-OrgID
```

Initially, Grafana Alloy was not configured to send this header.

As a result, Loki rejected every log push request.

------------------------------------------------------------------------

# Symptoms

Application logs were continuously generated.

Alloy successfully tailed:

``` text
/var/log/app.log
```

Loki was healthy:

``` bash
curl http://localhost:3100/ready
```

However, attempting to push logs returned:

``` text
no org id
```

This confirmed that the failure occurred during authentication between
Alloy and Loki.

------------------------------------------------------------------------

# Investigation

## Step 1

Verified Docker containers:

``` bash
docker compose ps
```

Result:

-   Alloy running
-   Loki running
-   Fake application running

------------------------------------------------------------------------

## Step 2

Verified Alloy:

``` bash
docker compose logs alloy
```

Confirmed Alloy was reading:

``` text
/var/log/app.log
```

------------------------------------------------------------------------

## Step 3

Verified Loki:

``` bash
curl http://localhost:3100/ready
```

Loki became:

``` text
ready
```

------------------------------------------------------------------------

## Step 4

Manually tested log ingestion:

``` bash
curl -X POST http://localhost:3100/loki/api/v1/push
```

Response:

``` text
no org id
```

------------------------------------------------------------------------

# Root Cause

Grafana Alloy did not send the required tenant identification header.

Because Loki was configured with:

``` yaml
auth_enabled: true
```

every request without:

``` text
X-Scope-OrgID
```

was rejected.

------------------------------------------------------------------------

# Solution

Updated `alloy-config.river`:

``` river
loki.write "local_loki" {
  endpoint {
    url = "http://loki:3100/loki/api/v1/push"

    headers = {
      "X-Scope-OrgID" = "tenant1",
    }
  }
}
```

Restarted Alloy:

``` bash
docker compose restart alloy
```

------------------------------------------------------------------------

# Verification

Verified labels:

``` bash
curl -H "X-Scope-OrgID: tenant1" http://localhost:3100/loki/api/v1/labels
```

Output:

``` json
{"status":"success","data":["filename","service_name"]}
```

Verified available log file:

``` bash
curl -G ^
-H "X-Scope-OrgID: tenant1" ^
--data-urlencode "name=filename" ^
http://localhost:3100/loki/api/v1/label/filename/values
```

Result contained:

``` text
/var/log/app.log
```

No further authentication errors appeared in Alloy logs.

------------------------------------------------------------------------

# Result

-   Docker environment deployed successfully.
-   Authentication issue reproduced.
-   Root cause identified.
-   Alloy configuration corrected.
-   Loki accepted authenticated requests.
-   Logging pipeline restored.

------------------------------------------------------------------------

# Key Learnings

-   Understand Grafana Loki multi-tenancy.
-   Diagnose authentication failures using logs and APIs.
-   Configure Grafana Alloy to authenticate with Loki.
-   Verify successful log ingestion using Loki APIs.
-   Perform structured incident investigation similar to production
    environments.
