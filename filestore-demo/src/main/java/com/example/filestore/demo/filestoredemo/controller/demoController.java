package com.example.filestore.demo.filestoredemo.controller;

import com.example.filestore.demo.filestoredemo.model.File;
import com.example.filestore.demo.filestoredemo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class demoController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/")
    public String home(){
        return "at landing page";
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("files") MultipartFile[] files){
        for(MultipartFile file: files){;
            fileStorageService.saveFile(file);
        }
        return new ResponseEntity<>("file uploaded", HttpStatus.CREATED);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> getFiles(@PathVariable int fileId){
        File file = fileStorageService.getFile(fileId).get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, file.getFileName())
                .body(new ByteArrayResource(file.getData()));
    }

    @GetMapping("/viewall")
    public List<File> viewAll(){
        return fileStorageService.getAllFiles();
    }
}
