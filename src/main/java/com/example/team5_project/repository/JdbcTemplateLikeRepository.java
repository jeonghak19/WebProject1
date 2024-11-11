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

import com.example.team5_project.entity.Likes;
import com.example.team5_project.entity.Post;
import com.example.team5_project.entity.User;


@Repository
public class JdbcTemplateLikeRepository implements LikeRepository{
	@Autowired private JdbcTemplate jdbcTemplate;
	@Autowired private UserRepository userRepository;
	@Autowired private PostRepository postRepository;
	
	public final RowMapper<Likes> rowMapper = (rs, rowNum) -> {
		Likes like = new Likes();
		like.setLikeId(rs.getLong("like_id"));
		
		Optional<User> user = userRepository.findById(rs.getLong("user_id"));
		like.setUser(user.isPresent() ? user.get() : null);
		
		Optional<Post> post = postRepository.findById(rs.getLong("post_id"));
		like.setPost(post.isPresent() ? post.get() : null);
		
		return like;
	};
	
	
	public Optional<Likes> findByPostAndUser(Long postId, Long userId) {
		String sql = "SELECT * FROM likes WHERE post_id =? AND user_id =?";
		return jdbcTemplate.query(sql, rowMapper, postId, userId).stream().findFirst();
	}
	
	
	public List<Likes> findAllByUser(Long userId) {
		String sql = "SELECT * FROM likes WHERE user_id =?";
		return jdbcTemplate.query(sql, rowMapper, userId);
	}
	
	
    public Likes save(Likes like) {
        if(like.getLikeId()==null){
            String sql = "INSERT INTO likes (post_id, user_id) VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql,new String[]{"like_id"});
                ps.setLong(1, like.getPost().getPostId());
                ps.setLong(2,  like.getUser().getUserId());

                return ps;
            },keyHolder);
            Number key = keyHolder.getKey();
            if(key!=null){
                like.setLikeId(key.longValue());
            }
        }
        return like;
    }
	
		
	public void delete(Likes likes) {
		String sql = "DELETE FROM likes WHERE like_id =?";
		jdbcTemplate.update(sql, likes.getLikeId());
	}	
}
