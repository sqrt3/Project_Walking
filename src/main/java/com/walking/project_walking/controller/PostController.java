package com.walking.project_walking.controller;

import com.walking.project_walking.domain.dto.PostRequest;
import com.walking.project_walking.domain.dto.PostResponse;
import com.walking.project_walking.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor

public class PostController {

    private final PostService postService;


    @PostMapping
    public ResponseEntity<PostResponse> savePosts(@Valid @RequestBody PostRequest postRequest) {
        PostResponse savedPost = postService.savePost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }


    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> modifyPosts(@PathVariable Long postId, @RequestParam Long userId, @Valid @RequestBody PostRequest postRequest) {
        PostResponse updatedPost = postService.modifyPost(postId, userId, postRequest);
        return ResponseEntity.ok(updatedPost);
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePosts(@PathVariable Long postId, @RequestParam Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");


    }
}
