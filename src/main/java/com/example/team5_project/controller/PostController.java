package com.example.team5_project.controller;

import com.example.team5_project.dto.PostDto;
import com.example.team5_project.entity.Post;
import com.example.team5_project.service.BoardService;
import com.example.team5_project.service.CommentService;
import com.example.team5_project.service.PostService;
import com.example.team5_project.service.UserService;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@RequestMapping("/home/posts")
@Controller
public class PostController {

    @Autowired private PostService postService;
    @Autowired private BoardService boardService;
    @Autowired private UserService userService;
    @Autowired private CommentService commentService;
    
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
        model.addAttribute("comments", commentService.findCommentsByPostId(postId));
        
        String imgName = null;
        if (postService.findPost(postId).getImgName() != null && !postService.findPost(postId).getImgName().isEmpty()) {
  
            String[] imgNameParts = postService.findPost(postId).getImgName().split("_");
            imgName = imgNameParts.length > 1 ? imgNameParts[1] : null;
            model.addAttribute("imgName", imgName);
        }
        
        return "home/post-details";
    }
    
    // 게시글 생성 페이지
    @GetMapping("/create")
    public String createPostPage(Model model, Long boardId, @RequestParam("userId") Long userId, Post post) {
    	model.addAttribute("postDto", new PostDto());
        model.addAttribute("boardId", boardId);
        
        post.setUser(userService.findUserByUserId(userId));
        
        return "home/posts-create";
    }
    
    // 게시글 생성
    @PostMapping("/create")
    public String createPost(PostDto postDto, 
    						 Long boardId, 
    						 MultipartFile file,
    						 @RequestParam("userId") Long userId,
    						 Model model, 
    						 RedirectAttributes redirect) throws IOException {
    	
    	Post post = postDto.toPost();    	
    	post.setUser(userService.findUserByUserId(userId));
    	
    	if(file == null) {
    		Post newPost = postService.createPost(post, boardId);
    		
        	if(newPost == null) {
        		return "redirect:/home/posts/create";
        	}   
    	} else {
    		Post newPost = postService.createPostWithFile(post, boardId,file);
    		
        	if(newPost == null) {
        		return "redirect:/home/posts/create";
        	}     
    	}
    	
    	redirect.addAttribute("boardId", boardId);
  
        return "redirect:/home/posts";
    }

    
    
    // 게시글 수정 페이지
    @GetMapping("/update")
	public String updatePostPage(Long boardId, Long postId, Model model) {

    	model.addAttribute("post", postService.findPost(postId));
    	model.addAttribute("boardId", boardId);
    	
        String imgName = null;
        if (postService.findPost(postId).getImgName() != null && !postService.findPost(postId).getImgName().isEmpty()) {
  
            String[] imgNameParts = postService.findPost(postId).getImgName().split("_");
            imgName = imgNameParts.length > 1 ? imgNameParts[1] : null;
            model.addAttribute("imgName", imgName);
        }
    	

    	return "home/posts-update";
    }   
    
    
    // 게시글 수정
    @PostMapping("/update")
    public String updatePost(Long postId, Long boardId, 
    						 Post post, 
    						 RedirectAttributes redirect, 
    						 MultipartFile file) throws IOException { 	

    	if(file == null) {
    		postService.updatePost(post, boardId);
        	redirect.addAttribute("boardId", boardId);
    	} else {
    		postService.uploadFile(post, file);
    		
    		postService.updatePost(post, boardId);
        	redirect.addAttribute("boardId", boardId);
    	}
    	
       
        return "redirect:/home/posts";
    }
    
    // 게시글 삭제
    @PostMapping("/delete")
    public String deletePost(Long postId, Long boardId, RedirectAttributes redirect) {
        
    	postService.deletePost(postId);
        redirect.addAttribute("boardId", boardId);

        return "redirect:/home/posts";
    }
}
