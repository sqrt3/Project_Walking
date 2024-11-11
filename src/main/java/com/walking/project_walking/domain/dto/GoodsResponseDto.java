package com.walking.project_walking.domain.dto;

import com.walking.project_walking.domain.Goods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class GoodsResponseDto {
    private Long goodsId;
    private String name;
    private String description;
    private Integer price;
    private String goodsImage;

    public GoodsResponseDto(Goods goods) {
        this.goodsId = goods.getGoodsId();
        this.name = goods.getName();
        this.description = goods.getDescription();
        this.price = goods.getPrice();
        this.goodsImage = goods.getGoodsImage();
    }

    public Goods toEntity() {
        return new Goods(this.getGoodsId(), this.getName(), this.getDescription(), this.getPrice(), this.getGoodsImage());
    }
}
