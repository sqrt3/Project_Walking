package com.walking.project_walking.controller;

import com.walking.project_walking.domain.dto.PostCreateResponseDto;
import com.walking.project_walking.domain.dto.PostRequestDto;
import com.walking.project_walking.domain.dto.PostResponseDto;
import com.walking.project_walking.domain.dto.PostSearchResultDto;
import com.walking.project_walking.domain.dto.PostSummuryResponseDto;
import com.walking.project_walking.repository.PostImagesRepository;
import com.walking.project_walking.service.BoardService;
import com.walking.project_walking.service.PostsService;
import com.walking.project_walking.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostsController {

  private final PostsService postsService;
  private final BoardService boardService;
  private final UserService userService;
  private final PostImagesRepository postImagesRepository;

  private static final int PAGE_SIZE = 6;

  // 제목, 글쓴이, 내용을 조합하여 특정 게시글 조회
  @GetMapping("/search")
  public ResponseEntity<PostSearchResultDto> searchPosts(
      @RequestParam Long boardId,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String content,
      @RequestParam(required = false) String nickname,
      @RequestParam(defaultValue = "1") int page) {

    if (!boardService.existsById(boardId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .build();
    }

    PageRequest pageRequest = PageRequest.of(page - 1, PAGE_SIZE);
    List<PostResponseDto> postList = postsService.searchPosts(boardId, title, content, nickname,
        pageRequest);

    if (postList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    int totalPages = postsService.getTotalPages(boardId, title, content, nickname, PAGE_SIZE);

    if (page < 1 || page > totalPages) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    PostSearchResultDto result = new PostSearchResultDto(postList, totalPages);
    return ResponseEntity.ok(result);
  }

  // 인기 게시판 전용
  // 인기글 조회 (전체)
  @GetMapping("/hot")
  public ResponseEntity<List<PostResponseDto>> getHotPosts() {
    List<PostResponseDto> postList = postsService.getHotPosts();

    if (postList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    return ResponseEntity.ok(postsService.getHotPosts());
  }

  // 인기글 조회 (1개)
  @GetMapping("/hot/{boardId}")
  public ResponseEntity<PostResponseDto> getOneHotPost(@PathVariable Long boardId) {
    if (!boardService.existsById(boardId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .build();
    }

    PostResponseDto hotPost = postsService.getOneHotPost(boardId);

    if (hotPost == null) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    return ResponseEntity.ok(hotPost);
  }

  // 유저가 작성한 게시글 목록 보기
  @GetMapping
  public ResponseEntity<List<PostSummuryResponseDto>> getUserPosts(@RequestParam Long userId) {
    if (!userService.existsById(userId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    List<PostSummuryResponseDto> userPosts = postsService.getPostsByUserId(userId);

    if (userPosts.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    return ResponseEntity.ok(userPosts);
  }

  //게시글 생성 (이미지 파일 업로드 포함)
  @PostMapping
  public ResponseEntity<?> savePosts(@RequestPart("postRequest") @Valid PostRequestDto postRequest
      , @RequestPart(value = "uploadFiles", required = false) List<MultipartFile> multipartFiles) {
    PostCreateResponseDto dto = postsService.savePost(postRequest);

    if (multipartFiles == null) {
      return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    if (!multipartFiles.isEmpty()) {
      postsService.uploadFileToS3(dto.getPostId(), multipartFiles);
    }
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  // 게시글 수정 (작성자만 가능)
  @PostMapping("/modify")
  public ResponseEntity<String> modifyPosts(
      @RequestParam Long postId,
      @RequestParam Long userId,
      @RequestPart("postRequest") @Valid PostRequestDto postRequestDto,
      @RequestPart(value = "uploadFiles", required = false) List<MultipartFile> multipartFiles) {
    postImagesRepository.deleteByPostId(postId);
    postsService.modifyPost(postId, userId, postRequestDto, multipartFiles);
    return ResponseEntity.ok("수정됐습니다.");

  }

  // 게시글 삭제 (작성자만 가능)
  @DeleteMapping("/delete")
  public ResponseEntity<String> deletePosts(@RequestParam Long postId, @RequestParam Long userId) {
    try {
      postsService.deletePost(postId, userId);
      return ResponseEntity.ok("게시글과 이미지가 삭제되었습니다.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  //게시글 상세 조회
  @GetMapping("/{postId}")
  public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId) {
    try {
      PostResponseDto post = postsService.getPostById(postId);
      return ResponseEntity.ok(post);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @PostMapping("/likes")
  public ResponseEntity<?> likePost(@RequestParam Long userId, @RequestParam Long postId) {
    postsService.likePost(userId, postId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
