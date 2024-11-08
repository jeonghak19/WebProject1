package com.example.team5_project.repository;

import com.example.team5_project.entity.Comment;

import java.util.List;
import java.util.Optional;

import com.example.team5_project.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CommentRepository{
    public Optional<Comment> findById(Long id);
    public Comment save(Comment comment);
    public void delete(Comment comment);
    public List<Comment> findByPostId(Long Id);
    public List<Comment> findByUserId(Long Id);
}


