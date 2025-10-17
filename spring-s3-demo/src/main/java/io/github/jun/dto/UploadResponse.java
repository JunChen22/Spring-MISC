package io.github.jun.dto;

public record UploadResponse(
        String uploadUrl,
        String fileUrl
) {
}
