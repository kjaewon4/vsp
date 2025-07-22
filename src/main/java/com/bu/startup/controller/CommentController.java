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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;

    @PostMapping("/posts/{postId}")
    public RedirectView addComment(@PathVariable Long postId,
                                           @RequestParam("content") String content,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            // 인증되지 않은 사용자 처리
            return new RedirectView("/login");
        }
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (currentUser == null) {
            // 사용자 정보를 찾을 수 없는 경우
            return new RedirectView("/login");
        }
        commentService.createComment(postId, content, currentUser);
        return new RedirectView("/posts/" + postId);
    }

    @PostMapping("/{commentId}/delete")
    public RedirectView deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new RedirectView("/login");
        }
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (currentUser == null) {
            return new RedirectView("/login");
        }
        
        Long postId = commentService.getCommentById(commentId).getPost().getPostId(); // 댓글이 속한 게시글 ID 가져오기

        boolean isAdmin = currentUser.getProfile() != null && currentUser.getProfile().isAdmin();
        commentService.deleteComment(commentId, currentUser, isAdmin);
        return new RedirectView("/posts/" + postId);
    }
}

