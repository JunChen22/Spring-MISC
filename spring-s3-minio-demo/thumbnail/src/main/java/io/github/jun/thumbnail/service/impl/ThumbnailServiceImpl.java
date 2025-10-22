package io.github.jun.thumbnail.service.impl;

import io.github.jun.thumbnail.service.ThumbnailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ThumbnailServiceImpl implements ThumbnailService {

    private static final Logger LOG = LoggerFactory.getLogger(ThumbnailServiceImpl.class);

    private final S3Client s3Client;

    private final S3Presigner presigner;

    @Autowired
    public ThumbnailServiceImpl(S3Client s3Client, S3Presigner presigner) {
        this.s3Client = s3Client;
        this.presigner = presigner;
    }

    @Override
    public Mono<Void> generateThumbnail(String bucketName, String keyName) {
        // 1. Download image from MinIO
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getRequest)) {
            BufferedImage originalImage = ImageIO.read(s3Object);

            // 2. Generate resized images
            generateAndUpload(bucketName, keyName, originalImage, 100, "small");
            generateAndUpload(bucketName, keyName, originalImage, 300, "medium");
            generateAndUpload(bucketName, keyName, originalImage, 600, "large");
        } catch (IOException e) {
            LOG.error("Error processing image for key {}: {}", keyName, e.getMessage());
            throw new RuntimeException(e); // wrap it in an unchecked exception
        }
        return Mono.empty();
    }

    private void generateAndUpload(String bucket, String key, BufferedImage original, int size, String label) throws IOException {
        // Create thumbnail
        BufferedImage thumbnail = resizeImage(original, size);

        // Convert BufferedImage to bytes
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, "jpeg", os);
        byte[] bytes = os.toByteArray();

        // Upload back to MinIO
        String thumbKey = key.replace("original", label);
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(thumbKey)
                .contentType("image/jpeg")
                .build();

        s3Client.putObject(putRequest, RequestBody.fromBytes(bytes));

        System.out.println("Uploaded " + label + " thumbnail to: " + thumbKey);
    }

    private BufferedImage resizeImage(BufferedImage original, int targetWidth) {
        int targetHeight = (int) ((double) original.getHeight() / original.getWidth() * targetWidth);
        Image scaled = original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    @Override
    public Mono<Void> deleteThumbnail(String bucketName, String keyName) {
        return null;
    }
}