# Exercise 7 – ALB Ingress Failure

## Objective

Deploy an application on Amazon EKS and expose it through an AWS Application Load Balancer (ALB). Investigate and troubleshoot an ALB Ingress failure by analyzing Kubernetes resources and AWS networking components.

---

# Problem Statement

Users reported:

```
504 Gateway Timeout
```

AWS Load Balancer Controller logs showed:

```
Unable to discover subnets
```

The objective was to determine why the application became inaccessible and identify the root cause.

---

# Architecture

```
                Internet
                     │
                     ▼
        AWS Application Load Balancer
                     │
                     ▼
              Target Group
                     │
                     ▼
          Kubernetes Service
                     │
                     ▼
             payment-service Pods
```

---

# Components Used

- Amazon EKS
- AWS Load Balancer Controller
- IAM Roles for Service Accounts (IRSA)
- Application Load Balancer (ALB)
- Target Groups
- Kubernetes Deployment
- Kubernetes Service
- Kubernetes Ingress

---

# Working Flow

```
Application Deployment
        │
        ▼
Pods Created
        │
        ▼
ClusterIP Service
        │
        ▼
Ingress Created
        │
        ▼
AWS Load Balancer Controller
        │
        ▼
Discovers Tagged Subnets
        │
        ▼
Creates Application Load Balancer
        │
        ▼
Creates Target Group
        │
        ▼
Registers Pod IP Addresses
        │
        ▼
Runs Health Checks
        │
        ▼
Users Access Application
```

---

# Steps Performed

- Verified the EKS cluster health.
- Verified OIDC provider.
- Created IAM policy.
- Configured IRSA.
- Installed AWS Load Balancer Controller using Helm.
- Deployed payment-service.
- Created ClusterIP Service.
- Created Kubernetes Ingress.
- Verified ALB creation.
- Verified Target Registration.
- Verified application accessibility.

---

# Validation

Verified:

- AWS Load Balancer Controller Running
- ALB Successfully Created
- Target Group Created
- Pod IPs Registered
- Ingress Successfully Reconciled

Example:

```
kubectl get ingress

ADDRESS

k8s-default-paymenti-xxxxxxxx.us-east-1.elb.amazonaws.com
```

---

# Investigation Performed

## 1. Verify Ingress

```
kubectl describe ingress payment-ingress
```

Checked:

- ALB Address
- Events
- Backend Service

---

## 2. Verify AWS Load Balancer Controller

```
kubectl logs -n kube-system deployment/aws-load-balancer-controller
```

Checked:

- Reconciliation errors
- AccessDenied
- Unable to discover subnets

---

## 3. Verify Subnet Discovery

Verified subnet tags:

```
kubernetes.io/role/elb=1

kubernetes.io/cluster/scaling-lab=shared
```

These tags allow the controller to discover suitable subnets for creating the ALB.

---

## 4. Verify Target Registration

Checked:

```
kubectl describe ingress payment-ingress
```

Verified backend Pod IPs were successfully registered in the Target Group.

---

## 5. Verify Target Health

Verified the application Pods were healthy and responding to ALB health checks.

---

# Troubleshooting Decision Tree

```
User gets 504 Gateway Timeout
             │
             ▼
1. Check Ingress
             │
      Ingress OK?
        │        │
       No       Yes
       │         ▼
 Fix Ingress   Check Controller Logs
                     │
          Controller Healthy?
              │           │
             No          Yes
             │            ▼
     Fix Controller   Check Subnets
                            │
                 Subnets Tagged?
                    │           │
                   No          Yes
                   │            ▼
           Fix Tags        Check Target Registration
                                   │
                        Targets Registered?
                           │             │
                          No            Yes
                          │              ▼
                  Fix Service      Check Health Checks
                                        │
                               Targets Healthy?
                                  │          │
                                 No         Yes
                                 │           ▼
                         Fix Application   Application Works
```

---

# Root Cause Analysis

### Symptom

```
504 Gateway Timeout
```

### Root Cause

The AWS Load Balancer Controller could not discover tagged subnets.

Without subnet discovery:

```
No Subnets
      ↓
No ALB
      ↓
No Target Group
      ↓
No Healthy Targets
      ↓
504 Gateway Timeout
```

---

# Solution

- Configure correct subnet tags.
- Verify AWS Load Balancer Controller logs.
- Verify Target Registration.
- Verify Health Checks.
- Reconcile the Ingress.

---

# Outcome

Successfully deployed an application using an AWS Application Load Balancer on Amazon EKS.

Validated:

- ALB creation
- Target Group creation
- Target Registration
- Health Checks
- End-to-end application accessibility

Also learned the production troubleshooting workflow for ALB Ingress failures.

---

# Tools & Technologies

- Amazon EKS
- AWS Load Balancer Controller
- Kubernetes
- Helm
- IAM
- IRSA
- Application Load Balancer
- Target Groups
- AWS VPC
- Ingress
- YAML

---

# Learning Outcomes

- Understood Kubernetes Ingress architecture.
- Installed AWS Load Balancer Controller.
- Configured IRSA authentication.
- Created an internet-facing ALB.
- Learned Target Registration.
- Understood subnet discovery.
- Learned ALB Health Checks.
- Performed production-style troubleshooting for ALB Ingress failures.
