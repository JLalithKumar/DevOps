# Exercise 5 – Helm Upgrade Failure

## Objective

Understand why Helm upgrades can fail when immutable fields in Kubernetes Deployments are modified and learn safe upgrade strategies.

---

## Scenario

A production deployment failed during a Helm upgrade.

### Version 1

```yaml
selector:
  matchLabels:
    app: payment
```

### Version 2

```yaml
selector:
  matchLabels:
    app: payment-v2
```

### Upgrade Command

```bash
helm upgrade payment-service .
```

### Error

```text
The Deployment "payment" is invalid:
spec.selector: Invalid value:
{"matchLabels":{"app":"payment-v2"}}:
field is immutable
```

---

## Files Created

### deployment-v1.yaml

Creates a Deployment named `payment` with selector:

```yaml
app: payment
```

### deployment-v2.yaml

Attempts to modify the selector to:

```yaml
app: payment-v2
```

which reproduces the immutable field error.

---

## Root Cause Analysis

A Kubernetes Deployment uses `spec.selector` to determine which Pods belong to it.

Example:

```yaml
selector:
  matchLabels:
    app: payment
```

The Deployment manages only Pods that have:

```yaml
labels:
  app: payment
```

After a Deployment is created, Kubernetes does not allow changes to `spec.selector` because it defines Pod ownership.

Changing the selector could cause a Deployment to unexpectedly take control of unrelated Pods, leading to application instability and resource conflicts.

Therefore, `spec.selector` is immutable.

---

## Validation Performed

### Deploy Version 1

```bash
kubectl apply -f deployment-v1.yaml
```

### Verify Deployment

```bash
kubectl get deployment
kubectl get pods
```

### Apply Version 2

```bash
kubectl apply -f deployment-v2.yaml
```

### Observed Result

```text
The Deployment "payment" is invalid:
spec.selector: Invalid value:
{"matchLabels":{"app":"payment-v2"}}:
field is immutable
```

The issue was successfully reproduced.

---

## Safe Upgrade Approaches

### Approach 1 – Delete and Recreate

```bash
kubectl delete deployment payment
kubectl apply -f deployment-v2.yaml
```

Advantages:

* Simple

Disadvantages:

* Causes downtime

---

### Approach 2 – Create New Deployment

Create a new Deployment:

```yaml
metadata:
  name: payment-v2
```

Advantages:

* No downtime
* Both versions can run simultaneously

---

### Approach 3 – Blue-Green / Canary Deployment

Deploy a new version alongside the existing version and gradually shift traffic.

Advantages:

* Safe production deployment
* Easy rollback
* Minimal risk

---

## Key Learning

* `spec.selector` determines Pod ownership.
* `spec.selector` is immutable after Deployment creation.
* Helm upgrades fail when attempting to modify immutable fields.
* Production environments should use new Deployments, Blue-Green, or Canary strategies instead of modifying Deployment selectors.

---

## Outcome

Successfully reproduced and analyzed a Helm upgrade failure caused by modification of an immutable Deployment selector and identified safe upgrade strategies for production environments.
