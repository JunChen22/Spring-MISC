 package io.github.jun.thumbnail.dto;

 import java.time.ZonedDateTime;

 import static java.time.ZonedDateTime.now;

 public class BucketEvent {

    public enum Type {
        UPLOAD,
        DELETE
    }

    private final Type eventType;
    private final String bucketName;
    private final String keyName;
    private final ZonedDateTime eventCreatedAt;

    public BucketEvent(Type eventType, String bucketName, String keyName) {
        this.eventType = eventType;
        this.bucketName = bucketName;
        this.keyName = keyName;
        this.eventCreatedAt = now();
    }

    public Type getEventType() {
        return eventType;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getKeyName() {
        return keyName;
    }

     public ZonedDateTime getEventCreatedAt() {
         return eventCreatedAt;
     }
 }
