#!/bin/bash
set -e

# Check if alias exists
if mc alias list | grep -q "myminio"; then
  echo "Alias 'myminio' already exists, skipping..."
else
  echo "Setting up alias..."
  mc alias set myminio http://minio:9000 minioadmin minioadmin
fi

echo 'Setting up AMQP notification target...';
mc admin config set myminio notify_amqp:thumbnail_noti enable=on url=amqp://guest:guest@rabbitmq:5672/ exchange=thumbnail exchange_type=topic delivery_mode=1 durable=on;

echo 'Restarting MinIO to apply config...';
mc admin service restart myminio;

mc mb myminio/e-com || true;
mc cp -r ./photos/ myminio/e-com/;
mc mb myminio/public-assets || true;
mc anonymous set download myminio/public-assets;
mc cp -r ./photos/ myminio/public-assets/;

mc event add myminio/e-com arn:minio:sqs::thumbnail_noti:amqp --event put --suffix .jpeg;
mc event add myminio/e-com arn:minio:sqs::thumbnail_noti:amqp --event put --suffix .jpg;
mc event add myminio/e-com arn:minio:sqs::thumbnail_noti:amqp --event put --suffix .png;
echo 'MinIO initialization complete!';

# TODO: check if prefix and suffix filter works for rabbitmq

# using manually script because after adding the notification, it requires a restart to take effect
# and while restarting it requires a tty which is a live terminal so it can display animation or else
# it can stop the process since there's no changes applied.