package com.walking.project_walking.domain.userdto;

import lombok.Getter;

@Getter
public class UserGoodsDto {
    private Long goodsId;
    private String name;
    private Integer amount;

    public UserGoodsDto(Long goodsId, String name, Integer amount) {
        this.goodsId = goodsId;
        this.name = name;
        this.amount = amount;
    }
}
