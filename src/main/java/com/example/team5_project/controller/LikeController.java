//package com.example.team5_project.controller;
//
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.example.team5_project.entity.Post;
//import com.example.team5_project.entity.User;
//import com.example.team5_project.service.LikesService;
//import com.example.team5_project.service.PostService;
//import com.example.team5_project.service.UserService;
//
//
//@RequestMapping("/home/posts")
//@Controller
//public class LikeController {
//	@Autowired private LikesService likesService;
//    @Autowired private PostService postService;
//    @Autowired private UserService userService;
//	
//	
//    @PostMapping("/like")
//    @ResponseBody
//    public Map<String, Object> clickLike(@RequestBody Map<String, Long> requestData) {
//    	Long postId = requestData.get("postId");
//	    Long userId = requestData.get("userId");
//    	Map<String, Object> response = new HashMap<>();
//        
//	    boolean liked = false;        
//    
//        Post post = postService.findPost(postId);
//	    User user = userService.findUserByUserId(userId);
//
//	    if (likesService.getLike(postId, userId)) {
//	        likesService.deleteLike(postId, userId);
//	        System.out.println("like 컨트롤러 삭제쪽 현재 likeCount: " +post.getLikeCount());
//
//	        liked = false;
//	       // postService.updateCount(post, liked);
//	        postService.updatePost(post, userId);
//	        System.out.println("좋아요 취소 후 likeCount: " + post.getLikeCount());
//	        
//	    } else {
//	        likesService.addLike(post, user);
//	        System.out.println("like 컨트롤러 추가 쪽 현재 likeCount: " +post.getLikeCount());
//
//	        liked = true;
//	        //postService.updateCount(post, liked);
//	        postService.updatePost(post, userId);
//	        System.out.println("좋아요 누른 후 likeCount: " + post.getLikeCount());
//	    }
//		response.put("liked", liked);
//		response.put("likeCount", post.getLikeCount());
//		return response;
//	}
//}
