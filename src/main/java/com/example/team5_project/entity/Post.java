package com.example.team5_project.entity;


import org.hibernate.annotations.ColumnDefault;

import com.example.team5_project.baseEntity.BaseEntity;
import com.example.team5_project.dto.PostResponseDto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="board_id", nullable=false)
    private Board board;

    @Column(name="post_title")
    private String postTitle;
    private String description;
//    private Integer postLike;
//    private Integer postDislike;
    private String imgName;
    private String imgPath;
    
    private String createdAt;
    private String updateAt;
    
    public PostResponseDto toDto() {
    	return PostResponseDto.builder()
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