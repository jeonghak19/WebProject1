package com.example.team5_project.repository;

import com.example.team5_project.entity.Board;
import com.example.team5_project.entity.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.team5_project.entity.Comment;
import com.example.team5_project.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTemplateCommentRepository implements CommentRepository {

    private JdbcTemplate jdbcTemplate;
    private UserRepository userRepository;
    private PostRepository postRepository;
    
    public JdbcTemplateCommentRepository(JdbcTemplate jdbcTemplate, UserRepository userRepository,PostRepository postRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    private final RowMapper<Comment> commentRowMapper = (rs, rowNum) -> {
        Comment comment = new Comment();
        comment.setCommentId(rs.getLong("comment_id"));
        comment.setContent(rs.getString("content"));
        comment.setCommentTime(rs.getTimestamp("comment_time"));

        Optional<User> user = userRepository.findById(rs.getLong("user_id"));
        comment.setUser(user.isPresent() ? user.get() : null);

        Optional<Post> post = postRepository.findById(rs.getLong("post_id"));
        comment.setPost(post.isPresent() ? post.get() : null);

        return comment;
    };
    
    @Override
    public Optional<Comment> findById(Long id) {
        String sql = "select * from comment where Comment_id = ?";
        return jdbcTemplate.query(sql, commentRowMapper, new Object[]{id}).stream().findFirst();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getCommentId() == null) { // 새 댓글 추가
            String sql = "INSERT INTO comment (post_id, user_id, content, comment_time) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"comment_id"});
                ps.setLong(1, comment.getPost().getPostId()); // postId 설정
                ps.setLong(2, comment.getUser().getUserId()); // userId 설정
                ps.setString(3, comment.getContent()); // 댓글 내용 설정
                ps.setTimestamp(4, comment.getCommentTime()); // 댓글 시간 설정
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                comment.setCommentId(key.longValue()); // 자동 생성된 commentId 설정
            }
        } else { // 기존 댓글 수정
            String sql = "UPDATE comment SET content = ? WHERE comment_id = ?";
            jdbcTemplate.update(sql, comment.getContent(), comment.getCommentId());
        }
        return comment;
    }


    @Override
    public void delete(Comment comment) {
        String sql = "delete from comment where comment_id = ?";
        jdbcTemplate.update(sql,comment.getCommentId());
    }

    @Override
    public List<Comment> findByPostId(Long Id) {
        String sql = "select * from comment where post_id = ?";
        return jdbcTemplate.query(sql,commentRowMapper, new Object[]{Id});
    }

    @Override
    public List<Comment> findByUserId(Long Id) {
        String sql = "select * from comment where user_id = ?";
        return jdbcTemplate.query(sql, commentRowMapper, new Object[]{Id});
    }
}
