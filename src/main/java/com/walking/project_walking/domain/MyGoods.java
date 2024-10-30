package com.walking.project_walking.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
//@Setter <- 필요할 시 활성화
@Table(name = "my_goods")
public class MyGoods {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    @Column(name = "amount", nullable = false)
    private Integer amount;

//    MyGoods (User user, Goods goods, int amount) {
//        this.userId = user.getUserId();
//        this.goodsId = goods.getGoodsId();
//        this.amount = amount;
//    }
}
