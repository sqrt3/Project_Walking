package com.walking.project_walking.controller;

import com.walking.project_walking.domain.dto.PostResponseDto;
import com.walking.project_walking.domain.dto.PostSummuryResponseDto;
import com.walking.project_walking.service.BoardService;
import com.walking.project_walking.service.PostsService;
import com.walking.project_walking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {
    private final PostsService postsService;
    private final BoardService boardService;
    private final UserService userService;

    private static final int PAGE_SIZE = 6;

    // 제목, 글쓴이, 내용을 조합하여 특정 게시글 조회
    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDto>> searchPosts(
            @RequestParam Long boardId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String nickname,
            @RequestParam(defaultValue = "1") int page) {

        if (!boardService.existsById(boardId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }

        int board_page = boardService.getPageCount(boardId);

        if (page < 1 || page > board_page) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        List<PostResponseDto> postList = postsService.searchPosts(boardId, title, content, nickname, pageRequest);

        if (postList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(postList);
    }

    // 인기 게시판 전용
    // 인기글 조회 (전체)
    @GetMapping("/hot")
    public ResponseEntity<List<PostResponseDto>> getHotPosts() {
        List<PostResponseDto> postList = postsService.getHotPosts();

        if (postList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(postsService.getHotPosts());
    }

    // 인기글 조회 (1개)
    @GetMapping("/hot/{boardId}")
    public ResponseEntity<PostResponseDto> getOneHotPost(@PathVariable Long boardId) {
        if (!boardService.existsById(boardId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }

        PostResponseDto hotPost = postsService.getOneHotPost(boardId);

        if (hotPost == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(hotPost);
    }

    // 유저가 작성한 게시글 목록 보기
    @GetMapping("/userId")
    public ResponseEntity<List<PostSummuryResponseDto>> getUserPosts(@RequestParam Long userId) {
        if (!userService.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<PostSummuryResponseDto> userPosts = postsService.getPostsByUserId(userId);

        if (userPosts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(userPosts);
    }

}
