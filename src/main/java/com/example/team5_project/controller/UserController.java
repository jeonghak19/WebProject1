package com.example.team5_project.controller;

import com.example.team5_project.entity.User;
import com.example.team5_project.dto.UserPostDto;
import com.example.team5_project.mapper.UserMapper;
import com.example.team5_project.service.CommentService;
import com.example.team5_project.service.PostPageService;
import com.example.team5_project.service.PostService;
import com.example.team5_project.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j

@Controller
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final PostPageService postPageService;

    public UserController(UserService userService, PostService postService, CommentService commentService, PostPageService postPageService) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.postPageService = postPageService;
    }
    // 사용자 목록 가져오기
    @GetMapping("/home/users")
    public String getUsers(Model model) {

        model.addAttribute("users", userService.findUsers());
        return "/home/users";
    }

    // 사용자 등록
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserPostDto userPostDto,Model model) {
        User user = userService.findUserByName(userPostDto.getName());

        // 사용자 존재 여부 및 비밀번호 확인
        if (user == null) {
            User newUser = UserMapper.toEntity(userPostDto);
            userService.createUser(newUser);
            return "redirect:/board/list";
        } else {
            model.addAttribute("error", "이미 존재하는 이름입니다.");
            return "register";  // 실패 시 다시 회원가입 페이지로 이동
        }
    }

    // 등록화면 이동
    @GetMapping("/register")
    public String MoveRegister() {
        return "register";
    }

    // 사용자 삭제
    @PostMapping("/home/user-details/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id,HttpSession session) {
        userService.deleteUser(id);
        session.invalidate();
        return "redirect:/board/list";
    }

    // 사용자 수정
    @PostMapping("/home/user-update/{id}/edit")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute User user, Model model) {
        user.setUserId(id);
        userService.updateUser(user);
        model.addAttribute("comments",commentService.findCommentsByUserId(id));
        model.addAttribute("posts",postService.findUserPosts(id));
        return "redirect:/home/user-details/"+id;
    }

    // 사용자 수정 화면
    @GetMapping("/home/user-update/{id}")
    public String moveupdateUser(@PathVariable("id") Long id,Model model) {
        model.addAttribute("user",userService.findUserByUserId(id));
        return "/home/user-update";
    }

    /*// 특정 사용자 상세 정보 가져오기
    @GetMapping("/home/user-details/{id}")
    public String getUser(@PathVariable Long id, Model model,HttpSession session) {
        User loginUser = (User) session.getAttribute("user");

        if(loginUser == null) {
            model.addAttribute("error","로그인이 필요합니다.");
            return "redirect:/login";
        }
        if(loginUser.getUserId().equals(id)) {
            model.addAttribute("comments",commentService.findCommentsByUserId(id));
            model.addAttribute("posts",postService.findUserPosts(id));
            model.addAttribute("user", userService.findUserByUserId(id));
            return "/home/user-details";
        }else{
            model.addAttribute("error","접근 제한됨");
            return "redirect:/home/users";
        }
    }*/

    // 특정 사용자 상세 정보 가져오기
    @GetMapping("/home/user-details/{id}")
    public String getUser(@PathVariable Long id,
                          Model model,HttpSession session,
                          @RequestParam(defaultValue = "0") int page) {
        User loginUser = (User) session.getAttribute("user");

        int pageSize = 5; // 한 페이지에 보여줄 게시물 개수
        Pageable pageable = PageRequest.of(page, pageSize);
        if(loginUser == null) {
            model.addAttribute("error","로그인이 필요합니다.");
            return "redirect:/login";
        }
        if(loginUser.getUserId().equals(id)) {
            model.addAttribute("comments",commentService.findCommentsByUserId(id));
            model.addAttribute("postsPage",postPageService.getPostPageByUser(id,pageable));
            model.addAttribute("user", userService.findUserByUserId(id));
            return "/home/user-details";
        }else{
            model.addAttribute("error","접근 제한됨");
            return "redirect:/board/list";
        }
    }

    // 로그인 페이지 이동
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password, HttpSession session, Model model) {

        User user = userService.findUserByName(name);

        // 사용자 존재 여부 및 비밀번호 확인
        if (user != null && password.equals(user.getPassword())) {
            session.setAttribute("user", user);
            return "redirect:/board/list?loginSuccess=true"; // 로그인 성공 시 mainpage
        } else {
            model.addAttribute("error", "잘못된 이름 또는 비밀번호입니다.");
            return "login";  // 실패 시 다시 로그인 페이지로 이동
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session,Model model) {
        session.invalidate();  // 세션 무효화하여 로그아웃 처리
        model.addAttribute("check", "로그아웃 되었습니다.");
        return "redirect:/login";  // 로그인 페이지로 리디렉션
    }
}




