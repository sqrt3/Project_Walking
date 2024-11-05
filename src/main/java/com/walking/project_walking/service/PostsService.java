package com.walking.project_walking.service;

import com.walking.project_walking.domain.Posts;
import com.walking.project_walking.domain.dto.NoticeResponseDto;
import com.walking.project_walking.domain.dto.PostResponseDto;
import com.walking.project_walking.domain.dto.PostSummuryResponseDto;
import com.walking.project_walking.repository.CommentsRepository;
import com.walking.project_walking.repository.PostsRepository;
import com.walking.project_walking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;

    private static final double THRESHOLD = 100.0;

    // 특정 게시판, 특정 페이지의 게시물을 가져오는 메소드 (가져오는 게시글 갯수는 6개로 정의)
    public List<PostResponseDto> getPostsByBoardId(Long boardId, PageRequest pageRequest) {

        return postsRepository.findByBoardId(boardId, pageRequest)
                .map(post -> {
                    Integer commentsNumber = commentsRepository.countCommentsByPostId(post.getPostId());
                    String postNickname = userRepository.getNicknameByUserId(post.getUserId());
                    return PostResponseDto.fromEntity(post, commentsNumber, postNickname);
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
                    return PostResponseDto.fromEntity(post, commentsNumber, postNickname);
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
                    return PostResponseDto.fromEntity(post, commentsNumber, postNickname);
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
                    return PostResponseDto.fromEntity(post, commentsNumber, postNickname);
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
}
