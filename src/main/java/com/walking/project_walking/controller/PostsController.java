package com.walking.project_walking.controller;

import com.walking.project_walking.domain.dto.*;
import com.walking.project_walking.service.BoardService;
import com.walking.project_walking.service.PostsService;
import com.walking.project_walking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostsController {
    private final PostsService postsService;
    private final BoardService boardService;
    private final UserService userService;

    private static final int PAGE_SIZE = 6;

    // 제목, 글쓴이, 내용을 조합하여 특정 게시글 조회
    @GetMapping("/search")
    public ResponseEntity<PostSearchResultDto> searchPosts(
            @RequestParam Long boardId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String nickname,
            @RequestParam(defaultValue = "1") int page) {

        if (!boardService.existsById(boardId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }

        PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
        List<PostResponseDto> postList = postsService.searchPosts(boardId, title, content, nickname, pageRequest);

        if (postList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        int totalPages = postsService.getTotalPages(boardId, title, content, nickname, PAGE_SIZE);

        if (page < 1 || page > totalPages) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        PostSearchResultDto result = new PostSearchResultDto(postList, totalPages);
        return ResponseEntity.ok(result);
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
    @GetMapping
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

    //게시글 생성 (작성자만 가능)
    @PostMapping
    public ResponseEntity<PostCreateResponseDto> savePosts(@Valid @RequestBody PostRequestDto postRequestDto) {
        PostCreateResponseDto savedPost = postsService.savePost(postRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }


    // 게시글 수정 (작성자만 가능)
    @PutMapping("/{postId}")
    public ResponseEntity<PostCreateResponseDto> modifyPosts(@PathVariable Long postId, @RequestParam Long userId, @Valid @RequestBody PostRequestDto postRequestDto) {
        PostCreateResponseDto updatedPost = postsService.modifyPost(postId, userId, postRequestDto);
        return ResponseEntity.ok(updatedPost);
    }


    // 게시글 삭제 (작성자만 가능)
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePosts(@PathVariable Long postId, @RequestParam Long userId) {
        postsService.deletePost(postId, userId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }


}
