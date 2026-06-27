# Exercise 3 – ArgoCD OutOfSync Production Incident

## Objective

Investigate an ArgoCD application reporting:

* Status: OutOfSync
* Health: Healthy

Determine:

1. What changed
2. Who changed it
3. How to prevent recurrence

---

## Scenario

### Desired State in Git

Deployment manifest stored in Git repository:

```yaml
spec:
  replicas: 3
```

### Live Cluster State

```bash
kubectl get deployment payment-service
```

Output:

```text
READY   5/5
```

The deployment was running with 5 replicas instead of the 3 replicas defined in Git.

---

## Investigation

### Check Application Status

```bash
argocd app get payment-service
```

Result:

```text
Sync Status: OutOfSync
Health Status: Healthy
```

### Verify Desired State

```bash
argocd app manifests payment-service
```

Result:

```yaml
replicas: 3
```

### Verify Live State

```bash
kubectl get deployment payment-service
```

Result:

```text
5 replicas running
```

### Compare States

| Source             | Replica Count |
| ------------------ | ------------- |
| Git Repository     | 3             |
| Kubernetes Cluster | 5             |

---

## Findings

### What Changed?

The replica count of the `payment-service` deployment was changed from **3** to **5**.

### Who Changed It?

A manual modification was made directly in the Kubernetes cluster outside the GitOps workflow.

The exact user cannot be identified without:

* Kubernetes Audit Logs
* ArgoCD Audit Logs
* AWS CloudTrail Logs

### How to Prevent Recurrence?

* Enable ArgoCD Auto Sync
* Enable ArgoCD Self-Heal
* Restrict direct kubectl access
* Implement Kubernetes RBAC
* Enable Kubernetes Audit Logging
* Enforce GitOps-only deployment changes

---

## Root Cause

A deployment configuration drift occurred because the live Kubernetes deployment was manually modified and no longer matched the configuration stored in Git.

---

## Conclusion

ArgoCD successfully detected the configuration drift and reported the application as **OutOfSync** while remaining **Healthy** because the application continued to function correctly despite the configuration mismatch.

The investigation confirmed that the deployment replica count differed between Git and the live cluster, demonstrating how ArgoCD helps identify unauthorized or manual changes in a GitOps environment.
