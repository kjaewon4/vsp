package com.bu.startup.service;

import com.bu.startup.entity.Comment;
import com.bu.startup.entity.Post;
import com.bu.startup.entity.User;
import com.bu.startup.repo.CommentRepository;
import com.bu.startup.repo.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment createComment(Long postId, String content, User author) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Comment comment = Comment.builder()
                .post(post)
                .author(author)
                .content(content)
                .build();
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPost_PostIdOrderByCreatedAtAsc(postId);
    }

    @Transactional(readOnly = true)
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
    }

    @Transactional
    public void deleteComment(Long commentId, User currentUser, boolean isAdmin) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (isAdmin || comment.getAuthor().getUserId().equals(currentUser.getUserId())) {
            commentRepository.delete(comment);
        } else {
            throw new IllegalArgumentException("You are not authorized to delete this comment.");
        }
    }
}
