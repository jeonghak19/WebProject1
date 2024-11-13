package com.example.team5_project.dto;

import org.springframework.web.multipart.MultipartFile;

import com.example.team5_project.entity.Board;
import com.example.team5_project.entity.Post;
import com.example.team5_project.entity.User;
import com.example.team5_project.validator.ValidFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long postId;
    
    private User user;
    private Board board;

    private String postTitle;
    private String description;
    

    @ValidFile(message = "파일은 5MB이하, 파일 형식은 jpg, png만 가능합니다.")
    private MultipartFile file;
    
    private String imgName;
    private String imgPath;
    
    public Post toPost() {
    	return Post.builder()
    			.postId(postId)
    			.user(user)
    			.board(board)
    			.postTitle(postTitle)
    			.description(description.replace("\r\n", "<br>"))  // 개행 처리를 추가
    			.imgName(imgName)
    			.imgPath(imgPath)
    			.build();
    }
}
