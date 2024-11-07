package com.example.team5_project.entity;

import com.example.team5_project.baseEntity.baseEntity;
import com.example.team5_project.dto.PostResponseDto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="board_id", nullable=false)
    private Board board;


    private String postTitle;
    private String destcription;
    private Integer postLike;
    private Integer postDislike;
    private String imgPath;
    
    public PostResponseDto toDto() {
    	return PostResponseDto.builder()
    			.postId(postId)
    			.user(user)
    			.board(board)
    			.postTitle(postTitle)
    			.destcription(destcription)
    			.postLike(postLike)
    			.postDislike(postDislike)
    			.imgPath(imgPath)
    			.build();
    }    
    
}