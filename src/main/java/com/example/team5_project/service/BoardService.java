package com.example.team5_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.team5_project.entity.Board;
import com.example.team5_project.repository.BoardRepository;


@Service
public class BoardService {
	@Autowired private BoardRepository boardRepository;
	
	// 전체 게시판 조회
	public List<Board> findBoards() {
		return boardRepository.findAll();
	}
	
	// 특정 게시판 조회
	public Board findBoard(Long boardId) {
		return boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException());
	}
	
	// 새 게시글 생성
	public Board createBoard(Board board) {
		return boardRepository.save(board);
	}
	
	// 게시글 수정
	public Board updateBoard(Board board) {
		Board findBoard = boardRepository.findById(board.getBoardId()).orElseThrow(() -> new RuntimeException());
		Optional.ofNullable(board.getBoardTitle()).ifPresent(boardTitle -> findBoard.setBoardTitle(boardTitle));

		return boardRepository.save(findBoard);
	}	
	
	// 게시글 삭제
	public void deleteBoard(Board board) {
		boardRepository.delete(board);
	}
	
	// boardTitle 불러오기
	public String getBoardTitle(Long boardId) {
		return findBoard(boardId).getBoardTitle();
	}
	
 }
