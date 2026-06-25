# Exercise 26 – S3 Backup Solution

## Objective

Implement backup and restore strategy using Amazon S3.

## Backup Steps

1. Compress application files

```powershell
Compress-Archive -Path .\customer-service -DestinationPath .\customer-backup.zip
```

2. Upload backup to S3

```powershell
aws s3 cp customer-backup.zip s3://lalith-ex26-backup-2026/
```

3. Verify upload

```powershell
aws s3 ls s3://lalith-ex26-backup-2026
```

## Restore Steps

1. Download backup

```powershell
aws s3 cp s3://lalith-ex26-backup-2026/customer-backup.zip .
```

2. Extract archive

```powershell
Expand-Archive -Path customer-backup.zip -DestinationPath restored-project
```

3. Build application

```powershell
cd restored-project\customer-service
mvn clean package
```

## Validation

* Backup stored successfully in S3.
* Backup downloaded successfully.
* Project restored successfully.
* Maven build completed successfully.

## AWS Services Used

* Amazon S3
* AWS CLI

## Outcome

Implemented backup and restore strategy for application files and configuration files using Amazon S3.
