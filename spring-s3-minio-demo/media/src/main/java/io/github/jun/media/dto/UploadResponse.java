package io.github.jun.media.dto;

public record UploadResponse(
        String uploadUrl,
        String fileUrl
) {
}
