# Exercise 18 - GitOps Platform Using ArgoCD

## Objective

Create a GitOps deployment platform on Amazon EKS using ArgoCD.

---

## Architecture

GitHub Repository
↓
ArgoCD
↓
Amazon EKS

ArgoCD continuously monitors the Git repository and automatically synchronizes Kubernetes resources with the cluster.

---

## Prerequisites

* AWS Account
* EKS Cluster
* kubectl
* eksctl
* ArgoCD

---

## Step 1: Install ArgoCD

```bash
kubectl create namespace argocd

kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

---

## Step 2: Verify Installation

```bash
kubectl get pods -n argocd
```

Expected:

All ArgoCD pods should be in Running state.

---

## Step 3: Access ArgoCD UI

```bash
kubectl port-forward svc/argocd-server -n argocd 8090:443
```

Open:

https://localhost:8090

---

## Step 4: Get Initial Admin Password

```bash
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}"
```

Decode the Base64 value.

Login:

Username: admin

Password: <decoded-password>

---

## Step 5: Create GitOps Repository Structure

```text
gitops-demo/
├── dev/
│   └── nginx.yaml
├── qa/
└── prod/
```

---

## Step 6: Push Application Manifest

```bash
git add .
git commit -m "Initial GitOps deployment"
git push origin main
```

---

## Step 7: Create ArgoCD Application

General:

Application Name: nginx-dev

Project: default

Sync Policy: Automatic

Enable:

* Auto Sync
* Self Heal
* Prune Resources
* Auto Create Namespace

Source:

Repository URL:
https://github.com/JLalithKumar/gitops-demo.git

Revision:
HEAD

Path:
dev

Destination:

Cluster:
https://kubernetes.default.svc

Namespace:
dev

---

## Validation

Check namespace:

```bash
kubectl get ns
```

Check deployment:

```bash
kubectl get pods -n dev
```

Expected:

```text
nginx-dev-xxxxx Running
```

---

## GitOps Workflow

Developer
↓
Git Push
↓
GitHub Repository
↓
ArgoCD Detects Change
↓
Auto Sync
↓
EKS Cluster Updated

---

## Features Implemented

* GitOps Deployment
* ArgoCD Auto Sync
* ArgoCD Self Heal
* Resource Pruning
* Automatic Namespace Creation

---

## Outcome

Successfully deployed an application to Amazon EKS using ArgoCD GitOps workflow with automatic synchronization and self-healing capabilities.
