package com.example.team5_project.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

import com.example.team5_project.baseEntity.BaseEntity;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CommentId;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=false)
    private Post post;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    private String content;
    private Timestamp commentTime;
    
    public Comment(Post post, User user, String content, Timestamp commentTime) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.commentTime = commentTime;
    }

}
