package com.example.team5_project.repository;

import com.example.team5_project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTemplateUserRepository implements UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setAttendance(rs.getInt("attendance"));
        return user;
    };

    @Override
    public Optional<User> findById(Long userId) {
        String sql = "select * from user where user_id = ?";
        return jdbcTemplate.query(sql, userRowMapper, new Object[]{userId}).stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from user";
        return jdbcTemplate.query(sql,userRowMapper);
    }

    @Override
    public User save(User user) {
        if(user.getUserId()==null){
            String sql = "INSERT INTO user (name, password, email, attendance) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql,new String[]{"user_id"});
                ps.setString(1, user.getName());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getEmail());
                ps.setInt(4, 1);
                return ps;
            },keyHolder);
            Number key=keyHolder.getKey();
            if(key!=null){
                user.setUserId(key.longValue());
            }
        }else{
            String sql = "UPDATE user SET name = ?, password = ?, email = ?, attendance = ? WHERE user_id = ?";
            jdbcTemplate.update(sql,
                    user.getName(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getAttendance(),
                    user.getUserId()
            );
        }
        return user;
    }

    @Override
    public void delete(User user) {
        String sql = "delete from user where user_id = ?";
        jdbcTemplate.update(sql,user.getUserId());
    }

    @Override
    public Optional<User> findbyCommentId(Long commentId) {
        String sql = "SELECT * FROM user JOIN comment ON user.user_id = comment.user_id WHERE comment.comment_id = ?";
        return jdbcTemplate.query(sql, userRowMapper, new Object[]{commentId}).stream().findFirst();
    }

    @Override
    public Optional<User> findbyPostId(Long postId) {
        String sql = "SELECT * FROM user JOIN post ON user.user_id = post.user_id WHERE post.post_id = ?";
        return jdbcTemplate.query(sql, userRowMapper, new Object[]{postId}).stream().findFirst();
    }

    @Override
    public Optional<User> findByName(String name) {
        String sql = "SELECT * FROM user WHERE name = ?";
        return jdbcTemplate.query(sql, userRowMapper, new Object[]{name}).stream().findFirst();
    }
}
