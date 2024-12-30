package com.example.mugejungsim_be.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * 파일 저장 서비스
 * 이미지를 서버의 지정된 디렉토리에 저장하고 경로를 반환합니다.
 */
@Service
public class FileStorageService {

    private static final Logger LOGGER = Logger.getLogger(FileStorageService.class.getName());

    /**
     * 파일 업로드 기본 디렉토리
     * application.properties 또는 application.yml에서 설정된 값을 가져옵니다.
     */
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 저장된 파일의 URL 기본 경로
     */
    @Value("${file.base-url}")
    private String baseUrl;

    /**
     * 이미지를 저장하고 경로를 반환합니다.
     *
     * @param image 업로드할 이미지 파일 (MultipartFile)
     * @return 저장된 파일의 URL
     * @throws IOException 파일 저장 중 발생할 수 있는 예외
     */
    public String storeImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalFilename = image.getOriginalFilename();
        String extension = getExtension(originalFilename);
        if (!isValidImageExtension(extension)) {
            throw new IllegalArgumentException("Invalid file extension: " + extension);
        }

        String uniqueFilename = UUID.randomUUID().toString() + "." + extension;
        Path targetPath = Paths.get(uploadDir, uniqueFilename);
        Files.createDirectories(targetPath.getParent());
        Files.copy(image.getInputStream(), targetPath);

        return baseUrl + "/" + uniqueFilename;
    }


    /**
     * 파일 확장자를 추출합니다.
     *
     * @param filename 원본 파일 이름
     * @return 파일 확장자 (소문자)
     */
    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) {
            throw new IllegalArgumentException("파일에 확장자가 없습니다: " + filename);
        }
        return filename.substring(dotIndex + 1).toLowerCase();
    }

    /**
     * 확장자가 유효한 이미지 형식인지 확인합니다.
     *
     * @param extension 파일 확장자
     * @return 유효한 확장자 여부
     */
    private boolean isValidImageExtension(String extension) {
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif");
    }
}