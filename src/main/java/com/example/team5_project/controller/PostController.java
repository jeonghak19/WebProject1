package com.example.team5_project.controller;

import com.example.team5_project.dto.PostDto;
import com.example.team5_project.entity.Comment;
import com.example.team5_project.entity.Post;
import com.example.team5_project.service.BoardService;
import com.example.team5_project.service.CommentPageService;
import com.example.team5_project.service.CommentService;
import com.example.team5_project.service.PostService;
import com.example.team5_project.service.UserService;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    @Autowired private UserService userService;
    @Autowired private CommentService commentService;
    @Autowired private LikesService likesService;
  
  	// 게시글 상세 페이지
    @GetMapping("/{postId}")
    public String post(@PathVariable("postId") Long postId, 
    				   @RequestParam("boardId") Long boardId,
    				   Model model,  
    				   HttpSession session) {
        Post post = postService.findPost(postId);
        postService.increaseViewCount(postId);
    	
        model.addAttribute("post", post);
        model.addAttribute("boardId", boardId);
        model.addAttribute("comments", commentService.findCommentsByPostId(postId));
        model.addAttribute("imgName", postService.getOriginalFileName(postId));
        
        User user = (User)session.getAttribute("user");
        boolean liked = false;
        
        if(user != null) {
        	liked = likesService.getLike(postId, user.getUserId());
        }
        model.addAttribute("liked", liked);
        model.addAttribute("likeCount", post.getLikeCount());
        
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
    // 파일 첨부 여부에 따라 로직이 다름.
    @PostMapping("/create")
    public String createPost(PostDto postDto, 
    						 Long boardId, 
    						 MultipartFile file,
    						 @RequestParam("userId") Long userId,
    						 Model model, 
    						 RedirectAttributes redirect) throws IOException {
    	System.out.println(file.getOriginalFilename());
		if(postDto == null) {
			return "redirect:/home/posts/create";
		}
    	
    	Post post = postDto.toPost();    	
    	post.setUser(userService.findUserByUserId(userId));
    	    	
    	if(file != null && !file.isEmpty()) {
    		postService.uploadFile(post, file);
		} 
    	postService.createPost(post, boardId);
    	
    	redirect.addAttribute("boardId", boardId);
        return "redirect:/home/posts/search";
    }
    
    // 게시글 수정 페이지
    @GetMapping("/update")
	public String updatePostPage(Long boardId, Long postId, Model model) {

    	model.addAttribute("post", postService.findPost(postId));
    	model.addAttribute("boardId", boardId);
        model.addAttribute("originalFileName", postService.getOriginalFileName(postId));

    	return "home/posts-update";
    }   
    
    // 게시글 수정
    @PostMapping("/update")
    public String updatePost(Long postId, Long boardId, 
    						 Post post,
    						 String imgPath,
    						 String imgName,
    						 RedirectAttributes redirect, 
    						 MultipartFile file) throws IOException { 	

    	if(file == null || file.isEmpty()) {
    		if(imgPath != null || imgName != null) {
    			post.setImgPath(imgPath);
    			post.setImgName(imgName);
    		}
    		postService.updatePost(post, boardId);
    	} else {
    		postService.uploadFile(post, file);    		
    		postService.updatePost(post, boardId);
    	}
    	redirect.addAttribute("boardId", boardId);
    	redirect.addAttribute("postId", postId);

		return "redirect:/home/posts/{postId}";
    }
    
    // 게시글 삭제
    @PostMapping("/delete")
    public String deletePost(Long postId, Long boardId, RedirectAttributes redirect) {
        
    	postService.deletePost(postId);
        redirect.addAttribute("boardId", boardId);

        return "redirect:/home/posts/search";
    }
}