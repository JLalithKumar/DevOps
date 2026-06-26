# External Secrets Operator with AWS Secrets Manager on Amazon EKS

## Objective

Demonstrate secure secret management in Kubernetes using:

- Amazon EKS
- IAM Roles for Service Accounts (IRSA)
- AWS Secrets Manager
- External Secrets Operator (ESO)

## Architecture

AWS Secrets Manager
↓
External Secrets Operator
↓
Kubernetes Secret
↓
Application Pods

## Prerequisites

- AWS CLI configured
- kubectl installed
- eksctl installed
- Helm installed
- Running EKS cluster

## Step 1: Create IAM Policy

Create a policy allowing access to AWS Secrets Manager.

```bash
aws iam create-policy --policy-name ExternalSecretsPolicy --policy-document file://policy.json
```

## Step 2: Configure IRSA

Create an IAM Service Account and attach the IAM policy.

```bash
eksctl create iamserviceaccount \
--name external-secrets-sa \
--namespace external-secrets \
--cluster irsa-lab \
--attach-policy-arn <POLICY_ARN> \
--approve
```

## Step 3: Install External Secrets Operator

```bash
helm repo add external-secrets https://charts.external-secrets.io
helm repo update

helm install external-secrets external-secrets/external-secrets \
-n external-secrets \
--create-namespace
```

## Step 4: Create Secret in AWS Secrets Manager

```json
{
  "DB_USERNAME": "admin",
  "DB_PASSWORD": "password123",
  "JWT_SECRET": "secret123"
}
```

```bash
aws secretsmanager create-secret \
--name payment-app-secret \
--secret-string file://secret.json
```

## Step 5: Create SecretStore

Apply SecretStore configuration.

```bash
kubectl apply -f secretstore.yaml
```

## Step 6: Create ExternalSecret

Apply ExternalSecret configuration.

```bash
kubectl apply -f externalsecret.yaml
```

## Verification

Check SecretStore:

```bash
kubectl get secretstore -n external-secrets
```

Check ExternalSecret:

```bash
kubectl get externalsecret -n external-secrets
```

Check Kubernetes Secret:

```bash
kubectl get secret payment-secret -n external-secrets
```

Expected Output:

```text
NAME             STORETYPE     STORE             STATUS         READY
payment-secret   SecretStore   aws-secretstore   SecretSynced   True
```

```text
NAME             TYPE     DATA
payment-secret   Opaque   3
```

## Files

- policy.json
- secretstore.yaml
- externalsecret.yaml
- COMMANDS.txt

## Outcome

Successfully integrated AWS Secrets Manager with Amazon EKS using External Secrets Operator and IRSA. Secrets stored in AWS Secrets Manager were automatically synchronized to Kubernetes Secrets securely without hardcoding sensitive information inside manifests.
