# Exercise 24 – DynamoDB Application Deployment Using IRSA

## Objective

Deploy a Spring Boot application on Amazon EKS that performs CRUD operations on DynamoDB without using AWS Access Keys.

## Technologies

* Java 21
* Spring Boot
* Docker
* Amazon EKS
* Amazon DynamoDB
* Amazon ECR
* IAM Roles for Service Accounts (IRSA)
* Kubernetes

## Architecture

User → LoadBalancer → Kubernetes Service → Pod → ServiceAccount → IAM Role (IRSA) → DynamoDB

## Features

* Create Customer
* Read Customer
* Update Customer
* No AWS Access Keys used
* IAM Role based authentication through IRSA

## DynamoDB Table

Table Name: customer-data

Partition Key:
CustomerId

## Deployment

Build:

mvn clean package

Docker:

docker build -t customer-service:v4 .

Push to ECR:

docker push 810648236794.dkr.ecr.us-east-1.amazonaws.com/customer-service:latest

Deploy:

kubectl apply -f deployment.yaml
kubectl apply -f service.yaml

## Validation

Create Customer:
POST /customer

Read Customer:
GET /customer/{id}

Update Customer:
PUT /customer/{id}

## Outcome

Successfully deployed a Spring Boot application on Amazon EKS using IRSA to securely access DynamoDB without AWS access keys.
