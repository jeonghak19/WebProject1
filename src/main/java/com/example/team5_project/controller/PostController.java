package com.example.team5_project.controller;

import com.example.team5_project.dto.PostDto;
import com.example.team5_project.entity.Comment;
import com.example.team5_project.entity.Post;
import com.example.team5_project.entity.User;
import com.example.team5_project.service.CommentPageService;
import com.example.team5_project.service.PostService;
import com.example.team5_project.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;


@RequestMapping("/post/posts")
@Controller
@Slf4j
public class PostController {

    @Autowired private PostService postService;
    @Autowired private UserService userService;
    @Autowired private CommentPageService commentPageService;
    
    private final Map<Long, Set<Long>> likeMap = new HashMap<>();
    private final Map<Long, Set<Long>> userViewRecords = new ConcurrentHashMap<>();
    
  	// 게시글 상세 페이지
    @GetMapping("/{postId}")
    public String post(@PathVariable("postId") Long postId, 
    				   @RequestParam("boardId") Long boardId,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder,
    				   Model model,  
    				   HttpSession session) {

        Post post = postService.findPost(postId);
        postService.increaseViewCount(postId);
    	
        User user = (User)session.getAttribute("user");
        boolean liked = false;
        
        if(user != null) {
   		
            // 사용자 ID를 키로 하는 조회 기록을 가져옴 - 존재하지 않을 경우 추가
            Set<Long> viewedPosts = userViewRecords.computeIfAbsent(user.getUserId(), k -> new HashSet<>());        	
        	Set<Long> postIdSet = likeMap.computeIfAbsent(user.getUserId(), k -> new HashSet<Long>());
        	
        	
            // 사용자가 현재 게시물을 조회한 적이 없다면 조회수 증가
            if (!viewedPosts.contains(postId)) {
                postService.visitPost(postId);  // 조회수 증가 메소드 호출
                viewedPosts.add(postId);  // 현재 게시물 ID를 사용자 조회 기록에 추가
                log.info("최초 방문");
            }        	
        	
            // 사용자가 현재 게시물을 좋아요 했는 지 확인
        	if(postIdSet.contains(postId)) {
         		liked = true;
         	} else {    
         		liked =  false;
     		}      
        	
        	model.addAttribute("session.user", user);
     	}

        // 댓글 페이지네이션 처리
        Page<Comment> commentsPage = commentPageService.getCommentsByPostId(postId, page, size, sortOrder);
        model.addAttribute("comments", commentsPage.getContent());
        model.addAttribute("totalPages", commentsPage.getTotalPages());
        model.addAttribute("currentPage", page);

        String imgName = null;
        if (postService.findPost(postId).getImgName() != null && !postService.findPost(postId).getImgName().isEmpty()) {
            String[] imgNameParts = postService.findPost(postId).getImgName().split("_");
            imgName = imgNameParts.length > 1 ? imgNameParts[1] : null;
            model.addAttribute("imgName", imgName);
        }
        
        model.addAttribute("post", post);
        model.addAttribute("boardId", boardId);
        model.addAttribute("imgName", postService.getOriginalFileName(postId));     
        model.addAttribute("liked", liked);
        model.addAttribute("likeCount", post.getLikeCount());
           
        return "post/post-details";
    }
    
    // 게시글 생성 페이지
    @GetMapping("/create")
    public String createPostPage(Model model, Long boardId, @RequestParam("userId") Long userId, Post post) {
    	model.addAttribute("postDto", new PostDto());
        model.addAttribute("boardId", boardId);
        
        post.setUser(userService.findUserByUserId(userId));
        
        return "post/posts-create";
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
			return "redirect:/post/posts/create";
		}
    	
    	Post post = postDto.toPost();    	
    	post.setUser(userService.findUserByUserId(userId));
    	    	
    	if(file != null && !file.isEmpty()) {
    		postService.uploadFile(post, file);
		} 
    	postService.createPost(post, boardId);
    	
    	redirect.addAttribute("boardId", boardId);
        return "redirect:/post/posts/search";
    }
    
    // 게시글 수정 페이지
    @GetMapping("/update")
	public String updatePostPage(Long boardId, Long postId, Model model) {

    	model.addAttribute("post", postService.findPost(postId));
    	model.addAttribute("boardId", boardId);
        model.addAttribute("originalFileName", postService.getOriginalFileName(postId));

    	return "post/posts-update";
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

		return "redirect:/post/posts/{postId}";
    }
    
    // 게시글 삭제
    @PostMapping("/delete")
    public String deletePost(Long postId, Long boardId, RedirectAttributes redirect) {
        
    	postService.deletePost(postId);
        redirect.addAttribute("boardId", boardId);

        return "redirect:/post/posts/search";
    }
    
    // 좋아요 버튼 눌렀을 때
    @PostMapping("/like")
    @ResponseBody
    public Map<String, Object> clickLike(@RequestBody Map<String, Long> requestData) {
    	Long postId = requestData.get("postId");
	    Long userId = requestData.get("userId");

    	Map<String, Object> response = new HashMap<>();

	    boolean liked = false;        
    
        Post post = postService.findPost(postId);
	    User user = userService.findUserByUserId(userId);

	    	if(likeMap.get(userId).contains(postId)) {
	    		likeMap.get(userId).remove(postId);
	    		liked = false;
	    		
	    		postService.updateCount(post, liked);
	    		
	    	} else {
	    		likeMap.get(userId).add(postId);
	    		liked = true;
	    		
	    		postService.updateCount(post, liked);
	    }
	    
		response.put("liked", liked);
		response.put("likeCount", post.getLikeCount());

		return response;
	}
    
}