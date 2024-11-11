package com.example.team5_project.service;

import com.example.team5_project.entity.Comment;
import com.example.team5_project.entity.Post;
import com.example.team5_project.repository.PostPageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostPageService {
    private final PostPageRepository postPageRepository;

    public PostPageService(PostPageRepository postPageRepository) {
        this.postPageRepository = postPageRepository;
    }

    public Page<Post> getPostPageByBoardId(Long boardId, Pageable pageable) {
        return postPageRepository.findByBoard_BoardId(boardId,pageable);
    }

    public Page<Post> getPostPageByTitle(String title, Long boardId, Pageable pageable) {
        return postPageRepository.findByPostTitleContainingAndBoard_BoardId(title,boardId,pageable);
    }

    public Page<Post> getPostPageByUser(Long userId, Pageable pageable) {
        return postPageRepository.findByUser_UserId(userId,pageable);
    }
}