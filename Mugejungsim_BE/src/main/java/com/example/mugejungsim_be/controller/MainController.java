package com.example.mugejungsim_be.controller;

import com.example.mugejungsim_be.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "Main Page", description = "Main page after user login")
public class MainController {

    @Operation(summary = "Main Page", description = "Displays the main page for authenticated users.")
    @GetMapping("/main")
    public String showMainPage(@AuthenticationPrincipal CustomOAuth2User customUser, Model model) {
        if (customUser != null) {
            model.addAttribute("name", customUser.getName());
            model.addAttribute("email", customUser.getEmail());
            model.addAttribute("picture", customUser.getAttributes().get("picture")); // Add user picture
        } else {
            return "redirect:/login"; // Redirect to login if not authenticated
        }
        return "main"; // Returns the main.html page
    }

    @Operation(summary = "Create Post Page", description = "Navigate to the post creation page.")
    @GetMapping("/create-post")
    public String showCreatePostPage() {
        return "create_post"; // Navigate to the post creation page
    }

    @Operation(summary = "View Posts Page", description = "Navigate to the page showing all user posts.")
    @GetMapping("/view-posts")
    public String showViewPostsPage() {
        return "view_posts"; // Navigate to the view posts page
    }
}
