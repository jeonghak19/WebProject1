package com.example.team5_project.entity;

import com.example.team5_project.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    private Integer postLike;
    private Integer postDislike;
    private String imgPath;
}