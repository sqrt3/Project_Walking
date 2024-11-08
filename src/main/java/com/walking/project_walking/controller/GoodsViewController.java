package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Goods;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.dto.GoodsResponseDto;
import com.walking.project_walking.domain.userdto.UserResponse;
import com.walking.project_walking.service.GoodsService;
import com.walking.project_walking.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/goods")
public class GoodsViewController {
    private final GoodsService goodsService;
    private final UserService userService;

    @GetMapping
    public String goods(Model model, HttpSession session) {
        List<GoodsResponseDto> goodsList = goodsService.getAllGoods();

        Long userId = (Long)session.getAttribute("userId");
        if (userId != null) {
            Users user = userService.findById(userId);
            model.addAttribute("user", user);
        }

        model.addAttribute("goodsList", goodsList);
        return "goods";
    }
}
