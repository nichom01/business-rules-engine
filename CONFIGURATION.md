# Configuration Guide

## Connecting to an Existing Local SQS Queue

If you already have a local SQS queue running (e.g., from LocalStack or another application), configure the application to consume from it.

### Option 1: Using Queue Name

Set the queue name that already exists in your LocalStack:

```bash
export AWS_SQS_ENDPOINT=http://localhost:4566
export SQS_QUEUE_NAME=your-existing-queue-name
```

### Option 2: Using Queue URL (More Explicit)

If you know the full queue URL:

```bash
export AWS_SQS_ENDPOINT=http://localhost:4566
export SQS_QUEUE_URL=http://localhost:4566/000000000000/your-existing-queue-name
export SQS_QUEUE_NAME=your-existing-queue-name
```

## Configuring Batch Sizes

The application supports configurable batch processing to optimize throughput:

### Configuration Properties

| Property | Environment Variable | Default | Description |
|----------|---------------------|---------|-------------|
| `sqs.max-messages-per-poll` | `SQS_MAX_MESSAGES_PER_POLL` | 10 | Number of messages to fetch per poll (SQS max: 10) |
| `sqs.max-concurrent-messages` | `SQS_MAX_CONCURRENT_MESSAGES` | 10 | Number of messages processed concurrently |
| `sqs.poll-timeout-seconds` | `SQS_POLL_TIMEOUT_SECONDS` | 20 | Long polling timeout in seconds (max: 20) |

### Example Configurations

#### High Throughput (Process Many Messages Quickly)
```bash
export SQS_MAX_CONCURRENT_MESSAGES=50
export SQS_MAX_MESSAGES_PER_POLL=10
export SQS_POLL_TIMEOUT_SECONDS=20
```

#### Low Latency (Process Messages Immediately)
```bash
export SQS_MAX_CONCURRENT_MESSAGES=5
export SQS_MAX_MESSAGES_PER_POLL=1
export SQS_POLL_TIMEOUT_SECONDS=5
```

#### Balanced (Default)
```bash
export SQS_MAX_CONCURRENT_MESSAGES=10
export SQS_MAX_MESSAGES_PER_POLL=10
export SQS_POLL_TIMEOUT_SECONDS=20
```

### Configuration via application.yml

You can also set these in `application-local.yml`:

```yaml
sqs:
  queue-name: your-existing-queue-name
  max-concurrent-messages: 20
  max-messages-per-poll: 10
  poll-timeout-seconds: 20
```

## Complete Example: Connecting to Existing Queue with Custom Batch Size

```bash
# Set LocalStack endpoint
export AWS_SQS_ENDPOINT=http://localhost:4566

# Set your existing queue name
export SQS_QUEUE_NAME=my-existing-queue

# Configure batch processing
export SQS_MAX_CONCURRENT_MESSAGES=25
export SQS_MAX_MESSAGES_PER_POLL=10
export SQS_POLL_TIMEOUT_SECONDS=20

# Run the application
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Notes

- **max-messages-per-poll**: SQS has a hard limit of 10 messages per poll. Setting this higher than 10 will be capped at 10.
- **max-concurrent-messages**: This controls how many messages are processed in parallel. Higher values increase throughput but also increase resource usage.
- **poll-timeout-seconds**: Longer timeouts reduce empty poll requests but may delay message processing. Maximum is 20 seconds.
