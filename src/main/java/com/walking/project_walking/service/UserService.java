package com.walking.project_walking.service;

import com.walking.project_walking.domain.MyGoods;
import com.walking.project_walking.domain.PointLog;
import com.walking.project_walking.domain.RecentPost;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.userdto.*;

import com.walking.project_walking.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PointLogRepository pointLogRepository;
    private final MyGoodsRepository myGoodsRepository;
    private final JavaMailSender mailSender;
    private final TokenService tokenService;
    private final RecentPostRepository recentPostRepository;

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

        String token = tokenService.createToken(user.getEmail());
        String recoveryLink = "http://yourdomain.com/auth/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("비밀번호 재설정");
        message.setText("비밀번호를 재설정하려면 아래 링크를 클릭하세요:\n" + recoveryLink);
        mailSender.send(message);
    }

    // 토큰으로 유저 검증
    public Users findUserByToken(String token) {
        String email = tokenService.extractEmail(token);
        if (email == null || !tokenService.isTokenValid(token, email)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
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
    public ResponseEntity<String> updateById(Long id, UserUpdate update) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 클라이언트가 보낸 데이터가 있을 때만 해당 필드를 업데이트
        if (update.getPassword() != null && !update.getPassword().isEmpty()) {
            users.setPassword(update.getPassword());
        }
        if (update.getPhone() != null && !update.getPhone().isEmpty()) {
            users.setPhone(update.getPhone());
        }
        if (update.getNickname() != null && !update.getNickname().isEmpty()) {
            users.setNickname(update.getNickname());
        }
        if (update.getProfileImage() != null && !update.getProfileImage().isEmpty()) {
            users.setProfileImage(update.getProfileImage());
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

    // mypage에서 정보 반환
    public UserPageDto getInfo(Long userId) {
        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Long followerCount = followRepository.countFollowers(userId);
        Long followingCount = followRepository.countFollowing(userId);

        return new UserPageDto(users, followerCount, followingCount);
    }

    // 사용자 포인트 조회 서비스
    public List<PointLog> getPointLog(Long userId) {
        return pointLogRepository.findByUserId(userId);
    }

    // 사용자 아이템 조회 서비스
    public List<MyGoods> getGoods(Long userId) {
        List<MyGoods> myGoods = myGoodsRepository.findByUserId(userId);
        if (myGoods.isEmpty()) {
            System.out.println("등록된 아이템이 없습니다.");
        }
        return myGoods;
    }

    // 사용자 최근 게시물 조회

    public Long getLastViewPost(Long userId) {
        Optional<RecentPost> recentPost = recentPostRepository.findPostById(userId);

        // 최근 조회한 게시물이 없으면 null 반환
        return recentPostRepository.findPostById(userId)
                .map(RecentPost::getPostId)
                .orElse(null);
    }
}
