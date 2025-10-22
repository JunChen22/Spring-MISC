package io.github.jun.thumbnail.service;

import reactor.core.publisher.Mono;

public interface ThumbnailService {

    /**
     * generate thumbnail for the given object in S3 bucket
     * @param bucketName S3 bucket name
     * @param keyName S3 object key name
     * @return Mono<Void>
     */
    Mono<Void> generateThumbnail(String bucketName, String keyName);

    /**
     * delete thumbnail for the given object in S3 bucket
     * @param bucketName S3 bucket name
     * @param keyName S3 object key name
     * @return Mono<Void>
     */
    Mono<Void> deleteThumbnail(String bucketName, String keyName);
}
