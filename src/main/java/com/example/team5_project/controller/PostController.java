package com.example.team5_project.controller;

import com.example.team5_project.dto.PostDto;
import com.example.team5_project.entity.Post;
import com.example.team5_project.service.BoardService;
import com.example.team5_project.service.PostService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
    
    // 게시글 생성 페이지
    @GetMapping("/create")
    public String createPostPage(Model model, Long boardId) {
    	model.addAttribute("postDto", new PostDto());
        model.addAttribute("boardId", boardId);

        return "home/posts-create";
    }
    
    // 게시글 생성 페이지
    @PostMapping("/create")
    public String createPost(PostDto postDto, 
    						 Long boardId, 
    						 @RequestParam("file") MultipartFile file,
    						 Model model, 
    						 RedirectAttributes redirect) throws IOException {

    	Post post = postDto.toPost();    	
    	Post newPost = postService.createPost(post, boardId,file);
    	
    	redirect.addAttribute("boardId", boardId);
    	
    	if(newPost == null) {
    		return "redirect:/home/posts/create";
    	}        
        return "redirect:/home/posts";
    }
    
    
    // 게시글 수정 페이지
    @GetMapping("/update")
	public String updatePostPage(Long boardId, Long postId, Model model) {

    	model.addAttribute("post", postService.findPost(postId));
    	model.addAttribute("boardId", boardId);

    	return "home/posts-update";
    }   
    
    
    // 게시글 수정
    @PostMapping("/update")
    public String updatePost(Long postId, Long boardId, 
    						 Post post, 
    						 RedirectAttributes redirect, 
    						 @RequestParam("file") MultipartFile file) throws IOException { 	
    	String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
    	
    	UUID uuid = UUID.randomUUID();
    	String fileName = uuid + "_" + file.getOriginalFilename();
    	
    	File saveFile = new File(projectPath, fileName);
    	file.transferTo(saveFile);
    	
    	post.setImgName(fileName);
    	post.setImgPath("/files/" + fileName);
    	
    	
    	redirect.addAttribute("boardId", boardId);
    	postService.updatePost(post, boardId);
       
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