package com.example.team5_project.dto;

import com.example.team5_project.entity.Board;
import com.example.team5_project.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {

    private Long postId;
    
    private User user;
    private Board board;

    private String postTitle;
    private String description;
    private String imgName;
    private String imgPath;
    
}
