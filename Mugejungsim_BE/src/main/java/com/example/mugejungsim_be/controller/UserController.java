package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.entity.User;
import com.example.mugejungsim_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 *
 * 사용자 정보를 관리하기 위한 REST API를 제공합니다.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "API for managing user information")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 사용자 정보를 저장하거나 업데이트합니다.
     *
     * @param user 저장할 사용자 데이터
     * @return 저장된 사용자 정보
     */
    @PostMapping("/save")
    @Operation(
            summary = "Save or Update User",
            description = "Saves a new user or updates an existing user's information.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User object to be saved or updated",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"id\": 1, \"name\": \"John Doe\", \"profileImageUrl\": \"https://example.com/profile.jpg\", \"provider\": \"google\" }"
                            )
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User saved or updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    )
            }
    )
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = userService.saveOrUpdateUser(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * ID로 사용자 정보를 조회합니다.
     *
     * @param id 사용자 고유 ID
     * @return 조회된 사용자 정보
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get User by ID",
            description = "Retrieves a user by their unique ID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "User retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class),
                                    examples = @ExampleObject(
                                            value = "{ \"id\": 1, \"name\": \"John Doe\", \"profileImageUrl\": \"https://example.com/profile.jpg\", \"provider\": \"google\" }"
                                    )
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    )
            }
    )
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
