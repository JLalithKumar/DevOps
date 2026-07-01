# Exercise 11 -- CrashLoopBackOff Investigation

## Objective

Investigate why `payment-service` entered **CrashLoopBackOff** and
determine whether the issue was related to DNS, the database, or
Kubernetes Secrets.

## Scenario

The application failed with: - `dial tcp postgres:5432` -
`connection refused`

## Investigation

1.  Verified Pod status.
2.  Collected application logs.
3.  Described the Pod and checked events.
4.  Verified Kubernetes Secret.
5.  Verified PostgreSQL Service.
6.  Checked Service Endpoints.

## Findings

-   DNS Issue: **No**
-   Secret Issue: **No**
-   Database Issue: **Yes**

The PostgreSQL Deployment had zero replicas, so the `postgres` Service
had no endpoints.

## Root Cause

Because the PostgreSQL database Pod was unavailable, the application
could not establish a TCP connection. The application exited with code
1, causing Kubernetes to repeatedly restart the container until it
entered **CrashLoopBackOff**.

## Immediate Fix

-   Scale PostgreSQL back to one replica.
-   Wait for the database Pod to become Ready.
-   Restart or recreate the payment-service Pod.
-   Verify the endpoints and application status.

## Validation

-   PostgreSQL Pod: Running
-   payment-service Pod: Running
-   Service endpoints populated.

## Key Learning

CrashLoopBackOff is a **symptom**. The actual root cause was that the
database Service had no backend Pods.
