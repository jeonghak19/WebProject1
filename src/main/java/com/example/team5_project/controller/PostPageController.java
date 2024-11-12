package com.example.team5_project.controller;

import com.example.team5_project.entity.Post;
import com.example.team5_project.service.BoardService;
import com.example.team5_project.service.PostPageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/post/posts")
@Controller
public class PostPageController {

    private final PostPageService postPageService;
    private final BoardService boardService;

    public PostPageController(PostPageService postPageService, BoardService boardService) {
        this.postPageService = postPageService;
        this.boardService = boardService;
    }


    //게시글 검색, 띄우기
    @GetMapping("/search")
    public String searchPosts(@RequestParam("boardId") Long boardId,
                              @RequestParam(defaultValue = "") String searchTitle,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "time") String sortBy,
                              Model model) {

        int pageSize = 10; // 한 페이지에 보여줄 게시물 개수
        Pageable pageable;

        if(sortBy.equalsIgnoreCase("view")) {
            Sort viewSort = Sort.by(Sort.Direction.DESC, "viewCount");
            pageable = PageRequest.of(page, pageSize,viewSort);
        }else if(sortBy.equalsIgnoreCase("time")){
            Sort createSort = Sort.by(Sort.Direction.DESC, "createdAt");
            pageable = PageRequest.of(page, pageSize,createSort);
        }else{
            Sort likeSort = Sort.by(Sort.Direction.DESC, "likeCount");
            pageable = PageRequest.of(page, pageSize,likeSort);
        }


        model.addAttribute("sortBy", sortBy);
        model.addAttribute("searchTitle", searchTitle);
        model.addAttribute("boardTitle", boardService.getBoardTitle(boardId));
        model.addAttribute("boardId", boardId);
        if(searchTitle == null || searchTitle.trim().isEmpty()){
            model.addAttribute("postsPage", postPageService.getPostPageByBoardId(boardId,pageable));
        }else{
            model.addAttribute("postsPage", postPageService.getPostPageByTitle(searchTitle,boardId,pageable));
        }
        return "post/posts"; // 기존 페이지와 동일하게 렌더링
    }
}