package com.example.team5_project.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.team5_project.entity.Comment;
import com.example.team5_project.entity.Post;
import com.example.team5_project.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class JdbcTemplateCommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 댓글 작성
    public void saveComment(Comment comment) {
        String sql = "INSERT INTO comment (post_id, user_id, content, comment_time) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, comment.getPost().getPostId(), comment.getUser().getUserId(), 
                comment.getContent(), new Timestamp(System.currentTimeMillis()));
    }

    // 댓글 목록 불러오기
    public List<Comment> getCommentsByPost(Long postId) {
        String sql = "SELECT * FROM comment WHERE post_id = ?";
        return jdbcTemplate.query(sql, new Object[]{postId}, new RowMapper<Comment>() {
            @Override
            public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
                Comment comment = new Comment();
                comment.setCommentId(rs.getLong("comment_id"));
                comment.setContent(rs.getString("content"));
                comment.setCommentTime(rs.getTimestamp("comment_time"));
                
                // Post 및 User 엔티티 설정
                Post post = new Post(); // 실제 Post 엔티티에서 조회해야 합니다.
                post.setPostId(rs.getLong("post_id"));
                comment.setPost(post);

                User user = new User(); // 실제 User 엔티티에서 조회해야 합니다.
                user.setUserId(rs.getLong("user_id"));
                comment.setUser(user);

                return comment;
            }
        });
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        String sql = "DELETE FROM comment WHERE comment_id = ?";
        jdbcTemplate.update(sql, commentId);
    }
}
