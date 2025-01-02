<div align="center">
  <h1>Mugejungsim</h1>
  <p>여기는 팀 "무게중심" 서버입니다.</p>
  <h2>모먼츠</h2>
  <a href="https://github.com/Club-PARD/Mugejungsim_FE" style="font-size: 1.2rem; color: #007BFF; text-decoration: none;">▶️ 프로덕트 설명 페이지로 이동하기</a>
</div>
---

<div align="center">
  <img src="https://github.com/user-attachments/assets/c38a4263-7f92-45b0-a502-00273417cbad" width="200" style="border-radius: 15px; margin-bottom: 20px;">
  <h1 style="font-size: 2.5rem;">Moments</h1>
  <p style="font-size: 1.3rem; color: #555;">여행의 순간을 담아, 감정으로 물들이다</p>
</div>

---

# **Back-End** 🛠️

---

## 🚀 주요 기능

### 1️⃣ **파일 업로드**
- 이미지를 로컬 서버 또는 **Amazon S3**에 저장하고 URL로 접근 가능.
- 지원 확장자: `jpg`, `jpeg`, `png`, `gif` 등.
- **AWS S3** 통합으로 클라우드 기반 파일 저장을 통해 높은 가용성 제공.

### 2️⃣ **사용자 관리**
- OAuth 제공자(`Google`, `Kakao` 등)를 통한 사용자 로그인 및 관리.
- 사용자 생성, 수정, 조회, 삭제 기능 지원.

### 3️⃣ **게시물 관리**
- 게시물 CRUD(생성, 조회, 수정, 삭제) 지원.
- 사용자별 게시물 필터링 가능.

### 4️⃣ **스토리 관리**
- 게시물에 연관된 스토리를 추가, 삭제, 순서 변경 가능.
- 다중 카테고리 지원.
- 이미지를 포함한 다양한 콘텐츠를 지원.

---

## 🛠️ 기술 스택

- **Spring Boot**: 고성능 백엔드 프레임워크.
- **Spring Data JPA**: 데이터베이스와의 간편한 통신.
- **Amazon S3**: 클라우드 기반 파일 저장소.
- **AWS EC2**: 글로벌 서비스를 위한 퍼블릭 IP 기반 배포.
- **MySQL**: 데이터베이스 관리.
- **Nginx**: 리버스 프록시 및 HTTPS 설정.
- **Certbot**: 무료 SSL 인증서 발급 및 자동 갱신.
- **Swagger**: API 문서화.
- **Lombok**: 개발 생산성 향상을 위한 코드 간소화.
- **SLF4J**: 로깅 및 디버깅 도구.

---

## 📂 디렉토리 구조

- **mugejungsim_be/**
    - **controller/**
        - `FileController.java`: 파일 업로드 및 다운로드 관리.
        - `PostController.java`: 게시물 관리 API.
        - `StoryController.java`: 스토리 관리 API.
        - `UserController.java`: 사용자 관리 API.
    - **dto/**
        - `PostDto.java`: 게시물 데이터 DTO.
        - `StoryDto.java`: 스토리 데이터 DTO.
        - `UserDto.java`: 사용자 데이터 DTO.
    - **entity/**
        - `Post.java`: 게시물 엔티티.
        - `Story.java`: 스토리 엔티티.
        - `User.java`: 사용자 엔티티.
    - **repository/**
        - `PostRepository.java`: 게시물 데이터 접근.
        - `StoryRepository.java`: 스토리 데이터 접근.
        - `UserRepository.java`: 사용자 데이터 접근.
    - **service/**
        - `FileStorageService.java`: 파일 저장 서비스.
        - `PostService.java`: 게시물 관련 서비스.
        - `StoryService.java`: 스토리 관련 서비스.
        - `UserService.java`: 사용자 관련 서비스.
    - **기타 주요 파일**:
        - `CorsConfig.java`: CORS 설정.
        - `MugejungsimBeApplication.java`: Spring Boot 메인 애플리케이션.
        - `S3Config.java`: Amazon S3 설정.
        - `S3Uploader.java`: S3 업로드 유틸리티.
        - `SecurityConfig.java`: 보안 설정.
        - `SwaggerConfig.java`: Swagger API 문서화 설정.

---

## 🗂️ 배포 성과

### ✅ **AWS 기반 배포**
- **EC2 인스턴스**를 활용하여 프로젝트를 퍼블릭 IP와 도메인(`mugejunsim.store`)으로 배포.
- **Nginx**를 이용해 리버스 프록시와 정적 파일 서빙 구현.
- 백엔드와 클라이언트 간 **CORS 이슈 해결**.

### ✅ **HTTPS 적용**
- **Certbot**을 사용하여 무료 SSL 인증서를 발급받아 HTTPS 적용.
- HTTP에서 HTTPS로의 자동 리디렉션 설정.

### ✅ **Amazon S3 통합**
- 클라우드 기반 파일 저장을 위해 S3를 통합.
- 파일 업로드 후 URL 반환으로 전역 액세스 가능.

---

## 📑 API 명세서

### 파일 업로드
| 메서드 | URL               | 설명                | 요청 데이터        | 응답 데이터          |
|--------|-------------------|---------------------|--------------------|----------------------|
| POST   | `/files/upload`   | 파일 업로드         | `MultipartFile`    | 업로드된 파일 URL    |
| GET    | `/files/{filename}` | 파일 다운로드      | `filename`         | 파일 리소스          |

### 사용자 관리
| 메서드 | URL               | 설명                | 요청 데이터              | 응답 데이터                   |
|--------|-------------------|---------------------|--------------------------|-------------------------------|
| POST   | `/api/users/save` | 사용자 저장 및 로그인 | `{name, provider}`       | `{userId, name, provider}`    |

### 게시물 관리
| 메서드 | URL                   | 설명                | 요청 데이터               | 응답 데이터                   |
|--------|-----------------------|---------------------|---------------------------|-------------------------------|
| POST   | `/api/posts`          | 게시물 생성         | `{userId, Post}`          | `{postId, pid, userId}`       |
| PUT    | `/api/posts/{postId}` | 게시물 수정         | `{userId, updatedPost}`   | 업데이트된 게시물              |
| DELETE | `/api/posts/{postId}` | 게시물 삭제         | `{userId}`                | -                             |

### 스토리 관리
| 메서드 | URL                      | 설명                | 요청 데이터                        | 응답 데이터               |
|--------|--------------------------|---------------------|------------------------------------|--------------------------|
| POST   | `/api/stories`           | 스토리 생성         | `{postId, content, categories...}` | 생성된 스토리 ID          |
| PUT    | `/api/stories/{storyId}` | 스토리 수정         | `{content, categories...}`         | 업데이트된 스토리 ID      |
| DELETE | `/api/stories/{storyId}` | 스토리 삭제         | `{postId}`                         | 성공 메시지               |

---

## 📑 ERD 구조

```plaintext
+------------------+          1        +------------------+
|      User        |<----------------->|      Post        |
+------------------+                   +------------------+
| id (PK)          |                   | id (PK)          |
| name             |                   | pid              |
| provider         |                   | title            |
|------------------|                   |------------------|
| posts [1..*]     |                   | stories [1..*]   |
+------------------+                   +------------------+
                                           |
                                           |
                                           | 1
                                           v
                                    +------------------+
                                    |      Story       |
                                    +------------------+
                                    | id (PK)          |
                                    | content          |
                                    | pid              |
                                    | categories       |
                                    | imagePath        |
                                    | post_id (FK)     |
                                    +------------------+

