# Local Development Guide

## Quick Start

### 1. Start LocalStack
```bash
docker-compose up -d
```

Wait for LocalStack to be ready (check logs: `docker-compose logs -f`)

### 2. Create SQS Queue
```bash
./scripts/setup-local-sqs.sh
```

### 3. Run Application
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### 4. Send Test Message
```bash
./scripts/send-test-message.sh
```

## Environment Variables

You can override defaults using environment variables:

```bash
export AWS_SQS_ENDPOINT=http://localhost:4566
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_REGION=us-east-1
export SQS_QUEUE_NAME=business-rules-queue
```

## Manual Queue Operations

### Create Queue
```bash
aws --endpoint-url=http://localhost:4566 \
    sqs create-queue \
    --queue-name business-rules-queue \
    --region us-east-1
```

### List Queues
```bash
aws --endpoint-url=http://localhost:4566 \
    sqs list-queues \
    --region us-east-1
```

### Send Message
```bash
aws --endpoint-url=http://localhost:4566 \
    sqs send-message \
    --queue-url http://localhost:4566/000000000000/business-rules-queue \
    --message-body '{"objects":[{"id":"123","name":"test"}]}'
```

### Receive Messages
```bash
aws --endpoint-url=http://localhost:4566 \
    sqs receive-message \
    --queue-url http://localhost:4566/000000000000/business-rules-queue \
    --region us-east-1
```

## Troubleshooting

### LocalStack not starting
- Check if port 4566 is already in use: `lsof -i :4566`
- Check Docker is running: `docker ps`

### Queue not found
- Verify queue exists: `aws --endpoint-url=http://localhost:4566 sqs list-queues`
- Check queue name matches in `application-local.yml`

### Messages not being consumed
- Check application logs for errors
- Verify queue name in `application-local.yml` matches created queue
- Ensure LocalStack is healthy: `curl http://localhost:4566/_localstack/health`
