package com.bu.startup.repo;

import com.bu.startup.entity.Comment;
import com.bu.startup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_PostIdOrderByCreatedAtAsc(Long postId);
    List<Comment> findByAuthor(User author);
}
