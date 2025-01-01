# mugejungsim
여기는 무게중심 서버입니다.


# **Mugejungsim BE (Back-End)** 🛠️



---

## 🚀 주요 기능

### 1️⃣ **파일 업로드**
- 이미지를 로컬 서버 또는 Amazon S3에 저장하고 URL로 접근 가능.
- 지원 확장자: `jpg`, `jpeg`, `png`, `gif` (이 정도면 충분할듯)

### 2️⃣ **사용자 관리**
- 사용자 생성, 수정, 조회까지 한 번에 OK!
- 이름과 프로바이더(`Google`, `kakao` 등)로 로그인.

### 3️⃣ **게시물 관리**
- 게시물 쓰고, 고치고, 삭제하고! 심지어 사용자별로 보기까지 가능.

### 4️⃣ **스토리 관리**
- 게시물에 스토리 추가, 삭제, 순서 변경도 지원.
- 다중 카테고리? 당연히 지원함! 🎯

---

## 🛠️ 기술 스택

- **Spring Boot** - 우리에게 빼놓을 수 없는 메인 프레임워크.
- **Spring Data JPA** - 복잡한 SQL은 가라!
- **Amazon S3** - 클라우드 업로드는 필수.
- **MySQL** - 데이터를 위한 믿음직한 친구.
- **Lombok** - 코드 짧아지고 손목은 편해짐..
- **SLF4J** - 로깅은 필수, 디버깅은 덤.

---
## 📂 디렉토리 구조 (이 정도면 깔끔한듯 솔직히)

- **mugejungsim_be/**
    - **controller/**
        - FileController.java
        - PostController.java
        - StoryController.java
        - UserController.java
    - **dto/**
        - PostDto.java
        - StoryDto.java
        - UserDto.java
    - **entity/**
        - Post.java
        - Story.java
        - User.java
    - **repository/**
        - PostRepository.java
        - StoryRepository.java
        - UserRepository.java
    - **service/**
        - FileStorageService.java
        - PostService.java
        - StoryService.java
        - UserService.java
    - CorsConfig.java
    - MugejungsimBeApplication.java
    - S3Config.java
    - S3Uploader.java
    - SecurityConfig.java
    - SwaggerConfig.java


## 🗂️ 디렉토리 설명
- **controller/**: RESTful API 엔드포인트를 정의한 컨트롤러들.
    - `FileController.java`: 파일 업로드 및 다운로드 관리.
    - `PostController.java`: 게시물 관리 API.
    - `StoryController.java`: 스토리 관리 API.
    - `UserController.java`: 사용자 관리 API.
- **dto/**: 데이터 전송 객체 정의.
    - `PostDto.java`: 게시물 데이터를 위한 DTO.
    - `StoryDto.java`: 스토리 데이터를 위한 DTO.
    - `UserDto.java`: 사용자 데이터를 위한 DTO.
- **entity/**: 데이터베이스 엔티티 정의.
    - `Post.java`: 게시물 엔티티.
    - `Story.java`: 스토리 엔티티.
    - `User.java`: 사용자 엔티티.
- **repository/**: 데이터베이스와의 인터페이스.
    - `PostRepository.java`: 게시물 관련 데이터 접근.
    - `StoryRepository.java`: 스토리 관련 데이터 접근.
    - `UserRepository.java`: 사용자 관련 데이터 접근.
- **service/**: 구체적인 로직을 처리하는 서비스 레이어.
    - `FileStorageService.java`: 파일 저장 서비스.
    - `PostService.java`: 게시물 관련 서비스.
    - `StoryService.java`: 스토리 관련 서비스.
    - `UserService.java`: 사용자 관련 서비스.
- **기타 주요 파일**:
    - `CorsConfig.java`: CORS 설정.
    - `MugejungsimBeApplication.java`: Spring Boot 메인 애플리케이션.
    - `S3Config.java`: Amazon S3 설정.
    - `S3Uploader.java`: S3 파일 업로드 유틸리티.
    - `SecurityConfig.java`: 보안 설정.
    - `SwaggerConfig.java`: Swagger 설정.
---
##📑 API 명세서

파일 업로드
| 메서드 | URL               | 설명                | 요청 데이터        | 응답 데이터          |
|--------|-------------------|---------------------|--------------------|----------------------|
| POST   | `/files/upload`   | 파일 업로드         | `MultipartFile`    | 업로드된 파일 URL    |
| GET    | `/files/{filename}` | 파일 다운로드      | `filename`         | 파일 리소스          |


사용자 관리
| 메서드 | URL               | 설명                | 요청 데이터              | 응답 데이터                   |
|--------|-------------------|---------------------|--------------------------|-------------------------------|
| POST   | `/api/users/save` | 사용자 저장 및 로그인 | `{name, provider}`       | `{userId, name, provider}`    |



게시물 관리
| 메서드 | URL                   | 설명                | 요청 데이터               | 응답 데이터                   |
|--------|-----------------------|---------------------|---------------------------|-------------------------------|
| POST   | `/api/posts`          | 게시물 생성         | `{userId, Post}`          | `{postId, pid, userId}`       |
| PUT    | `/api/posts/{postId}` | 게시물 수정         | `{userId, updatedPost}`   | 업데이트된 게시물              |
| DELETE | `/api/posts/{postId}` | 게시물 삭제         | `{userId}`                | -                             |



스토리 관리
| 메서드 | URL                      | 설명                | 요청 데이터                        | 응답 데이터               |
|--------|--------------------------|---------------------|------------------------------------|--------------------------|
| POST   | `/api/stories`           | 스토리 생성         | `{postId, content, categories...}` | 생성된 스토리 ID          |
| PUT    | `/api/stories/{storyId}` | 스토리 수정         | `{content, categories...}`         | 업데이트된 스토리 ID      |
| DELETE | `/api/stories/{storyId}` | 스토리 삭제         | `{postId}`                         | 성공 메시지               |

---

##📑 ERD구조


```plaintext
+------------------+          1        +------------------+
|      User        |<----------------->|      Post        |
+------------------+                   +------------------+
| id (PK)          |                   | id (PK)          |
| name             |                   | pid              |
| provider         |                   | title            |
|                  |                   | bottle           |
|                  |                   | startDate        |
|                  |                   | endDate          |
|                  |                   | location         |
|                  |                   | companion        |
|------------------|                   | user_id (FK)     |
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

