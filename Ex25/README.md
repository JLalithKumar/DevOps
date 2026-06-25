# Exercise 25 – Kubernetes Observability Stack using Prometheus, Grafana, Loki and Tempo

## Objective

The objective of this exercise is to implement a complete observability stack on an AWS EKS Kubernetes cluster using Prometheus, Grafana, Loki, Promtail and Tempo.

## Tools Used

* AWS EKS
* Kubernetes
* Helm
* Prometheus
* Grafana
* Loki
* Promtail
* Tempo

## Architecture

Monitoring:
Prometheus collects metrics from Kubernetes nodes and pods.

Logging:
Promtail collects logs and sends them to Loki for storage and querying.

Tracing:
Tempo stores distributed traces for microservices.

Visualization:
Grafana provides dashboards for metrics, logs and traces.

## Components Deployed

### Monitoring Namespace

* Prometheus
* Grafana
* Alertmanager
* Node Exporter
* kube-state-metrics

### Logging Namespace

* Loki
* Promtail

### Tracing Namespace

* Tempo

## Verification

The following commands were used for verification:

```bash
kubectl get pods -A
kubectl top nodes
kubectl top pods -A
kubectl get pods -n monitoring
kubectl get pods -n logging
kubectl get pods -n tracing
```

## Outcome

Successfully deployed a complete Kubernetes observability stack on AWS EKS.

The deployment provides:

* Metrics Monitoring using Prometheus
* Dashboard Visualization using Grafana
* Centralized Logging using Loki and Promtail
* Distributed Tracing using Tempo
* Resource Monitoring for Nodes and Pods

## Result

Exercise 25 completed successfully with all observability components deployed and running on the EKS cluster.
