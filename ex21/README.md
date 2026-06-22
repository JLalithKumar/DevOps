# Exercise 21 – Production ALB Ingress Setup on Amazon EKS

## Objective

Deploy applications on Amazon EKS and expose them to the internet using AWS Application Load Balancer (ALB) through Kubernetes Ingress.

---

## Architecture

Internet User
→ AWS Application Load Balancer (ALB)
→ Kubernetes Ingress
→ Kubernetes Services
→ Application Pods

---

## Prerequisites

* AWS Account
* AWS CLI
* kubectl
* eksctl
* Helm

---

## Steps Performed

### 1. Create EKS Cluster

```bash
eksctl create cluster \
--name ex21-alb-cluster \
--region us-east-1 \
--nodes 2 \
--node-type t3.small
```

### 2. Configure IAM OIDC Provider

```bash
eksctl utils associate-iam-oidc-provider \
--region us-east-1 \
--cluster ex21-alb-cluster \
--approve
```

### 3. Create IAM Policy

```bash
curl -o iam_policy.json https://raw.githubusercontent.com/kubernetes-sigs/aws-load-balancer-controller/main/docs/install/iam_policy.json

aws iam create-policy \
--policy-name AWSLoadBalancerControllerIAMPolicy \
--policy-document file://iam_policy.json
```

### 4. Create IAM Service Account

```bash
eksctl create iamserviceaccount \
--cluster=ex21-alb-cluster \
--namespace=kube-system \
--name=aws-load-balancer-controller \
--attach-policy-arn=<POLICY_ARN> \
--approve
```

### 5. Install AWS Load Balancer Controller

```bash
helm repo add eks https://aws.github.io/eks-charts
helm repo update
```

```bash
helm install aws-load-balancer-controller eks/aws-load-balancer-controller \
-n kube-system \
--set clusterName=ex21-alb-cluster \
--set serviceAccount.create=false \
--set serviceAccount.name=aws-load-balancer-controller \
--set region=us-east-1 \
--set vpcId=<VPC_ID>
```

### 6. Deploy Applications

Two sample applications were deployed:

* App1 (Nginx)
* App2 (Apache HTTP Server)

### 7. Create Services

ClusterIP services were created for both applications.

### 8. Create ALB Ingress

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: app-ingress
  annotations:
    alb.ingress.kubernetes.io/scheme: internet-facing
    alb.ingress.kubernetes.io/target-type: ip

spec:
  ingressClassName: alb
  rules:
  - http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: app1-service
            port:
              number: 80
```

### 9. Verify Deployment

```bash
kubectl get ingress
```

Output:

```text
k8s-default-appingre-c7ffc7dd18-1155713364.us-east-1.elb.amazonaws.com
```

Accessing the ALB DNS name displayed the Nginx welcome page successfully.

---

## Results

* EKS cluster created successfully.
* AWS Load Balancer Controller installed.
* ALB provisioned automatically.
* Applications exposed through Kubernetes Ingress.
* Internet traffic successfully routed to application pods.
* Nginx welcome page verified through ALB endpoint.

---

## Key Concepts Learned

* Amazon EKS
* IAM OIDC Provider
* IAM Roles for Service Accounts (IRSA)
* AWS Load Balancer Controller
* Kubernetes Ingress
* Application Load Balancer (ALB)
* Service Discovery
* Path-Based Routing
* Production Traffic Management

---

## Conclusion

Successfully implemented a Production ALB Ingress Setup on Amazon EKS using AWS Load Balancer Controller. The application was exposed through an internet-facing Application Load Balancer and verified through browser access.
