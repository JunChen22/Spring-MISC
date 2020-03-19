package com.amazons3.jun.amazonimagestore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.http.entity.ContentType.*;

@Service
public class UserService {

    @Value("${amazonProperties.bucketName}")
    private String path;
    private final FileStore fileStore;

    @Autowired
    public UserService(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    public void save(MultipartFile file){
        isImage(file);
        Map<String, String> metadata = extractMetadata(file);
        String fileName = file.getOriginalFilename();
        try {
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
        } catch (IOException e){
            throw new IllegalStateException(e);
        }
    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }
}
