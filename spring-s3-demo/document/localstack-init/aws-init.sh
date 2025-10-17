#!/bin/bash

set -e

echo "Initializing LocalStack..."

# Create bucket
echo "Creating bucket..."
awslocal s3 mb s3://my-app-profile-images 2>/dev/null || true

# Check if lambda.zip exists
LAMBDA_ZIP="/var/lib/localstack/lambda/thumbnail-generator/lambda.zip"
if [ ! -f "$LAMBDA_ZIP" ]; then
    echo "ERROR: lambda.zip not found"
    exit 1
fi

# Deploy Lambda
echo "Deploying Lambda..."
awslocal lambda create-function \
  --function-name thumbnail-generator \
  --runtime python3.11 \
  --handler lambda_function.lambda_handler \
  --zip-file fileb://$LAMBDA_ZIP \
  --role arn:aws:iam::000000000000:role/lambda-role\
  --timeout 10 \
  --memory-size 256

echo "✓ Lambda deployed"

# wait for it to become active
awslocal lambda wait function-active-v2 --function-name thumbnail-generator

# Set up S3 trigger
echo "Setting up S3 trigger..."
awslocal s3api put-bucket-notification-configuration \
  --bucket my-app-profile-images \
  --notification-configuration '{
    "LambdaFunctionConfigurations": [{
      "LambdaFunctionArn": "arn:aws:lambda:us-east-1:000000000000:function:thumbnail-generator",
      "Events": ["s3:ObjectCreated:*"]
    }]
  }'

echo "✓ S3 trigger configured"


echo "Uploading photos to S3..."

PHOTOS_DIR="/photos"
BUCKET="my-app-profile-images"

# Recursively upload all files under /photos while preserving folder structure
find "$PHOTOS_DIR" -type f | while read -r FILE; do
    # Compute S3 key relative to /photos folder
    KEY="${FILE#$PHOTOS_DIR/}"
    echo "Uploading $FILE -> s3://$BUCKET/$KEY"
    awslocal s3 cp "$FILE" "s3://$BUCKET/$KEY"
done

echo "All photos uploaded!"