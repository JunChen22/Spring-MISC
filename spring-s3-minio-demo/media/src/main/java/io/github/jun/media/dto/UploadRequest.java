package io.github.jun.media.dto;

public record UploadRequest(
        String fileName,   // original file name
        String fileType,   // MIME type like "image/jpeg"
        String service,    // e.g., "products", "users", "articles"
        String entityId,   // e.g., productId, userId, articleId
        String subId       // optional: SKU code, profile type, etc.
) {
}
