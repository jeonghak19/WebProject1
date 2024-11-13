package com.example.team5_project.dto;

import com.example.team5_project.entity.Board;
import com.example.team5_project.entity.Post;
import com.example.team5_project.entity.User;

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
//    private Integer postLike;
//    private Integer postDislike;
    private String imgName;
    private String imgPath;
    
    public Post toPost() {
    	return Post.builder()
    			.postId(postId)
    			.user(user)
    			.board(board)
    			.postTitle(postTitle)
    			.description(description)
//    			.postLike(postLike)
//    			.postDislike(postDislike)
    			.imgName(imgName)
    			.imgPath(imgPath)
    			.build();
    }
}
