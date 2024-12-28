package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * MainController
 *
 * 사용자 인증 후 표시되는 메인 페이지 및 추가 페이지를 처리하는 컨트롤러입니다.
 */
@Controller
@Tag(name = "Main Page", description = "Main page after user login")
public class MainController {

    /**
     * 인증된 사용자에게 메인 페이지를 보여줍니다.
     *
     * @param customUser 인증된 사용자 정보
     * @param model      뷰로 전달할 데이터
     * @return 메인 페이지 경로
     */
    @Operation(summary = "Main Page", description = "Displays the main page for authenticated users.")
    @GetMapping("/main")
    public String showMainPage(@AuthenticationPrincipal CustomOAuth2User customUser, Model model) {
        if (customUser != null) {
            model.addAttribute("name", customUser.getName());
            model.addAttribute("picture", customUser.getAttributes().get("picture")); // 사용자 이미지 추가
        } else {
            return "redirect:/login"; // 인증되지 않은 경우 로그인 페이지로 리다이렉트
        }
        return "main"; // main.html 페이지 반환
    }

    /**
     * 게시물 작성 페이지로 이동합니다.
     *
     * @return 게시물 작성 페이지 경로
     */
    @Operation(summary = "Create Post Page", description = "Navigate to the post creation page.")
    @GetMapping("/create-post")
    public String showCreatePostPage() {
        return "create_post"; // create_post.html 페이지로 이동
    }

    /**
     * 게시물 보기 페이지로 이동합니다.
     *
     * @return 게시물 보기 페이지 경로
     */
    @Operation(summary = "View Posts Page", description = "Navigate to the page showing all user posts.")
    @GetMapping("/view-posts")
    public String showViewPostsPage() {
        return "view_posts"; // view_posts.html 페이지로 이동
    }
}
