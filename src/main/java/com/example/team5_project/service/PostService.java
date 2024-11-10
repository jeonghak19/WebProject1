package com.example.team5_project.service;


import com.example.team5_project.entity.Post;
import com.example.team5_project.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PostService {

    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public Post findPost(Long postId){

        return postRepository.findById(postId)
                .orElseThrow(()->new RuntimeException("해당 ID를 가진 게시물이 없습니다."));
    }

    /*public List<Post> findPostByBoardId(Long boardId){
        return postRepository.findByBoardId(boardId);

    }*/

    public List<Post> findUserPosts(Long userId){
        return postRepository.findByUserId(userId);

    }

    /*public List<Post> findSearchPost(String title,Long boardId){
        return postRepository.findByTitle(title,boardId);

    }*/

    public Post createPost(Post post, Long boardId) {
    	return postRepository.save(post, boardId);
    }
    
    public void uploadFile(Post post, MultipartFile file) throws IOException {
    	String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
    	
    	UUID uuid = UUID.randomUUID();
    	String fileName = uuid + "_" + file.getOriginalFilename();
    	
    	File saveFile = new File(projectPath, fileName);
    	file.transferTo(saveFile);
    	
    	post.setImgName(fileName);
    	post.setImgPath("/files/" + fileName);
    }
    
    
    public Post createPostWithFile(Post post, Long boardId, MultipartFile file) throws IOException{
    	uploadFile(post, file);
    	
    	return postRepository.save(post, boardId);
    }

    public Post updatePost(Post post, Long boardId) {
        Post findpost = postRepository.findById(post.getPostId())
                .orElseThrow(()->new RuntimeException());

        Optional.ofNullable(post.getPostTitle())
                .ifPresent(title->findpost.setPostTitle(title));
        Optional.ofNullable(post.getDescription())
        		.ifPresent(description ->findpost.setDescription(description));
//        Optional.ofNullable(post.getPostLike())
//                .ifPresent(like->findpost.setPostLike(like));
//        Optional.ofNullable(post.getPostDislike())
//                .ifPresent(dislike->findpost.setPostDislike(dislike));
          Optional.ofNullable(post.getImgName())
                   .ifPresent(imgName->findpost.setImgName(imgName));
          Optional.ofNullable(post.getImgPath())
                .ifPresent(imgPath->findpost.setImgPath(imgPath));

        return postRepository.save(findpost, boardId);
    }

    public void deletePost(Long postId){
        Post post = findPost(postId);
        postRepository.delete(post);
    }   

}


