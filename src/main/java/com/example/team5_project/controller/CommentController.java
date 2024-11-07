package com.example.team5_project.controller;

import com.example.team5_project.entity.Comment;
import com.example.team5_project.entity.Post;
import com.example.team5_project.entity.User;
import com.example.team5_project.service.CommentService;
import com.example.team5_project.service.PostService;  // PostService 추가
import com.example.team5_project.service.UserService;  // UserService 추가
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/home/posts")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/{postId}/comments")
    public String getComments(@PathVariable("postId") Long postId, Model model) {
        Post post = postService.findPost(postId);
        List<Comment> comments = commentService.getCommentsByPost(postId);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "home/post_details"; // 수정된 템플릿 이름으로 변경
    }

    @PostMapping("/{postId}/comments")
    public String saveComment(@PathVariable("postId") Long postId,
                              @RequestParam("content") String content,
                              @RequestParam("userId") Long userId,
                              @RequestParam(value = "boardId", required = false) Long boardId,
                              RedirectAttributes redirectAttributes) {
        Post post = postService.findPost(postId);
        User user = userService.findUserByUserId(userId);

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCommentTime(new Timestamp(System.currentTimeMillis()));
        commentService.saveComment(comment);

        redirectAttributes.addFlashAttribute("comments", commentService.getCommentsByPost(postId));

        // 리다이렉트 후 적절한 경로로 이동
        if (boardId == null) {
            return "redirect:/home/posts/" + postId + "/comments";
        } else {
            return "redirect:/home/posts/" + postId + "?boardId=" + boardId;
        }
    }
}
