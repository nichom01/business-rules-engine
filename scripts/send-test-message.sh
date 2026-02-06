#!/bin/bash

# Script to send a test message to local SQS queue
ENDPOINT_URL="http://localhost:4566"
QUEUE_NAME="business-rules-queue"
QUEUE_URL="$ENDPOINT_URL/000000000000/$QUEUE_NAME"

# Sample message
MESSAGE='{
  "objects": [
    {
      "id": "123",
      "name": "John Doe",
      "email": "john@example.com",
      "amount": 100.50
    },
    {
      "id": "456",
      "name": "Jane Smith",
      "email": "jane@example.com",
      "amount": 250.75
    }
  ],
  "metadata": {
    "source": "test"
  }
}'

echo "Sending test message to queue: $QUEUE_NAME"
echo "Message: $MESSAGE"
echo ""

aws --endpoint-url=$ENDPOINT_URL \
    sqs send-message \
    --queue-url $QUEUE_URL \
    --message-body "$MESSAGE"

if [ $? -eq 0 ]; then
    echo ""
    echo "Message sent successfully!"
else
    echo "Failed to send message. Make sure LocalStack is running and queue exists."
    exit 1
fi
