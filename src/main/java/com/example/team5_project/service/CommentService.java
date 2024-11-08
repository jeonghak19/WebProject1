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
import java.util.Optional;

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

    public Comment updateComment(Comment comment) {
        Comment findcomment = commentRepository.findById(comment.getCommentId())
                .orElseThrow(()->new RuntimeException());

        Optional.ofNullable(comment.getContent())
                .ifPresent(content->findcomment.setContent(content));
        Optional.ofNullable(comment.getCommentTime())
                .ifPresent(time->findcomment.setCommentTime(time));
        findcomment.setCommentTime(new Timestamp(System.currentTimeMillis()));
        return commentRepository.save(findcomment);
    }

    public List<Comment> findCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public void deleteComment(Long commentId) {
        commentRepository.delete(commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found")));
    }

    public List<Comment> findCommentsByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }
    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

}
