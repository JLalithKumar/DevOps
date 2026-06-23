# Exercise 17 – Implement IRSA for Application Access

## Objective

Configure IAM Roles for Service Accounts (IRSA) in Amazon EKS to allow a Kubernetes Pod to access DynamoDB securely without using AWS Access Keys.

---

## Architecture

Pod (dynamodb-test)
↓
Service Account (dynamodb-sa)
↓
IAM Role
↓
IAM Policy
↓
DynamoDB (customer-data)

OIDC Provider establishes trust between EKS and AWS IAM.

---

## Components Created

### EKS Cluster

* Cluster Name: ex17-irsa-cluster
* Region: us-east-1
* Worker Nodes: 2

### DynamoDB Table

* Table Name: customer-data
* Partition Key: CustomerId

### IAM Policy

Permissions:

* dynamodb:GetItem
* dynamodb:PutItem
* dynamodb:UpdateItem

### Service Account

* Name: dynamodb-sa

### Test Pod

* Name: dynamodb-test
* Image: amazon/aws-cli

---

## Implementation Steps

1. Created EKS Cluster.
2. Created DynamoDB Table.
3. Associated OIDC Provider with EKS.
4. Created IAM Policy for DynamoDB access.
5. Created IAM Role using IRSA.
6. Created Kubernetes Service Account.
7. Attached IAM Role to Service Account.
8. Deployed AWS CLI Test Pod.
9. Verified temporary credentials using AWS STS.
10. Performed PutItem operation.
11. Performed GetItem operation.
12. Performed UpdateItem operation.
13. Verified updated record.

---

## Validation

### STS Verification

Successfully executed:

aws sts get-caller-identity

Output showed:

assumed-role/eksctl-ex17-irsa-cluster-addon-iamserviceaccount-role

This confirmed that the Pod received temporary AWS credentials through IRSA.

---

### DynamoDB Operations

#### PutItem

Inserted:

CustomerId = 101
Name = Lalith

#### GetItem

Retrieved record successfully.

#### UpdateItem

Updated:

Name = Lalith Kumar

#### Final Verification

Retrieved updated record successfully.

---

## Concepts Learned

### IAM Policy

Defines AWS permissions.

### IAM Role

Contains permissions granted to workloads.

### Service Account

Provides identity for Kubernetes Pods.

### OIDC Provider

Establishes trust between EKS and AWS IAM.

### IRSA

Connects Kubernetes Service Accounts with IAM Roles.

### STS

Generates temporary AWS credentials for Pods.

### DynamoDB

AWS NoSQL database service.

---

## Outcome

Successfully implemented IRSA-based authentication for EKS workloads and accessed DynamoDB without storing AWS Access Keys inside Kubernetes.
