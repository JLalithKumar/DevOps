# Exercise 2 – IAM / IRSA Failure

## Objective

Investigate and resolve an EKS application that cannot access DynamoDB due to an IRSA configuration issue.

---

## Architecture

Pod
↓
ServiceAccount
↓
IAM Role (IRSA)
↓
DynamoDB

---

## Incident

Application logs:

```text
AccessDeniedException

User:
arn:aws:sts::<account-id>:assumed-role/eks-nodegroup-role

is not authorized to perform:
dynamodb:GetItem
```

The application was expected to use an IRSA role but was instead using the EKS node role.

---

## Environment Setup

### Create EKS Cluster

```bash
eksctl create cluster \
--name irsa-lab \
--region us-east-1 \
--nodes 1 \
--node-type t3.small
```

### Enable OIDC

```bash
eksctl utils associate-iam-oidc-provider \
--cluster irsa-lab \
--approve
```

### Create DynamoDB Table

```bash
aws dynamodb create-table \
--table-name customer-data \
--attribute-definitions AttributeName=customerId,AttributeType=S \
--key-schema AttributeName=customerId,KeyType=HASH \
--billing-mode PAY_PER_REQUEST
```

### Create IAM Policy

```bash
aws iam create-policy \
--policy-name DynamoDBIRSALabPolicy \
--policy-document file://dynamodb-policy.json
```

### Create IRSA Service Account

```bash
eksctl create iamserviceaccount customer-sa \
--cluster irsa-lab \
--namespace irsa-demo \
--attach-policy-arn <policy-arn> \
--approve
```

---

## Failure Reproduction

### Deploy Pod Without IRSA

Apply:

```bash
kubectl apply -f bad-pod.yaml
```

Check identity:

```bash
aws sts get-caller-identity
```

Output:

```text
arn:aws:sts::<account-id>:assumed-role/eks-nodegroup-role
```

Test DynamoDB:

```bash
aws dynamodb get-item \
--table-name customer-data \
--key '{"customerId":{"S":"1"}}'
```

Result:

```text
AccessDeniedException
```

---

## Root Cause

The pod was deployed without the IRSA-enabled Service Account.

Therefore:

Pod
↓
Default Service Account
↓
Node IAM Role
↓
AccessDenied

The application used the EC2 node role instead of the IRSA role.

---

## Fix

Update the pod configuration:

```yaml
serviceAccountName: customer-sa
```

Deploy:

```bash
kubectl apply -f good-pod.yaml
```

Verify identity:

```bash
aws sts get-caller-identity
```

Output:

```text
arn:aws:sts::<account-id>:assumed-role/eksctl-irsa-lab-addon-iamserviceaccount...
```

DynamoDB access succeeds.

---

## Validation Commands

```bash
kubectl get sa customer-sa -n irsa-demo -o yaml

aws sts get-caller-identity

aws dynamodb get-item \
--table-name customer-data \
--key '{"customerId":{"S":"1"}}'
```

---

## Key Learnings

- IRSA allows Kubernetes workloads to access AWS services without AWS access keys.
- Pods must explicitly use the IRSA-enabled Service Account.
- If a pod does not use the correct Service Account, AWS falls back to the EC2 node role.
- AccessDenied errors can occur even when IRSA is configured correctly if the workload is not using the intended Service Account.
