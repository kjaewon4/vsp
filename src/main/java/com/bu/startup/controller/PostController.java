package com.bu.startup.controller;

import com.bu.startup.entity.Post;
import com.bu.startup.entity.User;
import com.bu.startup.service.CommentService;
import com.bu.startup.service.PostService;
import com.bu.startup.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestParam("assetBundleId") Long assetBundleId,
                                           @RequestParam("content") String content,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        postService.createOrUpdatePost(assetBundleId, content, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ModelAndView getPostDetail(@PathVariable Long postId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView mav = new ModelAndView("postDetail");
        Post post = postService.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        mav.addObject("post", post);
        mav.addObject("comments", commentService.getCommentsByPostId(postId));

        if (userDetails != null) {
            User currentUser = userService.getUserByUsername(userDetails.getUsername());
            mav.addObject("currentUser", currentUser);
        }
        return mav;
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        boolean isAdmin = currentUser.getProfile() != null && currentUser.getProfile().isAdmin();
        postService.deletePost(postId, currentUser, isAdmin);
        return ResponseEntity.ok().build();
    }
}
