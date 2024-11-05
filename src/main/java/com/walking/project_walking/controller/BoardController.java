package com.walking.project_walking.controller;

import com.walking.project_walking.domain.dto.BoardResponseDto;
import com.walking.project_walking.domain.dto.NoticeResponseDto;
import com.walking.project_walking.domain.dto.PostResponseDto;
import com.walking.project_walking.service.BoardService;
import com.walking.project_walking.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final PostsService postsService;

    private static final int PAGE_SIZE = 6;

    // 게시판 목록 조회
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getBoardsList() {
        List<BoardResponseDto> boardList = boardService.getBoardsList();

        if (boardList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(boardList);
    }

    // 특정 게시판, 특정 페이지의 게시글 조회
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getPostsByBoard(
            @RequestParam Long boardId,
            @RequestParam(defaultValue = "1") int page) {

        int board_page = boardService.getPageCount(boardId);

        if (page < 1 || page > board_page) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        List<PostResponseDto> postsList = postsService.getPostsByBoardId(boardId, pageRequest);

        return ResponseEntity.ok(postsList);
    }

    // 공지사항 조회
    @GetMapping("/notices")
    public ResponseEntity<List<NoticeResponseDto>> getNoticesByBoard(){
        PageRequest pageRequest = PageRequest.of(0, 2);
        List<NoticeResponseDto> noticesList = postsService.getNoticePosts(pageRequest);

        if (noticesList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(noticesList);
    }

    // 특정 게시판의 페이지 갯수 조회
    @GetMapping("/{boardId}/pagescount")
    public ResponseEntity<Integer> getPostsCount(@PathVariable Long boardId) {
        if (!boardService.existsById(boardId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return ResponseEntity.ok(boardService.getPageCount(boardId));
    }
}
