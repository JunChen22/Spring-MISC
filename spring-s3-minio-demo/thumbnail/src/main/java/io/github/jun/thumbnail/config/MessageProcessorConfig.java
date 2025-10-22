package io.github.jun.thumbnail.config;

import io.github.jun.thumbnail.dto.BucketEvent;
import io.github.jun.thumbnail.service.ThumbnailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MessageProcessorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final ThumbnailService thumbnailService;

    public MessageProcessorConfig(ThumbnailService thumbnailService) {
        this.thumbnailService = thumbnailService;
    }

    /**
     *
     * @return Consumer<BucketEvent>
     */

//    @RabbitListener(queues = "thumbnail")
//    public Consumer<String> thumbnailMessageProcessor() {
//        return payload -> {
//            // payload is MinIO JSON string
//            LOG.info("Received message: {}", payload);
//            // parse bucket/key from JSON
//        };
//    }

    @Bean
    public Consumer<String> thumbnailMessageProcessor() {
        return payload -> {
            // payload is MinIO JSON string
            LOG.info("Received message: {}", payload);
            // parse bucket/key from JSON
        };
    }

//    @Bean
//    public Consumer<BucketEvent> thumbnailMessageProcessor() {
//        // lambda expression of override method accept
//        return event -> {
//            LOG.info("Process message created at {}...", event.getEventCreatedAt());
//            String bucketName = event.getBucketName();
//            String keyName = event.getKeyName();
//
//            switch (event.getEventType()) {
//                case UPLOAD:
//                    System.out.println("received" + event.getEventType() + "event" + "for key: " + keyName + " in bucket: " + bucketName);
//                    //thumbnailService.generateThumbnail(bucketName, keyName);
//                    break;
//
//                case DELETE:
//                    System.out.println("received" + event.getEventType() + "event" + "for key: " + keyName + " in bucket: " + bucketName);
//                    //thumbnailService.deleteThumbnail(bucketName, keyName);
//                    break;
//
//                default:
//                    String errorMessage = "Incorrect event type:" + event.getEventType() + ", expected " +
//                            "UPLOAD and DELETE event";
//                    LOG.warn(errorMessage);
//                    throw new RuntimeException(errorMessage);
//            }
//        };
//    }
}
