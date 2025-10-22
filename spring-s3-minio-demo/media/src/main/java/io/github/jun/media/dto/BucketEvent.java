package io.github.jun.media.dto;

public class BucketEvent {

    public enum Type {
        UPLOAD,
        DELETE
    }

    private final Type eventType;
    private final String bucketName;
    private final String keyName;

    public BucketEvent(Type eventType, String bucketName,  String keyName) {
        this.eventType = eventType;
        this.bucketName = bucketName;
        this.keyName = keyName;
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
}
