package io.github.jun.controller;

import io.github.jun.dto.UploadRequest;
import io.github.jun.dto.UploadResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private final S3Presigner presigner;

    private final String bucketName = "my-app-profile-images";

    public UploadController(S3Presigner presigner) {
        this.presigner = presigner;
    }

//    @PostMapping("/presigned-url")
//    public UploadResponse getPresignedUrl(@RequestBody UploadRequest request) {
//        String service = request.service(); // "products", "users", "articles"  // TODO: create enum for this
//        String entityId = request.entityId(); // productId, userId, articleId
//        String subId = request.subId(); // skuCode for product, optional otherwise
//
//        String folderPath = switch (service) {
//            case "products" -> "products/" + entityId + "/" + subId;
//            case "users"    -> "users/profiles/" + entityId;
//            case "articles" -> "articles/" + entityId;
//            default -> throw new IllegalArgumentException("Unknown service type");
//        };
//
//        String key = folderPath + "/" + request.fileName();
//
//        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .contentType(request.fileType())
//                .build();
//
//        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(10))
//                .putObjectRequest(putObjectRequest)
//                .build();
//
//        PresignedPutObjectRequest presigned = presigner.presignPutObject(presignRequest);
//
//        URL uploadUrl = presigned.url();
//        String fileUrl = "http://localhost:4566/" + bucketName + "/" + key;
//
//        return new UploadResponse(uploadUrl.toString(), fileUrl);
//    }

    @PostMapping("/full")
    public UploadResponse getPresignedUrl(@RequestBody UploadRequest request) {
        String service = request.service(); // "products", "users", "articles"  // TODO: create enum for this
        String entityId = request.entityId(); // productId, userId, articleId
        String subId = request.subId(); // skuCode for product, optional otherwise

        String folderPath = switch (service) {
            case "products" -> "products/" + entityId + "/" + subId;
            case "users"    -> "users/profiles/" + entityId;
            case "articles" -> "articles/" + entityId;
            default -> throw new IllegalArgumentException("Unknown service type");
        };

        String key = folderPath + "/" + UUID.randomUUID() + request.fileName();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(request.fileType())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presigned = presigner.presignPutObject(presignRequest);

        String uploadUrl = "curl -X PUT \""
                + presigned.url()
                + "\" -H \"Content-Type: image/jpeg\" --data-binary \"@Google.jpeg\"";

        System.out.println(uploadUrl);
        String fileUrl = "http://localhost:4566/" + bucketName + "/" + key;

        return new UploadResponse(uploadUrl, fileUrl);
    }
}
