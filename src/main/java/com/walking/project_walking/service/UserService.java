package com.walking.project_walking.service;

import com.walking.project_walking.domain.MyGoods;
import com.walking.project_walking.domain.PointLog;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.*;
import com.walking.project_walking.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PointLogRepository pointLogRepository;
    private final GoodsRepository goodsRepository;
    private final MyGoodsRepository myGoodsRepository;
    private final JavaMailSender mailSender;
    private final RecentPostRepository recentPostRepository;
    private final PostsRepository postsRepository;
    private final ImageService imageService;

    // 회원 가입
    @Transactional
    public Users saveUser(UserSignUpDto dto) {
        String encodedPassword = encoder.encode(dto.getPassword());

        Users user = Users.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .name(dto.getName())
                .phone(dto.getPhone())
                .nickname(dto.getNickname())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .joinDate(LocalDate.now())
                .build();

        return userRepository.save(user);
    }

    // 비밀번호 재설정 이메일 전송
    @Transactional
    public void sendPasswordRecoveryEmail(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String token = generateToken(user.getEmail());

        String recoveryLink = "http://yourdomain.com/auth/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("비밀번호 재설정");
        message.setText("비밀번호를 재설정하려면 아래 링크를 클릭하세요:\n" + recoveryLink);
        mailSender.send(message);
    }

    // UUID로 이메일 재설정 주소용 토큰 생성
    private String generateToken(String email) {
        String uuid = UUID.randomUUID().toString();
        long expiryTime = System.currentTimeMillis() + 1000 * 60 * 60;
        String token = uuid + ":" + email + ":" + expiryTime;
        return token;
    }

    public boolean validateToken(String token, String email) {
        try {
            // 토큰을 ":"으로 분리
            String[] parts = token.split(":");
            if (parts.length != 3) {
                return false; // 잘못된 토큰 형식
            }

            String uuid = parts[0];      // UUID
            String tokenEmail = parts[1]; // 이메일
            long expiryTime = Long.parseLong(parts[2]); // 만료 시간

            // 토큰 만료 시간 검사
            if (expiryTime < System.currentTimeMillis()) {
                return false; // 토큰이 만료됨
            }

            // 이메일 검사
            if (!email.equals(tokenEmail)) {
                return false; // 이메일 불일치
            }

            return true; // 모든 검증을 통과하면 유효한 토큰
        } catch (Exception e) {
            return false; // 예외가 발생하면 유효하지 않은 토큰으로 간주
        }
    }

    // 이메일 등록 여부 확인
    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // 닉네임 등록 여부 확인
    public boolean checkNicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }


    // 유저 정보 전체 조회
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    // 유저 한 명 조회
    public Users findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    // 실제 존재하는 userId인지 검증하는 메소드
    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    // 유저 정보 수정
    @Transactional
    public ResponseEntity<String> updateById(Long id, UserUpdate update, MultipartFile profileImage) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 클라이언트가 보낸 데이터가 있을 때만 해당 필드를 업데이트
        if (update.getPassword() != null && !update.getPassword().isEmpty()) {
            String encodedPassword = encoder.encode(update.getPassword());
            users.setPassword(encodedPassword);
        }
        if (update.getPhone() != null && !update.getPhone().isEmpty()) {
            users.setPhone(update.getPhone());
        }
        if (update.getNickname() != null && !update.getNickname().isEmpty()) {
            users.setNickname(update.getNickname());
        }
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = imageService.upload(profileImage);  // S3에 파일 업로드 후 URL 반환
            users.setProfileImage(imageUrl);
        }
        userRepository.save(users);
        return new ResponseEntity<>("수정이 완료되었습니다 :)", HttpStatus.OK);
    }

    // 유저 soft delete
    @Transactional
    public void softDeleteUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        user.deactivate();
        userRepository.save(user);
    }

    // (Users) 유저 1명 조회
    public UserDetailDto userDetail(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Long followerCount = followRepository.countFollowers(userId);
        Long followingCount = followRepository.countFollowing(userId);

        return new UserDetailDto(user.getNickname(), user.getProfileImage(), followerCount, followingCount);
    }

    private final String defaultProfileImageUrl = "https://walkingproject.s3.ap-northeast-2.amazonaws.com/95360af5-04.jpg";
    // mypage에서 정보 반환
    public UserPageDto getInfo(Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Long followerCount = followRepository.countFollowers(userId);
        Long followingCount = followRepository.countFollowing(userId);

        String profileImage = users.getProfileImage() != null? users.getProfileImage() : defaultProfileImageUrl;

        return new UserPageDto(users, followerCount, followingCount, profileImage);
    }

    // 사용자 포인트 조회 서비스
    public List<PointLog> getPointLog(Long userId) {
        return pointLogRepository.findByUserId(userId);
    }

    // 사용자 아이템 조회 서비스
    public List<UserGoodsDto> getGoods(Long userId) {
        List<MyGoods> myGoodsList = myGoodsRepository.findByUserId(userId);
        List<UserGoodsDto> myGoodsDtos = new ArrayList<>();

        for (MyGoods myGoods : myGoodsList) {
            String name = goodsRepository.findNameByGoodsId(myGoods.getGoodsId());
            UserGoodsDto myGoodsDto = new UserGoodsDto(myGoods.getGoodsId(), name, myGoods.getAmount());
            myGoodsDtos.add(myGoodsDto);
        }

        if (myGoodsDtos.isEmpty()) {
            System.out.println("등록된 아이템이 없습니다.");
        }
        return myGoodsDtos;
    }

    // 아이템 사용
    @Transactional
    public void useItem(Long userId, Long goodsId) {
        // userId와 goodsId로 MyGoods 조회
        MyGoods myGoods = myGoodsRepository.findByUserIdAndGoodsId(userId, goodsId);

        if (myGoods == null) {
            throw new IllegalArgumentException("아이템이 존재하지 않습니다.");
        }

        if (myGoods.getAmount() > 1) {
            // 아이템 수량이 2개 이상일 때 수량 감소
            myGoods.setAmount(myGoods.getAmount() - 1);
        } else {
            // 아이템 수량이 1개 이하일 때 삭제
            myGoodsRepository.delete(myGoods);
        }
    }

    // 사용자 최근 게시물 조회
    public String getLastViewedPostTitle(Long userId) {
        // userId를 기준으로 postId를 조회
        Long postId = recentPostRepository.findPostIdByUserId(userId);

        if (postId != null) {
            return postsRepository.findTitleByPostId(postId);

        } else {
            return "최근 본 게시글이 없습니다";
        }
    }

}
