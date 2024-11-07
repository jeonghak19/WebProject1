package com.example.team5_project.controller;

import com.example.team5_project.entity.Post;
import com.example.team5_project.service.BoardService;
import com.example.team5_project.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/home/posts")
@Controller
public class PostController {

    @Autowired private PostService postService;
    @Autowired private BoardService boardService;
    @Autowired private UserController userController;
    
  // 전체 게시글 리스트
  @GetMapping()
  public String posts(Model model, @RequestParam("boardId") Long boardId) {
      
	  model.addAttribute("posts", postService.findPostByBoardId(boardId));
	  model.addAttribute("boardTitle", boardService.getBoardTitle(boardId));
      model.addAttribute("boardId", boardId);
      
      return "home/posts";
  }
  
  	// 게시글 상세 페이지
    @GetMapping("/{postId}")
    public String post(@PathVariable("postId") Long postId, 
    				   @RequestParam("boardId") Long boardId,
    				   Model model) {
        model.addAttribute("post", postService.findPost(postId));
        model.addAttribute("boardId", boardId);

        return "home/post-details";
    }
    
    // 게시글 생성
    @PostMapping
    public String createPost(Post post, @RequestParam("boardId") Long boardId, RedirectAttributes redirect) {

        postService.createPost(post, boardId);
        redirect.addAttribute("boardId", boardId);
        
        return "redirect:/home/posts";
    }
    
    // 게시글 수정 페이지
    @GetMapping("/update/{postId}")
    public String updatePostPage(@PathVariable("postId") Long postId,
    						     @RequestParam(value ="boardId", required=false) Long boardId,
    						     Model model){

    	model.addAttribute("post", postService.findPost(postId));
    	model.addAttribute("boardId", boardId);

    	return "home/posts-update";
    }   
    
    
    // 게시글 수정
    @PostMapping("/update")
    public String updatePost(@RequestParam("postId") Long postId, 
    						 Post post, 
    						 @RequestParam("boardId") Long boardId,
    						 RedirectAttributes redirect) { 	

    	redirect.addAttribute("boardId", boardId);
    	postService.updatePost(post, boardId);
       
        return "redirect:/home/posts";
    }
    
    // 게시글 삭제
    @PostMapping("/delete")
    public String deletePost(@RequestParam("postId") Long postId, 
    			             @RequestParam("boardId") Long boardId,
    			             RedirectAttributes redirect) {
        
    	postService.deletePost(postId);
        redirect.addAttribute("boardId", boardId);

        return "redirect:/home/posts";
    }
}