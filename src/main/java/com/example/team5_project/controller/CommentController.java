package com.example.team5_project.controller;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

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

    // 댓글 작성
    @PostMapping("/{postId}/comments")
    public String saveComment(@PathVariable("postId") Long postId,
                              @RequestParam("content") String content,
                              @RequestParam("boardId") Long boardId,
                              RedirectAttributes redirectAttributes) {
        
        // 세션에서 로그인된 사용자 정보 가져오기
        User sessionUser = (User) session.getAttribute("user");
        
        // 로그인 여부 체크
        if (sessionUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 후 댓글을 작성할 수 있습니다.");
            return "redirect:/home/posts/" + postId + "?boardId=" + boardId;
        }

        // Post 객체 조회
        Post post = postService.findPost(postId);
        
        // Comment 객체 생성 및 설정
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(sessionUser);  // 세션에서 가져온 사용자 설정
        comment.setContent(content);
        comment.setCommentTime(new Timestamp(System.currentTimeMillis()));

        // 댓글 저장
        commentService.saveComment(comment, sessionUser.getUserId(), postId);

        redirectAttributes.addFlashAttribute("message", "댓글이 작성되었습니다.");
        return "redirect:/home/posts/" + postId + "?boardId=" + boardId;
    }
    
 // 댓글 삭제
    @PostMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("postId") Long postId, 
                                @PathVariable("commentId") Long commentId, 
                                @RequestParam("boardId") Long boardId,
                                RedirectAttributes redirectAttributes) {

    	// 세션에서 로그인된 사용자 정보 가져오기
    	User sessionUser = (User) session.getAttribute("user");

    	// 로그인 여부 체크
    	if (sessionUser == null) {
    	    redirectAttributes.addFlashAttribute("errorMessage", "로그인 후 댓글을 작성할 수 있습니다.");
    	    return "redirect:/home/posts/" + postId + "?boardId=" + boardId;
    	}


        Comment comment = commentService.findComment(commentId);

        // 댓글 작성자와 로그인된 사용자가 일치하는지 확인
        if (comment.getUser().getUserId().equals(sessionUser.getUserId())) {
            commentService.deleteComment(commentId);  // 삭제
            redirectAttributes.addFlashAttribute("message", "댓글이 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제 권한이 없습니다.");
        }

        return "redirect:/home/posts/" + postId + "?boardId=" + boardId;
    }

    // 댓글 수정
    @PostMapping("/{postId}/comments/{commentId}/edit")
    public String editComment(@PathVariable("postId") Long postId, 
                              @PathVariable("commentId") Long commentId, 
                              @RequestParam("content") String content,
                              @RequestParam("boardId") Long boardId,
                              RedirectAttributes redirectAttributes) {

        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인 후 댓글을 수정할 수 있습니다.");
            return "redirect:/home/posts/" + postId + "?boardId=" + boardId;
        }

        Comment comment = commentService.findComment(commentId);

        if (!comment.getUser().getUserId().equals(sessionUser.getUserId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "수정 권한이 없습니다.");
            return "redirect:/home/posts/" + postId + "?boardId=" + boardId;
        }

        comment.setContent(content);
        commentService.saveComment(comment, sessionUser.getUserId(), postId);  // 수정된 댓글 저장

        redirectAttributes.addFlashAttribute("message", "댓글이 수정되었습니다.");
        return "redirect:/home/posts/" + postId + "?boardId=" + boardId;
    }

}


