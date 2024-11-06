package com.walking.project_walking.controller;


import com.walking.project_walking.domain.dto.CommentRequest;
import com.walking.project_walking.domain.dto.CommentResponse;
import com.walking.project_walking.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor

public class CommentController {


    private final CommentService commentService;


    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId, @Valid @RequestBody CommentRequest dto) {
        dto.setPostId(postId);
        CommentResponse createdDto = commentService.createComment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }


    @DeleteMapping("/{postId}/comments")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId, @RequestParam Long userId) {
        commentService.deleteComment(postId,userId);
        return ResponseEntity.status (HttpStatus.OK).body ("댓글이 삭제되었습니다");
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> modifyComment(@PathVariable Long commentId, @RequestParam Long userId, @Valid @RequestBody CommentRequest dto) {
        CommentResponse updatedComment = commentService.modifyComment(commentId, userId, dto);
        return ResponseEntity.status (HttpStatus.OK).body (updatedComment);
    }


    @GetMapping("/{commentId}/count")
    public ResponseEntity<Integer> getCommentCount(@PathVariable Long commentId) {
        int count = commentService.getCommentCount(commentId);
        return ResponseEntity.status (HttpStatus.OK).body(count);
    }



}