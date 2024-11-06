package com.example.team5_project.repository;

import java.util.List;
import java.util.Optional;

import com.example.team5_project.entity.Board;


public interface BoardRepository {
	List<Board> findAll();
	Optional<Board> findById(Long boardId);
	Board save(Board board);
	void delete(Board board);	
}
