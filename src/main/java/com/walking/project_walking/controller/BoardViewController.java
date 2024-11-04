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
    private static final int BLOCK_SIZE = 10; // 페이지 블록 크기

    @GetMapping("/board/{boardId}")
    public String getBoard(Model model, @PathVariable Long boardId,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(required = false) String searchCategory,
                           @RequestParam(required = false) String searchKeyword) {
        List<BoardResponseDto> boardList = boardService.getBoardsList();
        model.addAttribute("boardList", boardList);
        model.addAttribute("boardId", boardId);

        PostResponseDto hotPost = postsService.getOneHotPost(boardId);
        model.addAttribute("hotPost", hotPost);

        List<PostResponseDto> postsList;

        // 검색 조건이 있을 경우
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            switch (searchCategory) {
                case "title":
                    postsList = postsService.searchPosts(boardId, searchKeyword, null, null, PageRequest.of(0, PAGE_SIZE));
                    break;
                case "content":
                    postsList = postsService.searchPosts(boardId, null, searchKeyword, null, PageRequest.of(0, PAGE_SIZE));
                    break;
                case "title-content":
                    postsList = postsService.searchPosts(boardId, searchKeyword, searchKeyword, null, PageRequest.of(0, PAGE_SIZE));
                    break;
                default:
                    postsList = postsService.getPostsByBoardId(boardId, PageRequest.of(page - 1, PAGE_SIZE));
                    break;
            }
        } else {
            postsList = postsService.getPostsByBoardId(boardId, PageRequest.of(page - 1, PAGE_SIZE));
        }

        model.addAttribute("postsList", postsList);

        int pageCount = boardService.getPageCount(boardId);
        model.addAttribute("pageCount", pageCount);

        // 현재 페이지 계산
        int currentPage = boardService.getCurrentPage(boardId, page);
        model.addAttribute("currentPage", currentPage);


        int startPage = ((currentPage - 1) / BLOCK_SIZE) * BLOCK_SIZE + 1;
        int endPage = Math.min(startPage + BLOCK_SIZE - 1, pageCount);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "example";
    }
}
