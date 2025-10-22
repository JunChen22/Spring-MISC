package io.github.jun.media.service;

import io.github.jun.media.dto.UploadRequest;
import io.github.jun.media.dto.UploadResponse;
import reactor.core.publisher.Mono;

public interface BucketService {

    Mono<String> test();

    Mono<UploadResponse> getPresignedUrl(UploadRequest request);
}
