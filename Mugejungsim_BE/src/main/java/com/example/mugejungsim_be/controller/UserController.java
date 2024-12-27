package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 사용자 등록 또는 조회
     *
     * @param email 사용자 이메일 (필수)
     * @param name  사용자 이름 (필수)
     * @return 등록된 사용자 정보 또는 기존 사용자 정보
     */
    @Operation(
            summary = "사용자 등록 또는 조회",
            description = "이메일을 기준으로 사용자를 등록하거나 이미 등록된 사용자의 정보를 반환합니다.",
            tags = { "사용자 관리" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자가 성공적으로 등록되었거나 조회됨", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/register")
    public ResponseEntity<User> registerOrGetUser(
            @RequestParam String email,
            @RequestParam String name) {
        User user = userService.registerOrGetUser(email, name);
        return ResponseEntity.ok(user);
    }

    /**
     * 특정 사용자 조회
     *
     * @param id 조회할 사용자의 고유 ID
     * @return 사용자 정보
     */
    @Operation(
            summary = "특정 사용자 조회",
            description = "사용자의 고유 ID를 통해 사용자 정보를 조회합니다.",
            tags = { "사용자 관리" }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자가 성공적으로 조회됨", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
