package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Goods;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.dto.BoardResponseDto;
import com.walking.project_walking.service.BoardService;
import com.walking.project_walking.service.GoodsService;
import com.walking.project_walking.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminViewController {
    private final UserService userService;
    private final BoardService boardService;
    private final GoodsService goodsService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public String adminView(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Users currentUser) {
            Users user = userService.findById(currentUser.getUserId());
            model.addAttribute("user", user);
        }

        return "admin";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<Users> usersList = userService.findAll().stream().toList();
        model.addAttribute("userList", usersList);
        return "users";
    }

    @GetMapping("/users/{userId}")
    public String manageUser(@PathVariable Long userId, Model model) {
        Users user = userService.findById(userId);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/board")
    public String manageBoard(Model model) {
        List<BoardResponseDto> boardList = boardService.getBoardsList();
        model.addAttribute("boardList", boardList);
        return "board";
    }

    @GetMapping("/goods")
    public String manageGoods(Model model) {
        List<Goods> goodsList = goodsService.getAllGoods();
        model.addAttribute("goodsList", goodsList);
        return "goods-manager";
    }
}
