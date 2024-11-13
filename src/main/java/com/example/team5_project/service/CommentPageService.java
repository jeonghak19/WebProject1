package com.example.team5_project.service;

import com.example.team5_project.entity.Comment;
import com.example.team5_project.repository.CommentPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CommentPageService {
    private final CommentPageRepository commentPageRepository;

    @Autowired
    public CommentPageService(CommentPageRepository commentPageRepository) {
        this.commentPageRepository = commentPageRepository;
    }

    // 댓글을 페이지네이션 처리하여 반환, 최신순과 과거순 정렬을 선택할 수 있음
    public Page<Comment> getCommentsByPostId(Long postId, int page, int size, String sortOrder) {
        Sort sort = Sort.by(Sort.Order.desc("commentTime")); // 기본은 최신순
        if ("asc".equalsIgnoreCase(sortOrder)) {
            sort = Sort.by(Sort.Order.asc("commentTime")); // 과거순
        }
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return commentPageRepository.findByPost_PostId(postId, pageable);
    }

    public Page<Comment> getCommentsByUserId(Long userId, Pageable pageable) {
        return commentPageRepository.findByUser_UserId(userId, pageable);
    }
}