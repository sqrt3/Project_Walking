package com.walking.project_walking.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
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
