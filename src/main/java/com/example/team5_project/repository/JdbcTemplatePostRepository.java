package com.example.team5_project.repository;


import com.example.team5_project.entity.Board;
import com.example.team5_project.entity.Post;
import com.example.team5_project.entity.User;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;


@Repository
public class JdbcTemplatePostRepository implements PostRepository {

    private JdbcTemplate jdbcTemplate;
    private UserRepository userRepository;
    private BoardRepository boardRepository;

    public JdbcTemplatePostRepository(JdbcTemplate jdbcTemplate,UserRepository userRepository,BoardRepository boardRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
    }

    private final RowMapper<Post> postRowMapper = (rs, rowNum) -> {
        Post post = new Post();
        post.setPostId(rs.getLong("post_id"));
        post.setPostTitle(rs.getString("post_title"));
        post.setDescription(rs.getString("description"));
//        post.setPostLike(rs.getInt("post_like"));
//        post.setPostDislike(rs.getInt("post_dislike"));
        post.setImgName(rs.getString("img_name"));
        post.setImgPath(rs.getString("img_path"));
        post.setCreatedAt(rs.getString("created_at"));
        post.setUpdatedAt(rs.getString("updated_at"));

        Optional<User> user = userRepository.findById(rs.getLong("user_id"));
        post.setUser(user.isPresent() ? user.get() : null);

        Optional<Board> board = boardRepository.findById(rs.getLong("board_id"));
        post.setBoard(board.isPresent() ? board.get() : null);

        return post;
    };

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "select * from post where post_id = ?";
        return jdbcTemplate.query(sql, postRowMapper, new Object[]{id}).stream().findFirst();
    }

    @Override
    public Post save(Post post, Long boardId) {
        if(post.getPostId()==null){
            String sql = "INSERT INTO post (user_id, post_title, description, /*post_like, post_dislike, */img_name, img_path, board_id) "
            		+ "VALUES (/* ?, ?,*/?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql,new String[]{"post_id"});
                ps.setLong(1, post.getUser().getUserId());
                ps.setString(2, post.getPostTitle());
                ps.setString(3, post.getDescription());
//                ps.setInt(3, post.getPostLike());
//                ps.setInt(4, post.getPostDislike());
                ps.setString(4, post.getImgName());
                ps.setString(5, post.getImgPath());
                ps.setLong(6, boardId);

                return ps;
            },keyHolder);
            Number key=keyHolder.getKey();
            if(key!=null){
                post.setPostId(key.longValue());
            }
        }else{
            String sql = "UPDATE post SET post_title = ?, description =?, /*post_like = ?, post_dislike = ?,*/img_name=?, img_path = ? WHERE board_id = ?"
            		+ " AND post_id = ?";
            jdbcTemplate.update(sql,
                    post.getPostTitle(),
                    post.getDescription(),
//                    post.getPostLike(),
//                    post.getPostDislike(),
                    post.getImgName(),
                    post.getImgPath(),
                    boardId,
                    post.getPostId()
            );
        }
        return post;
    }

    @Override
    public void delete(Post post) {
        String sql = "DELETE FROM post WHERE post_id = ?";
        jdbcTemplate.update(sql, post.getPostId());
    }

    @Override
    public List<Post> findByBoardId(Long boardId) {
        String sql = "SELECT * FROM post WHERE board_id = ?";
        List<Post> posts = jdbcTemplate.query(sql, new Object[]{boardId}, postRowMapper);

        return posts;
    }

    @Override
    public List<Post> findByUserId(Long userId) {
        String sql = "SELECT * FROM post WHERE user_id = ?";
        List<Post> posts = jdbcTemplate.query(sql, new Object[]{userId}, postRowMapper);

        return posts;
    }

    @Override
    public List<Post> findByTitle(String title) {
        String sql = "SELECT * FROM post WHERE post_title LIKE ?";
        String searchTitle = "%" + title + "%";

        List<Post> posts = jdbcTemplate.query(sql, new Object[]{searchTitle}, postRowMapper);

        return posts;
    }
}

