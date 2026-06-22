# Exercise 22 - Kubernetes Horizontal Pod Autoscaler (HPA)

## Objective

The objective of this exercise is to implement Horizontal Pod Autoscaling (HPA) in Kubernetes. HPA automatically increases or decreases the number of pod replicas based on resource utilization such as CPU or memory usage.

---

## Prerequisites

* Minikube Cluster
* kubectl
* Metrics Server
* Existing Deployment (`payment-payment-chart`)

---

## Step 1: Verify Metrics Server

Check if Metrics Server is running.

```bash
kubectl get pods -n kube-system
```

Verify node and pod metrics.

```bash
kubectl top nodes
kubectl top pods
```

Example Output:

```text
NAME       CPU(cores)   CPU(%)   MEMORY(bytes)
minikube   420m         2%       2229Mi
```

---

## Step 2: Create Horizontal Pod Autoscaler

Create an HPA for the deployment.

```bash
kubectl autoscale deployment payment-payment-chart --cpu-percent=20 --min=2 --max=5
```

Verify HPA creation.

```bash
kubectl get hpa
```

Example Output:

```text
NAME                    REFERENCE                          TARGETS
payment-payment-chart   Deployment/payment-payment-chart   cpu: 34%/20%
```

---

## Step 3: Generate Application Load

Create a load generator pod.

```bash
kubectl run load-generator --image=busybox --restart=Never -it -- sh
```

Inside the pod:

```sh
while true; do wget -q -O- http://payment-payment-chart; done
```

Create a second load generator if additional load is required.

```bash
kubectl run load-generator-2 --image=busybox --restart=Never -it -- sh
```

Inside the pod:

```sh
while true; do wget -q -O- http://payment-payment-chart; done
```

---

## Step 4: Monitor Autoscaling

Watch HPA activity.

```bash
kubectl get hpa -w
```

Watch pod creation.

```bash
kubectl get pods -w
```

Example Output:

```text
payment-payment-chart   cpu: 34%/20%   2   5   4
```

This indicates that CPU utilization exceeded the configured threshold and Kubernetes automatically increased the number of replicas.

---

## Step 5: Verify Scaling

Check deployment status.

```bash
kubectl get deployment payment-payment-chart
```

Example Output:

```text
NAME                    READY   UP-TO-DATE   AVAILABLE
payment-payment-chart   4/4     4            4
```

Check running pods.

```bash
kubectl get pods
```

Example Output:

```text
payment-payment-chart-85769dcbbd-m5pzk
payment-payment-chart-85769dcbbd-sj4lz
payment-payment-chart-85769dcbbd-t89hd
payment-payment-chart-85769dcbbd-xkr87
```

The deployment successfully scaled from 2 replicas to 4 replicas.

---

## Architecture

```text
User Traffic
      |
      v
  Kubernetes Service
      |
      v
Deployment (payment-payment-chart)
      |
      v
      Pods
      |
      v
Metrics Server
      |
      v
Horizontal Pod Autoscaler
      |
      v
Scale Up / Scale Down
```

---

## Result

* Installed and verified Metrics Server.
* Collected CPU and memory metrics using kubectl top.
* Created a Horizontal Pod Autoscaler.
* Generated load using BusyBox load generators.
* Observed automatic scaling from 2 replicas to 4 replicas.
* Successfully demonstrated Kubernetes Horizontal Pod Autoscaling.

---

## Concepts Learned

* Metrics Server
* Resource Monitoring
* CPU Requests and Limits
* Horizontal Pod Autoscaler (HPA)
* Automatic Scaling
* Kubernetes Resource Management
* Production Scaling Strategies
