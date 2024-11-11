package com.walking.project_walking.service;

import com.walking.project_walking.domain.LikeLog;
import com.walking.project_walking.domain.Posts;
import com.walking.project_walking.domain.dto.*;
import com.walking.project_walking.repository.*;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Builder
@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final PostImagesRepository postImagesRepository;
    private final UserLikeLogRepository userLikeLogRepository;

    private static final double THRESHOLD = 100.0;

    // 특정 게시판, 특정 페이지의 게시물을 가져오는 메소드 (가져오는 게시글 갯수는 6개로 정의)
    public List<PostResponseDto> getPostsByBoardId(Long boardId, PageRequest pageRequest) {

        return postsRepository.findByBoardId(boardId, pageRequest)
                .map(post -> {
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(post.getPostId());
                    String postNickname = userRepository.getNicknameByUserId(post.getUserId());
                    List<String> imageUrl = postImagesRepository.findImageUrlsByPostId(post.getPostId());
                    return PostResponseDto.fromEntity(post, commentsNumber, postNickname, imageUrl);
                }).toList();
    }

    // 공지사항만 불러오는 메소드 (2개)
    public List<NoticeResponseDto> getNoticePosts(PageRequest pageRequest) {
        return postsRepository.findByBoardId(1L, pageRequest)
                .map(NoticeResponseDto::fromEntity).toList();

    }

    // boardId와 제목, 내용, 글쓴이를 통해 특정 게시물을 조회하는 메소드
    public List<PostResponseDto> searchPosts(Long boardId, String title, String content, String nickname, PageRequest pageRequest) {
        Long userId = userRepository.getUserIdByNickname(nickname);
        Page<Posts> postsPage = postsRepository.searchPosts(boardId, title, content, userId, pageRequest);
        return postsPage.getContent().stream()
                .map(post -> {
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(post.getPostId());
                    String postNickname = userRepository.getNicknameByUserId(post.getUserId());
                    List<String> imageUrl = postImagesRepository.findImageUrlsByPostId(post.getPostId());
                    return PostResponseDto.fromEntity(post, commentsNumber, postNickname, imageUrl);
                })
                .toList();
    }

    // 전체 게시판의 게시물 중 인기 게시글(weightValue가 특정 임계값을 넘은 게시글)을 모두 조회하는 메소드
    public List<PostResponseDto> getHotPosts() {
        List<Posts> hotPosts = postsRepository.findHotPosts(THRESHOLD);
        return hotPosts.stream()
                .map(post -> {
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(post.getPostId());
                    String postNickname = userRepository.getNicknameByUserId(post.getUserId());
                    List<String> imageUrl = postImagesRepository.findImageUrlsByPostId(post.getPostId());
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
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(post.getPostId());
                    String postNickname = userRepository.getNicknameByUserId(post.getUserId());
                    List<String> imageUrl = postImagesRepository.findImageUrlsByPostId(post.getPostId());
                    return PostResponseDto.fromEntity(post, commentsNumber, postNickname, imageUrl);
                })
                .orElse(null);
    }

    // 특정 유저가 작성한 게시글을 조회하는 메소드
    public List<PostSummuryResponseDto> getPostsByUserId(Long userId) {
        List<Posts> posts = postsRepository.findByUserId(userId);
        return posts.stream()
                .map(post -> {
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(post.getPostId());
                    return PostSummuryResponseDto.fromEntity(post, commentsNumber);
                })
                .toList();
    }

    // 검색 시 결과의 페이지를 구하는 메소드
    public int getTotalPages(Long boardId, String title, String content, String nickname, int pageSize) {
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
                .build();

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

    //게시글 수정 (작성자만 가능)
    public PostCreateResponseDto modifyPost(Long postId, Long userId, PostRequestDto postRequestDto) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());

        Posts updatedPost = postsRepository.save(post);

        return new PostCreateResponseDto(
                updatedPost.getPostId(),
                updatedPost.getUserId(),
                updatedPost.getBoardId(),
                updatedPost.getTitle(),
                updatedPost.getContent(),
                updatedPost.getViewCount(),
                updatedPost.getCreatedAt(),
                updatedPost.getModifiedAt(),
                updatedPost.getIsDeleted()
        );
    }

    //게시글 삭제 (작성자만 가능)
    public void deletePost(Long postId, Long userId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        post.setIsDeleted(true);
        postsRepository.save(post);

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
