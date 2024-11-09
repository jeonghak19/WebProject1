package com.example.team5_project.repository;

import com.example.team5_project.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostPageRepository extends JpaRepository<Post, Long> {
    Page<Post> findByBoard_BoardId(Long boardId, Pageable pageable);
    Page<Post> findByPostTitleContainingAndBoard_BoardId(String postTitle, Long boardId, Pageable pageable);
    Page<Post> findByUser_UserId(Long userId, Pageable pageable);
}
