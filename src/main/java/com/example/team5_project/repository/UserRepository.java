package com.example.team5_project.repository;

import com.example.team5_project.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public Optional<User> findById(Long id);
    public Optional<User> findByName(String name);
    public List<User> findAll();
    public User save(User user);
    public void delete(User user);
    public Optional<User> findbyCommentId(Long id);
    public Optional<User> findbyPostId(Long id);
}

