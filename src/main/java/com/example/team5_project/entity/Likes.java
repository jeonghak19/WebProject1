package com.example.team5_project.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Likes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long likeId;
	
	@ManyToOne
	@JoinColumn(name ="post_id", nullable=false)
	private Post post;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	public Likes(Post post, User user) {
		this.post = post;
		this.user = user;
	}
}
