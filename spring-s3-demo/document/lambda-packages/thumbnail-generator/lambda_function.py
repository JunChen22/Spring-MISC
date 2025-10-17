import json
import boto3
from PIL import Image, UnidentifiedImageError
import io
import os

s3 = boto3.client("s3")

ALLOWED_EXTENSIONS = (".jpg", ".jpeg", ".png", ".gif", ".bmp")

# Define thumbnail sizes
THUMBNAIL_SIZES = {
    "small": (100, 100),
    "medium": (300, 300),
    "large": (600, 600)
}

def lambda_handler(event, context):
    for record in event.get("Records", []):
        bucket = record["s3"]["bucket"]["name"]
        key = record["s3"]["object"]["key"]

        # Skip folders
        if key.endswith("/"):
            continue

        # Skip thumbnails to prevent recursion
        if any(f"/{size_name}/" in key for size_name in THUMBNAIL_SIZES):
            print(f"Skipping thumbnail: {key}")
            continue

        # Skip unsupported file types
        if not key.lower().endswith(ALLOWED_EXTENSIONS):
            print(f"Skipping {key}: not an allowed image type")
            continue

        try:
            obj = s3.get_object(Bucket=bucket, Key=key)
            body_bytes = obj['Body'].read()
            img = Image.open(io.BytesIO(body_bytes))
        except UnidentifiedImageError:
            print(f"Skipping {key}: not a valid image")
            continue

        # Extract folder and filename
        path_parts = key.split("/")
        filename = path_parts[-1]
        folder_prefix = "/".join(path_parts[:-1])  # preserves UUID folder or entity folder

        # Generate thumbnails
        for size_name, size in THUMBNAIL_SIZES.items():
            thumb_img = img.copy()
            thumb_img.thumbnail(size)

            buf = io.BytesIO()
            thumb_img.save(buf, format=img.format)
            buf.seek(0)
            content_type = f"image/{img.format.lower()}"

            thumb_key = f"{folder_prefix}/{size_name}/{filename}"
            print(f"Uploading {size_name} thumbnail: {thumb_key}")

            s3.put_object(
                Bucket=bucket,
                Key=thumb_key,
                Body=buf,
                ContentType=content_type
            )

    return {"status": "ok"}
