package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Goods;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.UserResponse;
import com.walking.project_walking.service.GoodsService;
import com.walking.project_walking.service.UserService;
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
    public String goods(Model model) {
        List<Goods> goodsList = goodsService.getAllGoods();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Users currentUser) {
            UserResponse userResponse = new UserResponse(userService.findById(currentUser.getUserId()));
            model.addAttribute("user", userResponse);
        }
        model.addAttribute("goodsList", goodsList);
        return "goods";
    }
}
