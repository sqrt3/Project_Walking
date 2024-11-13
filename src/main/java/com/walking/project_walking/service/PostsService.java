package com.walking.project_walking.service;

import com.walking.project_walking.domain.LikeLog;
import com.walking.project_walking.domain.PostImages;
import com.walking.project_walking.domain.Posts;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.dto.*;
import com.walking.project_walking.enums.Exp;
import com.walking.project_walking.enums.Point;
import com.walking.project_walking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.walking.project_walking.domain.dto.NoticeResponseDto;
import com.walking.project_walking.domain.dto.PostCreateResponseDto;
import com.walking.project_walking.domain.dto.PostRequestDto;
import com.walking.project_walking.domain.dto.PostResponseDto;
import com.walking.project_walking.domain.dto.PostSummuryResponseDto;
import com.walking.project_walking.repository.CommentsRepository;
import com.walking.project_walking.repository.PostImagesRepository;
import com.walking.project_walking.repository.PostsRepository;
import com.walking.project_walking.repository.UserLikeLogRepository;
import com.walking.project_walking.repository.UserRepository;


@Service
@RequiredArgsConstructor

public class PostsService {
    private final PostsRepository postsRepository;
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final PostImagesRepository postImagesRepository;
    private final UserLikeLogRepository userLikeLogRepository;
    private final ImageService imageService;
    private final PointService pointService;

    private static final double THRESHOLD = 100.0;

    // 특정 게시판, 특정 페이지의 게시물을 가져오는 메소드 (가져오는 게시글 갯수는 6개로 정의)
    public List<PostResponseDto> getPostsByBoardId(Long boardId, PageRequest pageRequest) {

        return postsRepository.findByBoardId(boardId, pageRequest)
                .map(post -> {
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(
                            post.getPostId());
                    String postNickname = userRepository.getNicknameByUserId(post.getUserId());
                    List<String> imageUrl = postImagesRepository.findImageUrlsByPostId(
                            post.getPostId());
                    return PostResponseDto.fromEntity(post, commentsNumber, postNickname, imageUrl);
                }).toList();
    }

    // 공지사항만 불러오는 메소드 (2개)
    public List<NoticeResponseDto> getNoticePosts(PageRequest pageRequest) {
        return postsRepository.findByBoardId(1L, pageRequest)
                .map(NoticeResponseDto::fromEntity).toList();

    }

    // boardId와 제목, 내용, 글쓴이를 통해 특정 게시물을 조회하는 메소드
    public List<PostResponseDto> searchPosts(Long boardId, String title, String content,
            String nickname, PageRequest pageRequest) {
        Long userId = userRepository.getUserIdByNickname(nickname);
        Page<Posts> postsPage = postsRepository.searchPosts(boardId, title, content, userId,
                pageRequest);
        return postsPage.getContent().stream()
                .map(post -> {
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(
                            post.getPostId());
                    String postNickname = userRepository.getNicknameByUserId(post.getUserId());
                    List<String> imageUrl = postImagesRepository.findImageUrlsByPostId(
                            post.getPostId());
                    return PostResponseDto.fromEntity(post, commentsNumber, postNickname, imageUrl);
                })
                .toList();
    }

    // 전체 게시판의 게시물 중 인기 게시글(weightValue가 특정 임계값을 넘은 게시글)을 모두 조회하는 메소드
    public List<PostResponseDto> getHotPosts() {
        List<Posts> hotPosts = postsRepository.findHotPosts(THRESHOLD);
        return hotPosts.stream()
                .map(post -> {
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(
                            post.getPostId());
                    String postNickname = userRepository.getNicknameByUserId(post.getUserId());
                    List<String> imageUrl = postImagesRepository.findImageUrlsByPostId(
                            post.getPostId());
                    return PostResponseDto.fromEntity(post, commentsNumber, postNickname, imageUrl);
                })
                .toList();
    }

    // 특정 게시판 중 가장 인기 있는(weightValue 값이 가장 큰) 게시물을 조회하는 메소드
    public PostResponseDto getOneHotPost(Long boardId) {
        List<Posts> hotPosts = postsRepository.findHotPosts(THRESHOLD);

        return hotPosts.stream()
                .filter(post -> post.getBoardId().equals(boardId))
                .max(Comparator.comparing(Posts::getWeightValue))
                .map(post -> {
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(
                            post.getPostId());
                    String postNickname = userRepository.getNicknameByUserId(post.getUserId());
                    List<String> imageUrl = postImagesRepository.findImageUrlsByPostId(
                            post.getPostId());
                    return PostResponseDto.fromEntity(post, commentsNumber, postNickname, imageUrl);
                })
                .orElse(null);
    }

    // 특정 유저가 작성한 게시글을 조회하는 메소드
    public List<PostSummuryResponseDto> getPostsByUserId(Long userId) {
        List<Posts> posts = postsRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return posts.stream()
                .map(post -> {
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(
                            post.getPostId());
                    return PostSummuryResponseDto.fromEntity(post, commentsNumber);
                })
                .toList();
    }

    // 검색 시 결과의 페이지를 구하는 메소드
    public int getTotalPages(Long boardId, String title, String content, String nickname,
            int pageSize) {
        Long userId = userRepository.getUserIdByNickname(nickname);
        long totalPosts = postsRepository.countBySearchCriteria(boardId, title, content, userId);
        return (int) Math.ceil((double) totalPosts / pageSize);
    }

    //게시글 생성
    public PostCreateResponseDto savePost(PostRequestDto postRequestDto) {
        Posts post = Posts.builder()
                .userId(postRequestDto.getUserId())
                .boardId(postRequestDto.getBoardId())
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .viewCount(0)
                .likes(0)
                .weightValue(0)
                .isDeleted(false)
                .build();

        Users user = userRepository.findById(post.getUserId()).orElseThrow(() -> new IllegalArgumentException("올바르지 않은 유저 ID 입니다."));
        if (user.getRole().name().equals("ROLE_USER")) {
            user.setUserExp(user.getUserExp() + Exp.WRITE_ARTICLE_POINT.getAmount());
            user.setPoint(user.getPoint() + Point.WRITE_ARTICLE_POINT.getAmount());
            pointService.addPoints(user.getUserId(), Point.WRITE_ARTICLE_POINT.getAmount(), "게시글 작성으로 포인트 지급");
        } else {
            user.setUserExp(user.getUserExp() + Exp.WRITE_ARTICLE_POINT.getAmount() * 2);
            user.setPoint(user.getPoint() + Point.WRITE_ARTICLE_POINT.getAmount() * 2);
            pointService.addPoints(user.getUserId(), Point.WRITE_ARTICLE_POINT.getAmount() * 2, "게시글 작성으로 포인트 지급");
        }
        // 게시글 저장
        Posts savedPost = postsRepository.save(post);

        return new PostCreateResponseDto(
                savedPost.getPostId(),
                savedPost.getUserId(),
                savedPost.getBoardId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getViewCount(),
                savedPost.getCreatedAt(),
                savedPost.getModifiedAt(),
                savedPost.getIsDeleted()
        );
    }

    // S3에 파일을 업로드하는 메서드
    public void uploadFileToS3(Long post_id, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            PostImages postImage = new PostImages(post_id, imageService.upload(file));
            postImagesRepository.save(postImage);
        }
    }

    //게시글 수정 (작성자만 가능, 기본 이미지 유지/삭제 가능)
    public void modifyPost(Long postId, Long userId, PostRequestDto postRequestDto,
                                            List<MultipartFile> files) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setBoardId(postRequestDto.getBoardId());
        post.setUserId(postRequestDto.getUserId());
        post.setModifiedAt(LocalDateTime.now()); // 수정 시간 업데이트


        // 새 파일 업로드 처리
        if (files != null && !files.isEmpty()) {
            uploadFileToS3(postId, files); // 파일 업로드 메서드 호출
        }

        postsRepository.save(post);

    }

    //게시글 삭제 (작성자만 가능)
    public void deletePost(Long postId, Long userId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저 ID를 찾을 수 없습니다."));
        if (!post.getUserId().equals(userId) && !user.getRole().name()
                .equals("ROLE_ADMIN")) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        post.setIsDeleted(true);
        postsRepository.save(post);
    }


    //게시글 상세 조회

    public PostResponseDto getPostById(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        Integer commentsNumber = commentsRepository.countCommentsByPostId(postId);
        String postNickname = userRepository.getNicknameByUserId(post.getUserId());
        List<String> imageUrl = postImagesRepository.findImageUrlsByPostId(postId);

        return PostResponseDto.fromEntity(post, commentsNumber, postNickname, imageUrl);
    }
  
    // 유저가 해당 게시글에 좋아요를 눌렀는지 확인하는 메소드
    public boolean hasLiked(Long userId, Long postId) {
        return userLikeLogRepository.findByUserIdAndPostId(userId, postId).isPresent();
    }

    // 좋아요 버튼을 클릭 시
    @Transactional
    public void likePost(Long userId, Long postId) {
        // 좋아요 로그 확인
        boolean hasLiked = hasLiked(userId, postId);

        // 게시글 조회
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (hasLiked) {
            // 유저가 이미 좋아요를 눌렀다면, 좋아요 로그 삭제하고 게시글 좋아요 수 감소
            userLikeLogRepository.deleteByUserIdAndPostId(userId, postId);  // 좋아요 로그 삭제
            post.setLikes(post.getLikes() - 1);  // 게시글 좋아요 수 감소
        } else {
            // 유저가 좋아요를 안 눌렀다면, 새로운 좋아요 로그 추가하고 게시글 좋아요 수 증가
            LikeLog newLikeLog = new LikeLog();
            newLikeLog.setUserId(userId);
            newLikeLog.setPostId(postId);
            userLikeLogRepository.save(newLikeLog);  // 새로운 좋아요 로그 추가
            post.setLikes(post.getLikes() + 1);  // 게시글 좋아요 수 증가
        }

        // 게시글 정보 저장
        postsRepository.save(post);  // 게시글 저장
    }

}