#!/bin/bash

set -e

# Try normal rm first
[ -d "build" ] && sudo rm -r build
[ -f "lambda.zip" ] && sudo rm lambda.zip

docker run --rm \
  -v "$PWD":/var/task \
  -w /var/task amazonlinux:2023 \
  bash -c "yum install -y python3.11 python3.11-pip zip &&
          python3.11 -m pip install -r requirements.txt -t build &&
          cp lambda_function.py build/ &&
          cd build && zip -r9 ../lambda.zip ."

# Clean up build directory
sudo rm -r build