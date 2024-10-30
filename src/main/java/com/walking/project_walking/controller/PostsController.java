package com.walking.project_walking.controller;

import com.walking.project_walking.domain.dto.PostResponseDto;
import com.walking.project_walking.service.PostsService;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {
    private final PostsService postsService;

    public PostsController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping
    public List<PostResponseDto> getPostsByBoard(
            @RequestParam("board") Long boardId,
            @RequestParam(defaultValue = "1") int page) {

        PageRequest pageRequest = PageRequest.of(page - 1, 6);
        return postsService.getPostsByBoardId(boardId, pageRequest);
    }

    @GetMapping("/{boardId}/posts/search")
    public List<PostResponseDto> searchPosts(
            @PathVariable Long boardId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") int page) {

        PageRequest pageRequest = PageRequest.of(page - 1, 6);
        return postsService.searchPosts(boardId, title, content, userId, pageRequest);
    }

}
