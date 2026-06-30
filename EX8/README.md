# Exercise 8 – Egress Restriction Incident

## Objective

Investigate why an application running in Amazon EKS cannot communicate with Amazon DynamoDB.

The simulated production incident reports:

```
Application Logs

Connection timeout
```

The objective is to determine whether the failure is caused by:

- Network Policies
- Security Groups
- Route Tables
- NAT Gateway
- VPC Endpoints

---

# Architecture

```
                    +----------------------+
                    |      DynamoDB        |
                    +----------+-----------+
                               ^
                               |
                      Internet Gateway
                               ^
                               |
                      Route Table (0.0.0.0/0)
                               ^
                               |
                  Security Group (Outbound)
                               ^
                               |
                      Worker Node (EC2)
                               ^
                               |
                     Payment Service Pod
```

---

# Project Structure

```
EX8/
│
├── README.md
└── commands.txt
```

---

# Investigation Steps

## 1. Verify Cluster Health

Verified:

- Worker Node
- CoreDNS
- Metrics Server
- AWS Load Balancer Controller
- Payment Service Pods

Commands

```
kubectl get nodes
kubectl get pods -A
kubectl get svc
kubectl get ingress
```

Result

Cluster healthy.

---

## 2. Verify Application

Checked:

```
kubectl get deployment
kubectl get pods -o wide
```

Result

Deployment available.

Pods running successfully.

---

## 3. Test External Connectivity

Entered the application Pod.

```
kubectl exec -it <pod> -- sh
```

Executed

```
curl https://dynamodb.us-east-1.amazonaws.com
```

Result

```
healthy: dynamodb.us-east-1.amazonaws.com
```

Conclusion

The application successfully reached DynamoDB.

No egress connectivity issue was observed.

---

## 4. Network Policy Investigation

Command

```
kubectl get networkpolicy -A
```

Result

```
No resources found
```

Conclusion

No Network Policies exist.

Kubernetes is not blocking outbound traffic.

---

## 5. Security Group Investigation

Obtained EC2 instance and Security Group.

Verified outbound rules.

Result

```
0.0.0.0/0
All Traffic
```

Conclusion

Outbound traffic is fully allowed.

Security Group is not the cause.

---

## 6. Route Table Investigation

Verified subnet routing.

Result

```
0.0.0.0/0 → Internet Gateway
```

Conclusion

The subnet has a valid default route.

Traffic can leave the VPC successfully.

---

## 7. VPC Endpoint Investigation

Checked for DynamoDB VPC Endpoints.

Result

No VPC Endpoint found.

Conclusion

The cluster uses the Internet Gateway to reach DynamoDB.

A VPC Endpoint is not required in this architecture.

---

# Root Cause Analysis

Expected Incident

```
Application
↓

Connection Timeout
```

Possible Causes

- Network Policy blocks egress
- Security Group blocks outbound HTTPS
- Missing Route Table entry
- Missing NAT Gateway (private subnet)
- Missing or misconfigured VPC Endpoint

---

# Findings

| Investigation | Status |
|---------------|--------|
| Cluster Health | ✅ Healthy |
| Application Pods | ✅ Running |
| External Connectivity | ✅ Successful |
| Network Policies | ✅ No restrictions |
| Security Groups | ✅ Outbound allowed |
| Route Table | ✅ Default route configured |
| VPC Endpoint | ✅ Not required |

---

# Final Conclusion

The simulated production incident could not be reproduced.

The application successfully communicated with DynamoDB.

No networking misconfiguration was found.

The EKS cluster networking configuration is healthy, and no egress restrictions are present.
