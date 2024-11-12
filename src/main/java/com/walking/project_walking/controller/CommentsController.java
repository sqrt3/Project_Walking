package com.walking.project_walking.controller;


import com.walking.project_walking.domain.dto.CommentRequestDto;
import com.walking.project_walking.domain.dto.CommentResponseDto;
import com.walking.project_walking.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {
    private final CommentsService commentsService;

    // 댓글, 답글 생성
    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId, @Valid @ModelAttribute CommentRequestDto dto) {
        dto.setPostId(postId);
        CommentResponseDto createdDto = commentsService.createComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    //댓글, 답글 삭제 (작성자만 가능)
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId, @RequestParam Long userId) {
        commentsService.deleteComment(postId,userId);
        return ResponseEntity.status (HttpStatus.OK).body ("댓글이 삭제되었습니다");
    }

    //댓글, 답글 수정 (작성자만 가능)
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> modifyComment(@PathVariable Long commentId, @RequestParam Long userId, @Valid @RequestBody CommentRequestDto dto) {
        CommentResponseDto updatedComment = commentsService.modifyComment(commentId, userId, dto);
        return ResponseEntity.status (HttpStatus.OK).body (updatedComment);
    }


}