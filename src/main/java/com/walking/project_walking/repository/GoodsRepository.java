package com.walking.project_walking.repository;

import com.walking.project_walking.domain.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {

  @Query("SELECT g.name FROM Goods g WHERE g.goodsId = :goodsId")
  String findNameByGoodsId(@Param("goodsId") Long goodsId);
}
