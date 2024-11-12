package com.example.team5_project.repository;

import com.example.team5_project.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    public Optional<Post> findById(Long id);
    public Post save(Post post, Long boardId);
    public void delete(Post post);
    /*public List<Post> findByBoardId(Long Id);*/
    public List<Post> findByUserId(Long Id);
    /*public List<Post> findByTitle(String Title,Long boardId);*/
    public void increasePostViewCount(Long postId);
    public void updateCount(Post post, boolean liked);
}