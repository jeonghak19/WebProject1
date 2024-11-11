package com.example.team5_project.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.team5_project.entity.Likes;
import com.example.team5_project.entity.Post;
import com.example.team5_project.entity.User;
import com.example.team5_project.repository.LikeRepository;

import jakarta.transaction.Transactional;


@Service
public class LikesService {
	@Autowired private LikeRepository likeRepository;

	
	@Transactional
	public void addLike(Post post, User user) {
	    if (!likeRepository.findByPostAndUser(post.getPostId(), user.getUserId()).isPresent()) {
	        Likes like = new Likes(post, user);
	        likeRepository.save(like);
	    }
	}

	@Transactional
	public void deleteLike(Long postId, Long userId) {
	    Optional<Likes> likes = likeRepository.findByPostAndUser(postId, userId);
	    likes.ifPresent(like -> likeRepository.delete(like));
	}

	public boolean getLike(Long postId, Long userId) { 
	    return likeRepository.findByPostAndUser(postId, userId).isPresent();
	}
}