package com.example.team5_project.controller;

import com.example.team5_project.entity.Post;
import com.example.team5_project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/home/posts")
@Controller
public class PostController {

    @Autowired private PostService postService;
    
  
  @GetMapping()
  public String posts(Model model, @RequestParam Long boardId, @RequestParam String boardTitle) {
      model.addAttribute("posts", postService.findPostByBoardId(boardId));
      model.addAttribute("boardId", boardId);
      model.addAttribute("boardTitle", boardTitle);
      System.out.println("보드 이름" + boardTitle);
      return "home/posts";
  }
  

    @GetMapping("/{postId}")
    public String post(@PathVariable("postId") Long postId, @RequestParam Long boardId, Model model) {
        model.addAttribute("post",postService.findPost(postId));
        model.addAttribute("boardId", boardId);

        return "home/post-details";
    }

    @PostMapping
    public String createPost(
            @RequestParam("postTitle") String postTitle,
            @RequestParam("postLike") Integer postLike,
            @RequestParam("postDislike") Integer postDislike,
            @RequestParam("imgPath") String imgPath, 
    		@RequestParam Long boardId,										
    		RedirectAttributes redirect) {

        Post newPost = new Post();
        newPost.setPostTitle(postTitle);
        newPost.setPostLike(postLike);
        newPost.setPostDislike(postDislike);
        newPost.setImgPath(imgPath);

        postService.createPost(newPost, boardId);
        redirect.addAttribute("boardId", boardId);
        
        return "redirect:/home/posts";
    }

    @PatchMapping("/{id}")
    public String updatePost(@PathVariable("id") Long id, @RequestBody Post post, @RequestParam Long boardId) {
        postService.updatePost(post, boardId);
        return "redirect:/home/posts";
    }

    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId, @RequestParam Long boardId, RedirectAttributes redirect) {
        postService.deletePost(postId);
        redirect.addAttribute("boardId", boardId);
        return "redirect:/home/posts";
    }
}