package com.example.team5_project.service;

import com.example.team5_project.entity.Comment;
import com.example.team5_project.repository.CommentRepository;
import com.example.team5_project.entity.Post;
import com.example.team5_project.entity.User;
import com.example.team5_project.repository.PostRepository;
import com.example.team5_project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment saveComment(Comment comment, Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        comment.setUser(user);  // 현재 사용자 설정
        comment.setPost(post);  // 해당 게시글 설정
        comment.setCommentTime(new Timestamp(System.currentTimeMillis()));

        return commentRepository.save(comment);
    }

    public List<Comment> findCommentsByPostId(Long postId) {
        return commentRepository.findByPostPostIdOrderByCommentTimeDesc(postId);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
