package com.example.team5_project.repository;

import com.example.team5_project.entity.Comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostPostIdOrderByCommentTimeDesc(Long postId);  // postId로 댓글 조회
}
