package com.walking.project_walking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class BoardViewController {

    @GetMapping("/boardList")
    public String getBoard2(@RequestParam(defaultValue = "1") Long boardId,
                            Model model) {
        model.addAttribute("boardId", boardId);
        model.addAttribute("isNoticeBoard", boardId == 1L);
        return "board-list";
    }
}
