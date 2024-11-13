package com.example.team5_project.repository;


import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.team5_project.entity.Board;


@Repository
public class BoardJdbcTempleteRepository implements BoardRepository {
	@Autowired private JdbcTemplate jdbcTemplate;
	
	private final RowMapper<Board> rowMapper = (rs, rowNum) -> {
		Board board = new Board();
		board.setBoardId(rs.getLong("board_id"));
		board.setBoardTitle(rs.getString("board_title"));
		
		return board;
	};
	
	public List<Board> findAll() {
		String sql = "SELECT * FROM board";
		return jdbcTemplate.query(sql, rowMapper);		
	}
	
	public Optional<Board> findById(Long boardId) {
		String sql = "SELECT * FROM board WHERE board_id =?";
		return jdbcTemplate.query(sql, rowMapper, boardId).stream().findFirst();
	}
	
	public Board save(Board board) {
		if(board.getBoardId() == null) {
			String sql = "INSERT INTO board(board_title) VALUES(?)";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] {"board_id"});
			ps.setString(1, board.getBoardTitle());
			return ps;
			}, keyHolder);
			
			Number key = keyHolder.getKey();
			if(key != null) {
				board.setBoardId(key.longValue());
			}
		} else {
			String sql = "UPDATE board SET board_title =? WHERE board_id =?";
			jdbcTemplate.update(sql, board.getBoardTitle(), board.getBoardId());
		}
		
		return board;
	}
	
	public void delete(Board board) {
		String postSq1 = "DELETE FROM post WHERE board_id =?";
		String boardSql = "DELETE FROM board WHERE board_id =?";
		
		jdbcTemplate.update(postSq1, board.getBoardId());
		jdbcTemplate.update(boardSql, board.getBoardId());
	}
}
