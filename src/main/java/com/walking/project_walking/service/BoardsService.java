package com.walking.project_walking.service;

import com.walking.project_walking.domain.Posts;
import com.walking.project_walking.repository.PostsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BoardsService {
    private final PostsRepository postsRepository;

    public BoardsService(PostsRepository boardsRepository) {
        this.postsRepository = boardsRepository;
    }

    public int getPageCount(Long boardId) {
        Page<Posts> postsPage = postsRepository.findByBoardId(boardId, PageRequest.of(0, 6));
        return postsPage.getTotalPages();
    }
}