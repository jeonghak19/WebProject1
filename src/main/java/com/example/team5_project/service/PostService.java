package com.example.team5_project.service;


import com.example.team5_project.entity.Post;
import com.example.team5_project.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostService {

    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    
    public Post findPost(Long postId){
        log.info("찾는 Id: {}", postId);
        return postRepository.findById(postId)
                .orElseThrow(()->new RuntimeException("해당 ID를 가진 게시물이 없습니다."));
    }

    public List<Post> findPostByBoardId(Long boardId){
        return postRepository.findByBoardId(boardId);

    }

    public List<Post> findUserPosts(Long userId){
        return postRepository.findByUserId(userId);

    }

    public List<Post> findSearchPost(String title){
        return postRepository.findByTitle(title);

    }

    public Post createPost(Post post, Long boardId){
        return postRepository.save(post, boardId);
    }

    public Post updatePost(Post post, Long boardId){
        Post findpost = postRepository.findById(post.getPostId())
                .orElseThrow(()->new RuntimeException());

        Optional.ofNullable(post.getPostTitle())
                .ifPresent(title->findpost.setPostTitle(title));
        Optional.ofNullable(post.getPostLike())
                .ifPresent(like->findpost.setPostLike(like));
        Optional.ofNullable(post.getPostDislike())
                .ifPresent(dislike->findpost.setPostDislike(dislike));
        Optional.ofNullable(post.getImgPath())
                .ifPresent(imgPath->findpost.setImgPath(imgPath));

        return postRepository.save(findpost, boardId);
    }

    public void deletePost(Long postId){
        Post post = findPost(postId);
        postRepository.delete(post);
    }   

}
