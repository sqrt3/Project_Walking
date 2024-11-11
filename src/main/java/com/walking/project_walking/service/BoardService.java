package com.walking.project_walking.service;

import com.walking.project_walking.domain.Board;
import com.walking.project_walking.domain.Posts;
import com.walking.project_walking.domain.dto.BoardRequestDto;
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
        return postsPage.getTotalPages() > 0 ? postsPage.getTotalPages() : 1;
    }

    // 현재 페이지를 반환하는 메소드
    public int getCurrentPage(Long boardId, int requestedPage) {
        int totalPageCount = getPageCount(boardId);
        if (requestedPage < 1) {
            return 1;
        } else if (requestedPage > totalPageCount) {
            return totalPageCount;
        }
        return requestedPage;
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

    public BoardResponseDto addBoard(BoardRequestDto boardRequestDto) {
        Board board = boardRequestDto.toEntity();
        Board savedBoard = boardRepository.save(board);
        return new BoardResponseDto(savedBoard.getBoardId(), savedBoard.getName());
    }

    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) {
            return null;
        }
        return new BoardResponseDto(board.getBoardId(), board.getName());
    }

    public BoardResponseDto deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board != null) {
            boardRepository.delete(board);
            return new BoardResponseDto(board.getBoardId(), board.getName());
        }
        return null;
    }

    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto boardRequestDto) {
        Board existingBoard = boardRepository.findById(boardId).orElse(null);
        if (existingBoard == null) {
            return null;
        }
        existingBoard.setName(boardRequestDto.getName());
        Board updatedBoard = boardRepository.save(existingBoard);
        return new BoardResponseDto(updatedBoard.getBoardId(), updatedBoard.getName());
    }

    //모든 게시판을 조회하여 반환하는 메소드 (게시글 작성 페이지로 이동)

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

}