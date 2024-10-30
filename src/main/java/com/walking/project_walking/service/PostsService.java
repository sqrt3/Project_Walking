package com.walking.project_walking.service;

import com.walking.project_walking.domain.Posts;
import com.walking.project_walking.domain.dto.PostResponseDto;
import com.walking.project_walking.repository.CommentsRepository;
import com.walking.project_walking.repository.PostsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final CommentsRepository commentsRepository;

    public PostsService(PostsRepository postsRepository, CommentsRepository commentsRepository) {
        this.postsRepository = postsRepository;
        this.commentsRepository = commentsRepository;
    }

    public List<PostResponseDto> getPostsByBoardId(Long boardId, PageRequest pageRequest) {
        return postsRepository.findByBoardId(boardId, pageRequest)
                .map(post -> {
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(post.getPostId());
                    return PostResponseDto.fromEntity(post, commentsNumber);
                }).toList();
    }

    public List<PostResponseDto> searchPosts(Long boardId, String title, String content, Long userId, PageRequest pageRequest) {
        Page<Posts> postsPage = postsRepository.searchPosts(boardId, title, content, userId, pageRequest);
        return postsPage.getContent().stream()
                .map(post -> {
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(post.getPostId());
                    return PostResponseDto.fromEntity(post, commentsNumber);
                })
                .collect(Collectors.toList());
    }
}
