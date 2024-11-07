package com.walking.project_walking.domain.dto;

import com.walking.project_walking.domain.Goods;
import lombok.Getter;

@Getter
public class GoodsResponseDto {
    private final Long goodsId;
    private final String name;
    private final String description;
    private final Integer price;

    public GoodsResponseDto(Goods goods) {
        this.goodsId = goods.getGoodsId();
        this.name = goods.getName();
        this.description = goods.getDescription();
        this.price = goods.getPrice();
    }
}
