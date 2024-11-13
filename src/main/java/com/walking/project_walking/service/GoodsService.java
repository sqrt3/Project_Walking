package com.walking.project_walking.service;

import com.walking.project_walking.domain.Goods;
import com.walking.project_walking.domain.MyGoods;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.dto.GoodsResponseDto;
import com.walking.project_walking.repository.GoodsRepository;
import com.walking.project_walking.repository.MyGoodsRepository;
import com.walking.project_walking.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoodsService {

  private final GoodsRepository goodsRepository;
  private final UserRepository userRepository;
  private final MyGoodsRepository myGoodsRepository;
  private final PointService pointService;
  private final ImageService imageService;

  public List<GoodsResponseDto> getAllGoods() {
    List<Goods> goodsList = goodsRepository.findAll();
    return goodsList.stream()
        .map(GoodsResponseDto::new)
        .collect(Collectors.toList());
  }

  public GoodsResponseDto getGoodsById(Long id) {
    Goods goods = goodsRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당하는 굿즈의 ID가 없습니다."));
    return new GoodsResponseDto(goods);
  }

  public GoodsResponseDto addGoods(GoodsResponseDto goods) {
    Goods savedGoods = goodsRepository.save(goods.toEntity());
    return new GoodsResponseDto(savedGoods);
  }

  @Transactional
  public GoodsResponseDto updateGoods(Long orgGoodsId, GoodsResponseDto newGoods) {
    GoodsResponseDto orgGoods = getGoodsById(orgGoodsId);
    orgGoods.setName(newGoods.getName());
    orgGoods.setDescription(newGoods.getDescription());
    orgGoods.setPrice(newGoods.getPrice());
    goodsRepository.save(orgGoods.toEntity());
    return orgGoods;
  }

  @Transactional
  public Boolean deleteGoods(Long id) {
    GoodsResponseDto goods = getGoodsById(id);
    if (goods != null) {
      goodsRepository.deleteById(id);
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  @Transactional
  public Boolean purchaseGoods(Long goodsId, Long userId) {
    Users user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 유저 ID 입니다."));
    if (user == null)                       // 유저가 null 일때
    {
      return Boolean.FALSE;
    }

    GoodsResponseDto goods = getGoodsById(goodsId);
    if (goods == null) {
      return Boolean.FALSE;
    }
    if (user.getPoint() < goods.getPrice()) // 유저의 포인트보다 굿즈의 가격이 높을 때
    {
      return Boolean.FALSE;
    }

    if (goodsId / 100000 == 3 && user.getRole().name().equals("ROLE_USER") || user.getRole().name()
        .equals("PATHFINDER")) {
      return Boolean.FALSE;
    }

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
    pointService.deductPoints(user.getUserId(), goods.getPrice(), "아이템 구매로 인한 포인트 차감");
    userRepository.save(user);
    return Boolean.TRUE;
  }

  @Transactional
  public Boolean giftGoods(Long goodsId, Long userId, String targetUserNickname) {
    Users user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 유저 ID 입니다."));
    Long targetUserId = userRepository.getUserIdByNickname(targetUserNickname);

    if (targetUserId == null) {
      return Boolean.FALSE;
    }

    Users targetUser = userRepository.findById(targetUserId)
        .orElseThrow(() -> new IllegalArgumentException("해당 닉네임을 가진 유저가 없습니다."));

    if (user == null || targetUser == null)                       // 유저가 null 일때
    {
      return Boolean.FALSE;
    }

    if (!user.getRole().name().equals("PIONEER") && !user.getRole().name().equals("ROLE_ADMIN")) {
      return Boolean.FALSE;
    }

    GoodsResponseDto goods = getGoodsById(goodsId);
    if (goods == null) {
      return Boolean.FALSE;
    }
    if (user.getPoint() < goods.getPrice()) // 유저의 포인트보다 굿즈의 가격이 높을 때
    {
      return Boolean.FALSE;
    }

    user.setPoint(user.getPoint() - goods.getPrice());
    Boolean isExists = myGoodsRepository.existsByUserIdAndGoodsId(targetUser.getUserId(),
        goodsId);
    if (isExists == Boolean.TRUE)          // 유저가 굿즈를 이미 갖고있다면
    {
      MyGoods myGoods = myGoodsRepository.findByUserIdAndGoodsId(targetUser.getUserId(),
          goodsId);
      myGoods.setAmount(myGoods.getAmount() + 1);
      myGoodsRepository.save(myGoods);
    } else {
      MyGoods myGoods = new MyGoods(targetUser.getUserId(), goodsId, 1);
      myGoodsRepository.save(myGoods);
    }
    pointService.deductPoints(user.getUserId(), goods.getPrice(), "아이템 선물로 인한 포인트 차감");
    userRepository.save(user);
    return Boolean.TRUE;
  }
}