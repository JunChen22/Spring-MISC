package io.github.jun.media.controller;

import io.github.jun.media.dto.UploadRequest;
import io.github.jun.media.dto.UploadResponse;
import io.github.jun.media.service.BucketService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private final BucketService bucketService;

    public UploadController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @GetMapping("/test")
    public Mono<String> test() {
        return bucketService.test();
    }

    @PostMapping("/presigned-url")
    public Mono<UploadResponse> getPresignedUrl(@RequestBody UploadRequest request) {
        return bucketService.getPresignedUrl(request);
    }
//
//    @GetMapping("/presigned-get")
//    public String getPresignedDownloadUrl(@RequestParam String key) {
//        // check in redis first
//    }
}
