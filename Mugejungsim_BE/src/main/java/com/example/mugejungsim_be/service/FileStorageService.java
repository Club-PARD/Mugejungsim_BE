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

/**
 * 파일 저장 서비스
 * 이미지를 서버의 지정된 디렉토리에 저장하고 경로를 반환합니다.
 */
@Service
public class FileStorageService {

    /**
     * 파일 업로드 기본 디렉토리
     * application.properties 또는 application.yml에서 설정된 값을 가져옵니다.
     */
    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 이미지를 저장하고 경로를 반환합니다.
     *
     * @param image 업로드할 이미지 파일 (MultipartFile)
     * @return 저장된 파일 경로
     * @throws IOException 파일 저장 중 발생할 수 있는 예외
     */
    @Transactional
    public String storeImage(MultipartFile image) throws IOException {
        // 이미지가 비어있는지 확인
        if (image.isEmpty()) {
            throw new IllegalArgumentException("비어있는 파일은 저장할 수 없습니다.");
        }

        // 원본 파일 이름에서 확장자 추출
        String originalFilename = image.getOriginalFilename();
        String extension = getExtension(originalFilename);

        // 유효한 이미지 확장자인지 확인
        if (!isValidImageExtension(extension)) {
            throw new IllegalArgumentException("유효하지 않은 파일 확장자: " + extension);
        }

        // 유니크한 파일 이름 생성
        String uniqueFilename = UUID.randomUUID().toString() + "." + extension;

        // 저장 경로 설정
        Path targetPath = Paths.get(uploadDir, uniqueFilename);

        // 디렉토리가 존재하지 않으면 생성
        Files.createDirectories(targetPath.getParent());

        // 파일 저장
        Files.copy(image.getInputStream(), targetPath);

        // 저장된 파일 경로 반환
        return targetPath.toString();
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
