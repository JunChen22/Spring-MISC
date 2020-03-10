package com.example.filestore.demo.filestoredemo.service;

import com.example.filestore.demo.filestoredemo.model.File;
import com.example.filestore.demo.filestoredemo.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileStorageService {

    @Autowired
    private FileRepository fileRepository;

    public File saveFile(MultipartFile file){
        String fileName = file.getOriginalFilename();
        try{
            File f = new File(fileName, file.getContentType(), file.getBytes());
            return fileRepository.save(f);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public Optional<File> getFile(int id){
        return fileRepository.findById(id);
    }

    public List<File> getAllFiles(){
        return fileRepository.findAll();
    }
}
