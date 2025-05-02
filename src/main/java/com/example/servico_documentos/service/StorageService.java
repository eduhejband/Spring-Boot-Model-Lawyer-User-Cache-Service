package com.example.servico_documentos.service;


import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface StorageService {
    String uploadFile(MultipartFile file, String fileName) throws IOException;
    byte[] downloadFile(String fileName) throws IOException;
    void deleteFile(String fileName) throws IOException;
}