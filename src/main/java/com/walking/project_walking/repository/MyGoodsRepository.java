package com.walking.project_walking.repository;

import com.walking.project_walking.domain.MyGoods;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyGoodsRepository extends JpaRepository<MyGoods, Long> {

  Boolean existsByUserIdAndGoodsId(Long userId, Long goodsId);

  MyGoods findByUserIdAndGoodsId(Long userId, Long goodsId);

  List<MyGoods> findByUserId(Long userId);
}
