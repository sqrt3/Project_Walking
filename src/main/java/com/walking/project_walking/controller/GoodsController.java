package com.walking.project_walking.controller;

import com.walking.project_walking.domain.dto.GoodsResponseDto;
import com.walking.project_walking.service.GoodsService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    private final GoodsService goodsService;

    @GetMapping
    public List<GoodsResponseDto> getAllGoods() {
        return goodsService.getAllGoods();
    }

    @GetMapping("/{goodsId}")
    public ResponseEntity<GoodsResponseDto> getGoods(@PathVariable Long goodsId) {
        GoodsResponseDto goods = goodsService.getGoodsById(goodsId);
        return ResponseEntity.ok(goods);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<GoodsResponseDto> addGoods(@RequestBody GoodsResponseDto goods) {
        goodsService.addGoods(goods);
        return ResponseEntity.status(HttpStatus.CREATED).body(goods);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{goodsId}")
    public ResponseEntity<GoodsResponseDto> updateGoods(@RequestBody GoodsResponseDto goods,
            @PathVariable Long goodsId) {
        GoodsResponseDto newGoods = goodsService.updateGoods(goodsId, goods);
        if (newGoods != null) {
            return ResponseEntity.status(HttpStatus.OK).body(newGoods);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{goodsId}")
    public ResponseEntity<String> deleteGoods(@PathVariable Long goodsId) {
        Boolean isGoodsExists = goodsService.deleteGoods(goodsId);
        if (isGoodsExists == Boolean.FALSE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(goodsId + "에 해당하는 굿즈가 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("굿즈가 삭제 되었습니다.\nID: " + goodsId);
    }

    @PostMapping("/{goodsId}/purchase")
    public ResponseEntity<String> purchaseGoods(@PathVariable Long goodsId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        Boolean isSuccessful = goodsService.purchaseGoods(goodsId, userId);
        if (isSuccessful == Boolean.FALSE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("포인트가 충분하지 않거나, 구매할 수 없는 굿즈 입니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("굿즈를 성공적으로 구매하셨습니다.");
    }

    @GetMapping("/{goodsId}/description")
    public ResponseEntity<String> getGoodsDescription(@PathVariable Long goodsId) {
        GoodsResponseDto goods = goodsService.getGoodsById(goodsId);
        if (goods.getGoodsId() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(goods.getDescription());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(goodsId + "에 해당하는 굿즈가 없습니다.");
    }

    @PostMapping("/{goodsId}/gift")
    public ResponseEntity<String> giftGoods(@PathVariable Long goodsId,
            @RequestParam String targetNickname, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        Boolean isSuccessful = goodsService.giftGoods(goodsId, userId, targetNickname);
        if (isSuccessful == Boolean.FALSE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("선물을 보낼 수 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(targetNickname + "님에게 선물을 보냈습니다.");
    }
}
