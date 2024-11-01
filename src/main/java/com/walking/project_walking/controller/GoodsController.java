package com.walking.project_walking.controller;

import com.walking.project_walking.domain.Goods;
import com.walking.project_walking.service.GoodsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    private final GoodsService goodsService;

    @GetMapping
    public List<Goods> getAllGoods() {
        return goodsService.getAllGoods();
    }

    // Admin Only
    @PostMapping
    public ResponseEntity<String> addGoods(@RequestBody Goods goods) {
        goodsService.addGoods(goods);
        return ResponseEntity.status(HttpStatus.CREATED).body("굿즈가 추가되었습니다.\nID: " + goods.getGoodsId());
    }

    // Admin Only
    @PutMapping("/{goodsId}")
    public ResponseEntity<String> updateGoods(@RequestBody Goods goods, @PathVariable Long goodsId) {
        Goods newGoods = goodsService.updateGoods(goodsId, goods);
        if (newGoods != null)
            return ResponseEntity.status(HttpStatus.OK).body(goods.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(goodsId + " 에 해당하는 굿즈가 없습니다.");
    }

    // Admin Only
    @DeleteMapping("/{goodsId}")
    public ResponseEntity<String> deleteGoods(@PathVariable Long goodsId) {
        Boolean isGoodsExists = goodsService.deleteGoods(goodsId);
        if (isGoodsExists == Boolean.FALSE)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(goodsId + "에 해당하는 굿즈가 없습니다.");
        return ResponseEntity.status(HttpStatus.OK).body("굿즈가 삭제 되었습니다.\nID: " + goodsId);
    }

    @PostMapping("/{goodsId}/{userId}/purchase")
    public ResponseEntity<String> purchaseGoods(@RequestBody Goods goods, @PathVariable Long goodsId, @PathVariable Long userId) {
        Boolean isSuccessful = goodsService.purchaseGoods(goodsId, userId);
        if (isSuccessful == Boolean.FALSE)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("포인트가 충분하지 않거나, 올바르지 않은 굿즈입니다.");
        return ResponseEntity.status(HttpStatus.OK).body("굿즈를 성공적으로 구매하셨습니다.");
    }

    @GetMapping("/{goodsId}/description")
    public ResponseEntity<String> getGoodsDescription(@PathVariable Long goodsId) {
        Goods goods = goodsService.getGoodsById(goodsId);
        if (goods.getGoodsId() != null)
            return ResponseEntity.status(HttpStatus.OK).body(goods.getDescription());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(goodsId + "에 해당하는 굿즈가 없습니다.");
    }
}
