package com.example.mugejungsim_be.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty file.");
        }

        String originalFilename = image.getOriginalFilename();
        String extension = getExtension(originalFilename);

        if (!isValidImageExtension(extension)) {
            throw new IllegalArgumentException("Unsupported file extension: " + extension);
        }

        String uniqueFilename = UUID.randomUUID().toString() + "." + extension;
        Path targetPath = Paths.get(uploadDir, uniqueFilename);

        Files.createDirectories(targetPath.getParent());
        Files.copy(image.getInputStream(), targetPath);

        return uniqueFilename;
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1) {
            throw new IllegalArgumentException("File does not have an extension: " + filename);
        }
        return filename.substring(dotIndex + 1).toLowerCase();
    }

    private boolean isValidImageExtension(String extension) {
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif");
    }
}
