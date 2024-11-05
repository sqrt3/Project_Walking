package com.walking.project_walking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter // <- 필요할 시 활성화
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "goods")
public class Goods {
    @Id
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "description", nullable = false, length = 256)
    private String description;

    @Column(name = "price", nullable = false)
    private Integer price;

}

