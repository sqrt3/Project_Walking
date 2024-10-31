package com.walking.project_walking.service;

import com.walking.project_walking.domain.Goods;
import com.walking.project_walking.domain.MyGoods;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.repository.GoodsRepository;
import com.walking.project_walking.repository.MyGoodsRepository;
import com.walking.project_walking.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final UserRepository userRepository;
    private final MyGoodsRepository myGoodsRepository;

    public GoodsService(GoodsRepository goodsRepository, UserRepository userRepository, MyGoodsRepository myGoodsRepository) {
        this.goodsRepository = goodsRepository;
        this.userRepository = userRepository;
        this.myGoodsRepository = myGoodsRepository;
    }

    public List<Goods> getAllGoods() {
        return goodsRepository.findAll();
    }

    public Goods getGoodsById(Long id) {
        return goodsRepository.findById(id).get();
    }

    public Goods addGoods(Goods goods) {
        return goodsRepository.save(goods);
    }

    @Transactional
    public Goods updateGoods(Long orgGoodsId, Goods newGoods) {
        Goods orgGoods = getGoodsById(orgGoodsId);
        orgGoods.setName(newGoods.getName());
        orgGoods.setDescription(newGoods.getDescription());
        orgGoods.setPrice(newGoods.getPrice());
        return goodsRepository.save(orgGoods);
    }

    @Transactional
    public Boolean deleteGoods(Long id) {
        Goods goods = getGoodsById(id);
        if (goods.getGoodsId() != null) {
            goodsRepository.deleteById(id);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional
    public Boolean purchaseGoods(Long userId, Long goodsId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null)                       // 유저가 null 일때
            return Boolean.FALSE;

        Goods goods = getGoodsById(goodsId);
        if (user.getPoint() < goods.getPrice()) // 유저의 포인트보다 굿즈의 가격이 높을 때
            return Boolean.FALSE;

        user.setPoint(user.getPoint() - goods.getPrice());
        Boolean isExists = myGoodsRepository.existsByUserIdAndGoodsId(userId, goodsId);
        if (isExists == Boolean.TRUE)          // 유저가 굿즈를 이미 갖고있다면
        {
            MyGoods myGoods = myGoodsRepository.findByUserIdAndGoodsId(userId, goodsId);
            myGoods.setAmount(myGoods.getAmount() + 1);
            myGoodsRepository.save(myGoods);
        } else {
            MyGoods myGoods = new MyGoods(userId, goodsId, 1);
            myGoodsRepository.save(myGoods);
        }
        userRepository.save(user);
        return Boolean.TRUE;
    }
}
