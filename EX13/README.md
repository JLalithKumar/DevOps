# Exercise 13 – Secret Rotation Outage

## Architecture

``` text
            AWS Secrets Manager
                --------------------
                password = Password123
                        │
                        │
                        ▼
        External Secrets Operator
        (runs inside Kubernetes)
                        │
                        │ asks AWS
                        ▼
              "Give me payment-secret"
                        │
                        ▼
                IAM Authentication
                        │
             Does this role have permission?
                 │                 │
               YES                NO
                │                  │
                ▼                  ▼
        Return Secret        AccessDenied
                │
                ▼
       Kubernetes Secret
      payment-secret
                │
                ▼
        Application Pod
```

## Summary

This exercise demonstrated configuring External Secrets Operator with
IRSA, synchronizing AWS Secrets Manager with Kubernetes Secrets,
intentionally breaking IAM permissions to reproduce a production outage,
investigating `SecretSyncedError` and `AccessDeniedException`, and
restoring synchronization by reattaching the IAM policy.
