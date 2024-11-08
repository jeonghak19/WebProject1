package com.example.team5_project.controller;


import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.team5_project.entity.Comment;
import com.example.team5_project.entity.Post;
import com.example.team5_project.entity.User;
import com.example.team5_project.service.CommentService;
import com.example.team5_project.service.PostService;
import com.example.team5_project.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/home/posts")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    private final HttpSession session;

    @Autowired
    public CommentController(CommentService commentService, PostService postService, UserService userService, HttpSession session) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
        this.session = session;
    }

    @PostMapping("/{postId}/comments")
    public String saveComment(@PathVariable("postId") Long postId,
                              @RequestParam("content") String content,
                              @RequestParam("boardId") Long boardId,  // 수정된 부분
                              RedirectAttributes redirectAttributes) {
        // 임시로 userId를 1로 설정
        Long userId = 1L;

        // User 객체 조회
        User user = userService.findUserByUserId(userId);

        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
            return "redirect:/home/posts/" + postId + "?boardId=" + boardId;  // 수정된 부분
        }

        // Post 객체 조회
        Post post = postService.findPost(postId);

        // Comment 객체 생성 및 설정
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCommentTime(new Timestamp(System.currentTimeMillis()));

        // 댓글 저장
        commentService.saveComment(comment, userId, postId);  // 수정된 부분

        redirectAttributes.addFlashAttribute("message", "댓글이 작성되었습니다.");

        return "redirect:/home/posts/" + postId + "?boardId=" + boardId;  // 수정된 부분
    }
}


