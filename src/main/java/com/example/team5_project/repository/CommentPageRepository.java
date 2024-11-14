package com.example.team5_project.repository;

import com.example.team5_project.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentPageRepository extends JpaRepository<Comment, Long> {
    // 'postId'로 댓글을 조회할 수 있도록
    Page<Comment> findByPost_PostId(Long postId, Pageable pageable);
    Page<Comment> findByUser_UserId(Long userId, Pageable pageable);
}