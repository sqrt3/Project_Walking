package com.walking.project_walking.service;

import com.walking.project_walking.domain.Posts;
import com.walking.project_walking.domain.dto.BoardResponseDto;
import com.walking.project_walking.repository.BoardRepository;
import com.walking.project_walking.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final PostsRepository postsRepository;
    private final BoardRepository boardRepository;

    private static final Integer PAGE_SIZE = 6;

    // 특정 게시판의 총 페이지 개수를 조회하는 메소드
    public int getPageCount(Long boardId) {
        Page<Posts> postsPage = postsRepository.findByBoardId(boardId, PageRequest.of(0, PAGE_SIZE));
        return postsPage.getTotalPages();
    }

    // 모든 게시판의 boardId와 name을 조회하는 메소드
    public List<BoardResponseDto> getBoardsList() {
        return boardRepository.findAll().stream()
                .map(board -> new BoardResponseDto(board.getBoardId(), board.getName()))
                .toList();
    }

    // 실제 존재하는 boardId인지 검증하는 메소드
    public boolean existsById(Long boardId) {
        return boardRepository.existsById(boardId);
    }
}