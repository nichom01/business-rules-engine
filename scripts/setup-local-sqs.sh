#!/bin/bash

# Script to setup local SQS queue using LocalStack
# Make sure LocalStack is running: docker-compose up -d

ENDPOINT_URL="http://localhost:4566"
QUEUE_NAME="business-rules-queue"
REGION="us-east-1"

echo "Creating SQS queue: $QUEUE_NAME"

# Create the queue
aws --endpoint-url=$ENDPOINT_URL \
    sqs create-queue \
    --queue-name $QUEUE_NAME \
    --region $REGION \
    --attributes VisibilityTimeout=30

if [ $? -eq 0 ]; then
    echo "Queue created successfully!"
    echo ""
    echo "Queue URL: $ENDPOINT_URL/000000000000/$QUEUE_NAME"
    echo ""
    echo "To send a test message:"
    echo "aws --endpoint-url=$ENDPOINT_URL sqs send-message --queue-url $ENDPOINT_URL/000000000000/$QUEUE_NAME --message-body '{\"objects\":[{\"id\":\"123\",\"name\":\"test\"}]}'"
else
    echo "Failed to create queue. Make sure LocalStack is running."
    exit 1
fi
