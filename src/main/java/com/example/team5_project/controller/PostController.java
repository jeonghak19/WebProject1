package com.example.team5_project.controller;

import com.example.team5_project.entity.Comment;
import com.example.team5_project.entity.Post;
import com.example.team5_project.service.BoardService;
import com.example.team5_project.service.CommentService;
import com.example.team5_project.service.PostService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/home")
@Controller
public class PostController {
    @Autowired private BoardService boardService;
    @Autowired private PostService postService;
    @Autowired private CommentService commentService;
    
    @GetMapping("/posts")
    public String posts(@RequestParam("boardId") Long boardId, Model model) {
        model.addAttribute("boardTitle",boardService.findBoard(boardId).getBoardTitle());
        model.addAttribute("boardId",boardId);
        model.addAttribute("posts",postService.findPostByBoardId(boardId));
        return "home/posts";
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable("postId") Long postId, @RequestParam("boardId") Long boardId, Model model) {
        // Post 조회
        Post post = postService.findPost(postId);
        // Post에 속한 댓글 조회
        List<Comment> comments = commentService.getCommentsByPost(postId);
        
        model.addAttribute("post", post);         // Post 정보
        model.addAttribute("comments", comments); // 댓글 리스트
        model.addAttribute("boardId", boardId);   // boardId 전달

        return "home/post_details";  // 댓글 포함된 게시글 상세 페이지로 이동
    }

    @PostMapping("/create")
    public String createPost(@RequestParam("boardId") Long boardId, @ModelAttribute Post post) {
        post.setBoard(boardService.findBoard(boardId));
        postService.createPost(post);
        return "redirect:/home/posts";
    }

    @GetMapping("/create")
    public String createPost() {
        return "home/create";
    }

    @PostMapping("/posts/{id}/edit")
    public String updatePost(@PathVariable("id") Long id, @ModelAttribute Post post) {
        post.setPostId(id);
        postService.updatePost(post);

        return "redirect:/home/posts";
    }

    @PostMapping("/posts/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return "redirect:/home/posts";
    }
}

