package io.github.jun.media.service.impl;

import io.github.jun.media.dto.BucketEvent;
import io.github.jun.media.dto.UploadRequest;
import io.github.jun.media.dto.UploadResponse;
import io.github.jun.media.service.BucketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

import static io.github.jun.media.dto.BucketEvent.Type.UPLOAD;

@Service
public class BucketServiceImpl implements BucketService {

    private static final Logger LOG = LoggerFactory.getLogger(BucketServiceImpl.class);

    private final S3Presigner presigner;

    private final S3Client client;

    private final StreamBridge streamBridge;

    private final String bucketName = "e-com";

    private final String BINDING_NAME = "thumbnail-out-0";

    @Autowired
    public BucketServiceImpl(S3Presigner presigner, S3Client client, StreamBridge streamBridge) {
        this.presigner = presigner;
        this.client = client;
        this.streamBridge = streamBridge;
    }

    @Override
    public Mono<UploadResponse> getPresignedUrl(UploadRequest request) {
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

        String fileUrl = "http://localhost:9000/" + bucketName + "/" + key;

        return Mono.just(new UploadResponse(presigned.url().toString(), fileUrl));
    }
//
//    public String getPresignedDownloadUrl(@RequestParam String key) {
//
//        //
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .build();
//
//        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
//                .signatureDuration(Duration.ofMinutes(10))
//                .getObjectRequest(getObjectRequest)
//                .build();
//
//        PresignedGetObjectRequest presigned = presigner.presignGetObject(presignRequest);
//
//        return presigned.url().toString();
//    }


    @Override
    public Mono<String> test() {
        return sendThumbnailMessage(BINDING_NAME, new BucketEvent(UPLOAD, "test-bucket", "test-key"))
                .thenReturn("Bucket Service is running.");
    }

    private Mono<Void> sendThumbnailMessage(String bindingName, BucketEvent event) {
        return Mono.fromRunnable(() -> {
            LOG.debug("Sending a {} message to {}", event.getEventType(), bindingName);
            Message<BucketEvent> message = MessageBuilder.withPayload(event)
                    .setHeader("event-type", event.getEventType())
                    .build();
            boolean messageSendSuccess = streamBridge.send(bindingName, message);
            if (!messageSendSuccess) {
                throw new RuntimeException("Failed to send message to " + bindingName);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}
