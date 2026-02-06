# Business Rules Engine

A stateless Spring Boot application that processes JSON objects from AWS SQS using Drools rules engine for validation and transformation.

## Features

- **SQS Integration**: Consumes messages from AWS SQS queue
- **Drools Rules Engine**: Validates and transforms data using business rules
- **Parallel Processing**: Rules are executed in parallel for better performance
- **Stateless Design**: Each request is processed independently
- **Error Handling**: Comprehensive error reporting and validation

## Prerequisites

- Java 17+
- Maven 3.6+
- Docker and Docker Compose (for local development)
- AWS Account with SQS queue configured (for production)
- AWS Credentials configured (via environment variables, IAM role, or AWS CLI)

## Configuration

For detailed configuration options including connecting to existing queues and batch size tuning, see [CONFIGURATION.md](CONFIGURATION.md).

## Local Development Setup

### Using LocalStack (Recommended)

1. **Start LocalStack**:
   ```bash
   docker-compose up -d
   ```

2. **Create the SQS queue**:
   ```bash
   chmod +x scripts/setup-local-sqs.sh
   ./scripts/setup-local-sqs.sh
   ```
   
   Or manually:
   ```bash
   aws --endpoint-url=http://localhost:4566 \
       sqs create-queue \
       --queue-name business-rules-queue \
       --region us-east-1
   ```

3. **Run the application with local profile**:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```
   
   Or set environment variables:
   ```bash
   export AWS_SQS_ENDPOINT=http://localhost:4566
   export AWS_ACCESS_KEY_ID=test
   export AWS_SECRET_ACCESS_KEY=test
   export SQS_QUEUE_NAME=business-rules-queue
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```

4. **Send a test message**:
   ```bash
   aws --endpoint-url=http://localhost:4566 \
       sqs send-message \
       --queue-url http://localhost:4566/000000000000/business-rules-queue \
       --message-body '{"objects":[{"id":"123","name":"test","email":"test@example.com","amount":100}]}'
   ```

### Configuration for Local Development

The `application-local.yml` profile is configured with:
- LocalStack endpoint: `http://localhost:4566`
- Dummy AWS credentials: `test/test`
- Debug logging enabled

## Production Configuration

Set the following environment variables:

- `AWS_REGION`: AWS region (default: us-east-1)
- `SQS_QUEUE_NAME`: Name of the SQS queue (default: business-rules-queue)
- `SQS_MAX_CONCURRENT_MESSAGES`: Max concurrent message processing (default: 10)
- `SQS_MAX_MESSAGES_PER_POLL`: Messages per poll (default: 10, max: 10)
- `SQS_POLL_TIMEOUT_SECONDS`: Poll timeout in seconds (default: 20, max: 20)

## Building and Running

```bash
# Build the project
mvn clean package

# Run the application (production)
mvn spring-boot:run

# Run the application (local development)
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Message Format

The application expects JSON messages in the following format:

```json
{
  "objects": [
    {
      "id": "123",
      "name": "John Doe",
      "email": "john@example.com",
      "amount": 100.50
    }
  ],
  "metadata": {
    "source": "api"
  }
}
```

## Response Format

The processing result includes:

- `success`: Boolean indicating if processing was successful
- `message`: Success or error message
- `errors`: List of validation errors (if any)
- `transformedData`: Transformed objects (if successful)

## Adding Rules

1. Create new `.drl` files in `src/main/resources/rules/`
2. Update `DroolsConfig.java` to include the new rule file
3. Use `RuleContext` to access data and add errors or transformations

## Rule Groups

Rules are organized into logical groups:
- `validation-rules`: For data validation
- `transformation-rules`: For data transformation

Rules can be executed in parallel by grouping them appropriately.
