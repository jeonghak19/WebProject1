package com.example.team5_project.repository;

import com.example.team5_project.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    public Optional<Post> findById(long id);
    public List<Post> findAll();
    public Post save(Post post);
    public void delete(Post post);
    public List<Post> findByBoardId(long Id);
    public List<Post> findByUserId(long Id);
    public List<Post> findByTitle(String Title);
}
