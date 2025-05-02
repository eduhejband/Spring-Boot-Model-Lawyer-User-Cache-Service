package com.example.servico_documentos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalStorageService implements StorageService {

    private final Path rootLocation;

    // Use valor padrão caso a propriedade não exista
    public LocalStorageService(@Value("${storage.local.upload-dir:/app/uploads}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        Path destination = this.rootLocation.resolve(fileName);
        file.transferTo(destination);
        return destination.toString();
    }

    @Override
    public byte[] downloadFile(String fileName) throws IOException {
        Path file = rootLocation.resolve(fileName);
        return Files.readAllBytes(file);
    }

    @Override
    public void deleteFile(String fileName) throws IOException {
        Path file = rootLocation.resolve(fileName);
        Files.deleteIfExists(file);
    }
}