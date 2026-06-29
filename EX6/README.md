# Exercise 6 – EKS Node Scale Failure

## Objective

Simulate a production scaling incident where application demand exceeds available node capacity. Configure Horizontal Pod Autoscaler (HPA) and Cluster Autoscaler to automatically scale pods and worker nodes in an Amazon EKS cluster.

---

## Problem Statement

Production applications experienced increased CPU utilization, causing Kubernetes to create additional pod replicas. Since the existing worker node lacked sufficient CPU resources, several pods remained in the **Pending** state.

The objective was to identify the root cause and configure automatic node provisioning using the Cluster Autoscaler.

---

## Architecture

```
Application
      │
      ▼
Deployment
      │
      ▼
Horizontal Pod Autoscaler
      │
      ▼
Pending Pods
      │
      ▼
Cluster Autoscaler
      │
      ▼
Amazon EKS Node Group
      │
      ▼
Additional Worker Nodes
```

---

## Concepts Used

### Horizontal Pod Autoscaler (HPA)

Automatically increases or decreases the number of pod replicas based on CPU utilization.

### Metrics Server

Collects CPU and memory metrics required by the HPA.

### Cluster Autoscaler

Automatically provisions or removes EC2 worker nodes when Kubernetes cannot schedule pods due to insufficient resources.

### IRSA (IAM Roles for Service Accounts)

Allows the Cluster Autoscaler pod to securely access AWS APIs without storing AWS credentials.

---

## Steps Performed

* Verified EKS cluster and Metrics Server installation.
* Deployed the sample application.
* Exposed the application using a ClusterIP service.
* Configured Horizontal Pod Autoscaler.
* Generated continuous CPU load using a stress container.
* Observed HPA increasing replicas.
* Identified Pending pods caused by insufficient CPU.
* Created IAM policy for Cluster Autoscaler.
* Associated an IAM OIDC provider with the EKS cluster.
* Configured IRSA using an IAM Service Account.
* Installed the Cluster Autoscaler.
* Verified autoscaler logs.
* Observed automatic provisioning of additional worker nodes.
* Confirmed that pods were scheduled on newly created nodes.

---

## Validation

### HPA

```
Target CPU: 50%

Current CPU: 137%

Replicas: 15
```

### Worker Nodes

```
Before:

1 Worker Node

After:

3 Worker Nodes
```

### Pod Scheduling

Pods were automatically distributed across the newly created worker nodes after scaling.

---

## Root Cause Analysis

### HPA Issue

No.

The Horizontal Pod Autoscaler correctly increased the number of replicas based on CPU utilization.

### Node Issue

Yes.

The original worker node did not have sufficient CPU resources to schedule all requested pods.

### Cluster Autoscaler Issue

Initially, the node group reached its scaling limit.

After increasing the maximum node group size and configuring IRSA correctly, the Cluster Autoscaler successfully provisioned additional EC2 instances.

---

## Solution

* Installed Metrics Server.
* Configured Horizontal Pod Autoscaler.
* Created IAM Policy.
* Associated IAM OIDC Provider.
* Configured IRSA.
* Installed Cluster Autoscaler.
* Allowed the node group to scale up.
* Verified automatic node provisioning.

---

## Outcome

* Successfully implemented Horizontal Pod Autoscaling.
* Successfully configured Cluster Autoscaler.
* Worker nodes automatically scaled from **1 → 3**.
* Application replicas increased automatically based on CPU utilization.
* Production scaling incident successfully resolved.

---

## Tools & Technologies

* Amazon EKS
* Kubernetes
* eksctl
* AWS IAM
* IRSA
* Metrics Server
* Horizontal Pod Autoscaler
* Cluster Autoscaler
* Docker
* YAML

---

## Learning Outcomes

* Configured Horizontal Pod Autoscaler.
* Configured Cluster Autoscaler.
* Implemented IRSA for AWS authentication.
* Diagnosed Pending pods caused by insufficient CPU.
* Performed production-style Kubernetes scaling troubleshooting on Amazon EKS.
