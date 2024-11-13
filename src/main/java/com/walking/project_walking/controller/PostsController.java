package com.walking.project_walking.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.walking.project_walking.domain.PostImages;
import com.walking.project_walking.domain.dto.*;
import com.walking.project_walking.service.BoardService;
import com.walking.project_walking.service.PostsService;
import com.walking.project_walking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostsController {
    private final PostsService postsService;
    private final BoardService boardService;
    private final UserService userService;

    @Autowired
    private AmazonS3 amazonS3Client;

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
        List<PostResponseDto> postList = postsService.searchPosts(boardId, title, content, nickname, pageRequest);

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
    // 게시글, 이미지 생성을 독립적으로 가능하도록 하기 위해 api 따로 작성
    @PostMapping
    public ResponseEntity<?> savePosts(@RequestPart("postRequest") @Valid PostRequestDto postRequest,
                                        @RequestPart(value = "uploadFiles", required = false) List<MultipartFile> multipartFiles) {
        try {
            PostCreateResponseDto savedPost = postsService.savePost(postRequest, multipartFiles);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 업로드 중 오류가 발생했습니다.");
        }
    }

    // 이미지 파일 업로드
    @PostMapping("/upload-images")
    public ResponseEntity<String> uploadMultipleFiles(
            @RequestPart("uploadFiles") List<MultipartFile> multipartFiles,
            @RequestParam String type) { //  파일 유형 처리시 필요할 수도 있음
        try {
            for (MultipartFile file : multipartFiles) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());

                // 파일 이름이 중복되면 기존 파일을 덮을 수가 있어서 고유 식별자를 생성해서 파일 관리를 해야함
                String uniqueFileName = "objectKey/" + UUID.randomUUID() + "-" + file.getOriginalFilename();


                // S3 업로드 요청 생성 및 파일 업로드
                PutObjectRequest putObjectRequest = new PutObjectRequest(
                        "bucketName",
                        uniqueFileName,
                        file.getInputStream(),
                        metadata
                );
                amazonS3Client.putObject(putObjectRequest);

            }
            return ResponseEntity.ok("업로드 완료");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 업로드 중 오류가 발생했습니다.");
        }
    }

    // 게시글 수정 (작성자만 가능, 기존 이미지 유시/삭제 가능)
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostCreateResponseDto> modifyPosts(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @Valid @RequestBody PostRequestDto postRequestDto,
            @RequestPart(value = "uploadFiles", required = false) List<MultipartFile> multipartFiles,
            @RequestParam(value = "deleteExistingImages", defaultValue = "false") boolean deleteExistingImages) {
        try {
            // 게시글 수정
            PostCreateResponseDto updatedPost = postsService.modifyPost(postId, userId, postRequestDto, multipartFiles, deleteExistingImages);

            // 기존 이미지 삭제 옵션 처리
            if (deleteExistingImages) {
                List<String> existingImageUrls = postsService.getPostImageUrls(postId);  // 기존 이미지 URL 가져오기
                for (String imageUrl : existingImageUrls) {
                    postsService.deleteFileFromS3(imageUrl);  // S3에서 기존 이미지 삭제
                }
                postsService.deletePostImages(postId);  // 데이터베이스에서 기존 이미지 정보 삭제 (지정된 파일 삭제)
            }

            // 새 이미지 업로드 및 저장
            if (multipartFiles != null && !multipartFiles.isEmpty()) {
                List<PostImages> postImagesList = new ArrayList<>();
                for (MultipartFile file : multipartFiles) {
                    if (!file.isEmpty()) {
                        String fileUrl = postsService.uploadFileToS3(file);  // S3에 새 이미지 업로드
                        PostImages postImage = new PostImages(postId, fileUrl);  // 새 이미지 URL 저장
                        postImagesList.add(postImage);
                    }
                }
                postsService.savePostImages(postId, postImagesList);  // 데이터베이스에 새 이미지 정보 저장
            }

            return ResponseEntity.ok(updatedPost);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 게시글 삭제 (작성자만 가능)
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<String> deletePosts(@PathVariable Long postId, @RequestParam Long userId) {
        try {
            // 게시글에 연관된 이미지 URL 가져오기
            List<String> imageUrls = postsService.getPostImageUrls(postId);

            // S3에서 이미지 삭제
            for (String imageUrl : imageUrls) {
                postsService.deleteFileFromS3(imageUrl);  // S3에서 이미지 삭제
            }

            // 데이터베이스에서 게시글 및 이미지 정보 삭제
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
    public ResponseEntity<String> likePost (@RequestParam Long userId, @RequestParam Long postId) {
        postsService.likePost(userId, postId);
        return ResponseEntity.ok("좋아요 상태가 변경되었습니다.");
    }

}
