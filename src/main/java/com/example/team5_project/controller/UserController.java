package com.example.team5_project.controller;

import com.example.team5_project.entity.User;
import com.example.team5_project.dto.UserPostDto;
import com.example.team5_project.mapper.UserMapper;
import com.example.team5_project.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j

@Controller
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final CommentPageService commentPageService;
    private final PostPageService postPageService;

    public UserController(UserService userService, PostService postService, CommentPageService commentPageService, PostPageService postPageService) {
        this.userService = userService;
        this.postService = postService;
        this.commentPageService = commentPageService;
        this.postPageService = postPageService;
    }
    // 사용자 목록 가져오기
    @GetMapping("/user/users")
    public String getUsers(Model model) {

        model.addAttribute("users", userService.findUsers());
        return "/user/users";
    }

    // 이름 중복 체크
    @GetMapping("/user/check-name")
    @ResponseBody
    public Map<String, Object> checkName(@RequestParam("name") String name) {
        Map<String, Object> response = new HashMap<>();
        User existingUser = userService.findUserByName(name);
        response.put("isAvailable", existingUser == null); // 중복되지 않으면 true, 중복되면 false
        return response;
    }


    // 사용자 등록
    @PostMapping("/user/register")
    public String registerUser(@Valid @ModelAttribute("user") UserPostDto userPostDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "user/register"; // 오류가 있으면 register.html로 다시 돌아감
        }

        if (userService.findUserByName(userPostDto.getName()) != null) {
            model.addAttribute("nameErr", "중복된 이름이 있습니다.");
            return "user/register"; // 중복 Id 있으면 register.html로 다시 돌아감
        }
        User user = UserMapper.toEntity(userPostDto);
        userService.createUser(user);
        return "user/login";
    }

    // 등록화면 이동
    @GetMapping("/user/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserPostDto()); // 빈 User 객체를 폼에 전달
        return "user/register"; // 회원가입 폼을 반환 (register.html)
    }

    // 사용자 삭제
    @PostMapping("/user/user-details/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id,HttpSession session) {
        userService.deleteUser(id);
        session.invalidate();
        return "redirect:/user/login";
    }

    // 사용자 수정
    @PostMapping("/user/user-update/{id}/edit")
    public String updateUser(@PathVariable("id") Long id,
                             @ModelAttribute("user") User user, Model model,
                             @RequestParam(value="page" ,defaultValue = "0") int page) {

        int pageSize = 5; // 한 페이지에 보여줄 게시물 개수
        Pageable pageable = PageRequest.of(page, pageSize);

        user.setUserId(id);
        userService.updateUser(user);
        model.addAttribute("comments",commentPageService.getCommentsByUserId(id,pageable));
        model.addAttribute("posts",postService.findUserPosts(id));
        return "redirect:/user/user-details/"+id;
    }

    // 사용자 수정 화면
    @GetMapping("/user/user-update/{id}")
    public String moveupdateUser(@PathVariable("id") Long id,Model model) {
        model.addAttribute("user",userService.findUserByUserId(id));
        return "/user/user-update";
    }

    // 특정 사용자 상세 정보 가져오기
    @GetMapping("/user/user-details/{id}")
    public String getUser(@PathVariable("id") Long id,
                          Model model,HttpSession session,
                          @RequestParam(value="postPage", defaultValue = "0") int postPage,
                          @RequestParam(value="commentPage", defaultValue = "0") int commentPage) {
        User loginUser = (User) session.getAttribute("user");

        int postPageSize = 5; // 한 페이지에 보여줄 게시물 개수
        Pageable postPageable = PageRequest.of(postPage, postPageSize);

        int commentPageSize = 5;
        Pageable commentPageable = PageRequest.of(commentPage, commentPageSize);

        if(loginUser == null) {
            model.addAttribute("error","로그인이 필요합니다.");
            return "redirect:/user/login";
        }
        if(loginUser.getUserId().equals(id)) {
            model.addAttribute("commentsPage",commentPageService.getCommentsByUserId(id,commentPageable));
            model.addAttribute("postsPage",postPageService.getPostPageByUser(id,postPageable));
            model.addAttribute("user", userService.findUserByUserId(id));
            return "/user/user-details";
        }else{
            model.addAttribute("error","접근 제한됨");
            return "redirect:/board/list";
        }
    }

    // 로그인 페이지 이동
    @GetMapping("/user/login")
    public String showLoginPage() {
        return "user/login";
    }

    // 로그인 처리
    @PostMapping("/user/login")
    public String login(@RequestParam("name") String name, @RequestParam("password") String password, HttpSession session, Model model) {

        User user = userService.findUserByName(name);

        // 사용자 존재 여부 및 비밀번호 확인
        if (user != null && password.equals(user.getPassword())) {
            user.setAttendance(user.getAttendance()+1);
            session.setAttribute("user", user);
            return "redirect:/board/list?loginSuccess=true"; // 로그인 성공 시 mainpage
        } else {
            model.addAttribute("error", "잘못된 이름 또는 비밀번호입니다.");
            return "user/login";  // 실패 시 다시 로그인 페이지로 이동
        }
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session,Model model) {
        session.invalidate();  // 세션 무효화하여 로그아웃 처리
        model.addAttribute("check", "로그아웃 되었습니다.");
        return "redirect:/user/login";  // 로그인 페이지로 리디렉션
    }
}
