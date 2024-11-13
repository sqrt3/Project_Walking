package com.walking.project_walking.controller;


import com.walking.project_walking.domain.dto.CommentRequestDto;
import com.walking.project_walking.domain.dto.CommentResponseDto;
import com.walking.project_walking.service.CommentsService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {

  private final CommentsService commentsService;

  // 댓글, 답글 생성
  @PostMapping("/{postId}")
  public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId,
      @Valid @ModelAttribute CommentRequestDto dto) {
    dto.setPostId(postId);
    CommentResponseDto createdDto = commentsService.createComment(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
  }

  //댓글, 답글 삭제 (작성자만 가능)
  @DeleteMapping("/{commentId}")
  public ResponseEntity<String> deleteComment(@PathVariable Long commentId, HttpSession session) {
    Long userId = (Long) session.getAttribute("userId");
    commentsService.deleteComment(commentId, userId);
    return ResponseEntity.status(HttpStatus.OK).body("댓글이 삭제되었습니다");
  }
}