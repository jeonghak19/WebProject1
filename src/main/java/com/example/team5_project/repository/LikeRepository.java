package com.example.team5_project.repository;

import java.util.List;
import java.util.Optional;

import com.example.team5_project.entity.Likes;


public interface LikeRepository {
	  Optional<Likes> findByPostAndUser(Long postId, Long UserId);
	  List<Likes> findAllByUser(Long userId);
	  Likes save(Likes like);
	  void delete(Likes like);
}
