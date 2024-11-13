package com.example.team5_project.service;


//import com.example.team5_project.entity.FileEntity;
import com.example.team5_project.entity.Post;
//import com.example.team5_project.repository.FileEntityRepository;
import com.example.team5_project.repository.PostRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.extern.slf4j.Slf4j;

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

    private final PostRepository postRepository;

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
    
    
    // 조회수 증가
    @Transactional
    public void increaseViewCount(Long postId) {
    	Post post = postRepository.findById(postId).get();
    	post.setViewCount(post.getViewCount()+1);

    }
    
    
    public Post createPost(Post post, Long boardId) {
    	return postRepository.save(post, boardId);
    }
    
    
    // 파일 업로드 관련
    public void uploadFile(Post post, MultipartFile file) throws IOException {
    	// 파일 경로 설정
    	String projectPath = "C:/Prj1/Files/";
    	
    	// 서버 저장용 파일 이름 설정
    	UUID uuid = UUID.randomUUID();
    	String fileName = uuid + "_" + file.getOriginalFilename();
    	
    	// 해당 경로에 파일 저장
    	File saveFile = new File(projectPath, fileName);
    	file.transferTo(saveFile);
    	
    	// 파일 이름, 경로 저장
    	post.setImgName(fileName);
    	post.setImgPath("/files/" + fileName);
    }
    
    
    // 파일 원래 이름 조회
    public String getOriginalFileName(Long postId) {
        String originalFileName = null;
        if (findPost(postId).getImgName() != null && !findPost(postId).getImgName().isEmpty()) {
  
            String[] imgNameParts = findPost(postId).getImgName().split("_");
            originalFileName = imgNameParts.length > 1 ? imgNameParts[1] : null;
        }            
            return originalFileName;
    }    
    
    @Transactional
    public Post updatePost(Post post, Long boardId) {
        Post findpost = postRepository.findById(post.getPostId())
                .orElseThrow(()->new RuntimeException());

        Optional.ofNullable(post.getPostTitle())
        		.ifPresent(title->findpost.setPostTitle(title));
        Optional.ofNullable(post.getDescription())
        		.ifPresent(description ->findpost.setDescription(description));
        Optional.ofNullable(post.getImgName())
               	.ifPresent(imgName->findpost.setImgName(imgName));
        Optional.ofNullable(post.getImgPath())
                .ifPresent(imgPath->findpost.setImgPath(imgPath));
        Optional.ofNullable(post.getLikeCount())
          		.ifPresent(likeCount -> findpost.setLikeCount(likeCount));

        return postRepository.save(findpost, boardId);
    }

    
    public void deletePost(Long postId){
        Post post = findPost(postId);
        postRepository.delete(post);
    } 
    
    @Transactional
    public void visitPost(Long postId){
        postRepository.increasePostViewCount(postId);
    }

    public void updateCount(Post post, boolean liked) {
    	postRepository.updateCount(post, liked);
    	post.updateCount(liked);
    }
}
