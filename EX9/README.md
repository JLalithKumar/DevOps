# Exercise 9 – Prometheus Monitoring Failure Investigation

## Objective

Install a Kubernetes monitoring stack using Prometheus and Grafana, monitor an application using a ServiceMonitor, intentionally introduce a monitoring failure, investigate the issue, fix the configuration, and verify monitoring recovery.

---

# Architecture

```
                    Grafana
                        │
                        ▼
                 Prometheus Server
                        │
                        ▼
              Prometheus Operator
                        │
                        ▼
                 ServiceMonitor
                        │
                        ▼
                Kubernetes Service
                        │
                        ▼
                 Demo Application
                        │
                        ▼
                      Pods
```

---

# Project Structure

```
EX9/
│
├── deployment.yaml
├── service.yaml
├── servicemonitor.yaml
├── commands.txt
└── README.md
```

---

# Workflow

## Step 1 – Install Monitoring Stack

Installed the Prometheus monitoring stack using the Helm chart.

Components installed:

- Prometheus
- Grafana
- Alertmanager
- Prometheus Operator
- Node Exporter
- kube-state-metrics

Verified all monitoring components were running successfully.

---

## Step 2 – Access Monitoring

Configured port forwarding.

Grafana:

```
http://localhost:3000
```

Prometheus:

```
http://localhost:9090
```

Verified successful login to Grafana and Prometheus.

---

## Step 3 – Create Demo Application

Created a dedicated namespace.

```
demo-monitor
```

Created:

- Deployment
- Service
- ServiceMonitor

Verified:

- Pods Running
- Service Created
- ServiceMonitor Created

---

## Step 4 – Verify Monitoring

Opened

```
Prometheus

Status
↓

Targets
```

Verified

```
demo-app

UP
```

This confirmed Prometheus successfully discovered and scraped the application.

---

# Failure Simulation

To simulate a monitoring failure, the Service port name was intentionally changed.

Original

```yaml
ports:
- name: metrics
```

Modified

```yaml
ports:
- name: prometheus
```

The ServiceMonitor configuration was intentionally left unchanged.

```yaml
endpoints:
- port: metrics
```

---

# Result

Prometheus displayed

```
No Targets

0 / 0 UP
```

The application continued running successfully, but Prometheus could not discover any scrape targets.

---

# Investigation

The following resources were inspected:

- Service
- Endpoints
- ServiceMonitor

Comparison

Service

```
Port Name

prometheus
```

ServiceMonitor

```
Port

metrics
```

The ServiceMonitor searched for a Service port named **metrics**, while the Service exposed **prometheus**.

---

# Root Cause

A ServiceMonitor discovers Services using the Service port name.

Since the Service port name and the ServiceMonitor endpoint name did not match, Prometheus failed to discover any monitoring targets.

---

# Resolution

Updated the Service configuration.

Changed

```yaml
name: prometheus
```

back to

```yaml
name: metrics
```

Applied the updated Service.

---

# Verification

Refreshed

```
Prometheus

Status

↓

Targets
```

Verified

```
demo-app

UP
```

Monitoring was successfully restored.

---

# Skills Learned

- Deploy Prometheus using Helm
- Configure Grafana
- Understand Prometheus Operator
- Configure ServiceMonitor
- Monitor Kubernetes applications
- Simulate monitoring failures
- Investigate Prometheus target discovery
- Troubleshoot ServiceMonitor configuration
- Restore monitoring after configuration errors

---

# Outcome

Successfully implemented a Prometheus monitoring environment, simulated a ServiceMonitor configuration failure, identified the root cause, restored monitoring, and verified successful recovery.
