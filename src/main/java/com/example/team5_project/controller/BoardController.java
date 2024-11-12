package com.example.team5_project.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.team5_project.entity.Board;
import com.example.team5_project.service.BoardService;


@RequestMapping("/board")
@Controller
public class BoardController {
	@Autowired private BoardService boardService;
	
	// 전체 게시판 리스트
	@GetMapping("/list")
	public String getBoard(Model model) {
		model.addAttribute("boards", boardService.findBoards());
		
		return "board/list";
	}

	// 게시판 생성 페이지
	@GetMapping("/create")
	public String createBoardPage() {
		
		return "board/create";
	}
	
	// 게시판 생성
	@PostMapping("/upload")
	public String createBoard(Board board) {
		boardService.createBoard(board);
		
		return "redirect:/board/list";
	}

	// 게시판 수정 페이지
	@GetMapping("/update/{boardId}")
	public String updateBoardPage(@PathVariable("boardId") Long boardId, Model model) {
		model.addAttribute("board", boardService.findBoard(boardId));
		
		return "board/update";
	}
	
	// 게시판 수정
	@PostMapping("/update")
	public String updateBoard(Board board, RedirectAttributes redirect) {
		boardService.updateBoard(board);		
		redirect.addAttribute("boardId", board.getBoardId());
		
		return "redirect:/post/posts/search";
	}	
	
	// 게시판 삭제
	@PostMapping("/delete")
	public String deleteBoard(@RequestParam("boardId") Long boardId) {
		Board board = boardService.findBoard(boardId);
		boardService.deleteBoard(board);
		
		return "redirect:/board/list";
	}
}



