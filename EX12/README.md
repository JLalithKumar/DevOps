# Exercise 12 -- Node NotReady Production Incident

## Objective

Simulate and investigate a Kubernetes node storage pressure incident on
Amazon EKS and recover the node safely.

## Environment

-   Amazon EKS
-   Managed Node Group (t3.small)
-   Amazon Linux 2023
-   Kubernetes v1.34.x

## Steps Performed

1.  Created an EKS cluster with SSH access.
2.  Connected to a worker node.
3.  Verified disk usage using `df -h`.
4.  Filled the node disk using `fallocate`.
5.  Monitored node status using:
    -   `kubectl get nodes -w`
    -   `kubectl describe node`
6.  Observed:
    -   `DiskPressure=True`
    -   `FreeDiskSpaceFailed`
    -   `ImageGCFailed`
    -   `EvictionThresholdMet`
    -   `node.kubernetes.io/disk-pressure:NoSchedule`
7.  Removed the temporary files.
8.  Restarted the kubelet.
9.  Verified:
    -   `DiskPressure=False`
    -   `Ready=True`
    -   No taints remained.

## Root Cause

The node's available disk space dropped below the kubelet eviction
threshold. Kubelet attempted image garbage collection but could not
reclaim enough space, resulting in DiskPressure and eviction attempts.

## Recovery

-   Removed the large files created for the simulation.
-   Restarted kubelet.
-   Confirmed the node returned to a healthy state.

## Key Learnings

-   DiskPressure is different from NotReady.
-   Kubernetes first applies a NoSchedule taint before a node becomes
    unavailable.
-   Kubelet attempts image garbage collection before evicting workloads.
-   Monitoring node storage is essential to prevent production outages.

## Result

Exercise 12 completed successfully with a live Amazon EKS demonstration
of node disk pressure detection and recovery.
