package com.walking.project_walking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

@Entity
@Getter
@Setter // <- 필요할 시 활성화
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "my_goods")
public class MyGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    public MyGoods(Long users, Long goodsId, Integer amount) {
        this.setUserId(users);
        this.setGoodsId(goodsId);
        this.amount = amount;
    }
}
