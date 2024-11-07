package com.example.team5_project.dto;

import com.example.team5_project.entity.Board;
import com.example.team5_project.entity.Post;
import com.example.team5_project.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long postId;
    
    private User user;
    private Board board;

    private String postTitle;
    private String destcription;
    private Integer postLike;
    private Integer postDislike;
    private String imgPath;
    

}
