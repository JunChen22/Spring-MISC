package com.example.filestore.demo.filestoredemo.repository;

import com.example.filestore.demo.filestoredemo.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {

}
