package com.walking.project_walking.controller;

import com.walking.project_walking.service.BoardsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class BoardsController {
    private final BoardsService boardsService;

    public BoardsController(BoardsService boardsService) {
        this.boardsService = boardsService;
    }

    @GetMapping("/{boardId}/pagescount")
    public int getPostsCount(@PathVariable Long boardId) {
        return boardsService.getPageCount(boardId);
    }
}
