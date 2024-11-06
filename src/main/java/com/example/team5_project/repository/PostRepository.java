package com.example.team5_project.repository;

import com.example.team5_project.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    public Optional<Post> findById(Long id);
    public Post save(Post post, Long boardId);
    public void delete(Post post);
    public Optional<List<Post>> findByBoardId(Long Id);
    public Optional<List<Post>> findByUserId(Long Id);
    public Optional<List<Post>> findByTitle(String Title);
}
