package com.walking.project_walking.controller;

import com.walking.project_walking.domain.dto.BoardResponseDto;
import com.walking.project_walking.domain.dto.PostResponseDto;
import com.walking.project_walking.service.BoardService;
import com.walking.project_walking.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardViewController {

    private final BoardService boardService;
    private final PostsService postsService;

    private static final int PAGE_SIZE = 6;

    @GetMapping("/board/{boardId}")
    public String getBoard(Model model, @PathVariable Long boardId, @RequestParam(defaultValue = "1") int page) {
        List<BoardResponseDto> boardList = boardService.getBoardsList();
        model.addAttribute("boardList",boardList);
        model.addAttribute("boardId", boardId);

        PostResponseDto hotPost = postsService.getOneHotPost(boardId);
        model.addAttribute("hotPost", hotPost);

        List<PostResponseDto> postsList = postsService.getPostsByBoardId(boardId, PageRequest.of(page - 1, PAGE_SIZE));
        model.addAttribute("postsList", postsList);

        model.addAttribute("pageCount",boardService.getPageCount(boardId));

        // 현재 페이지 계산
        int currentPage = boardService.getCurrentPage(boardId, page);
        model.addAttribute("currentPage", currentPage);

        return "example";
    }
}
