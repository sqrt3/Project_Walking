package com.walking.project_walking.service;

import com.walking.project_walking.domain.Posts;
import com.walking.project_walking.domain.dto.PostRequest;
import com.walking.project_walking.domain.dto.PostResponse;
import com.walking.project_walking.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class PostService {

    private final PostRepository postRepository;

    //게시글 생성
    public PostResponse savePost(PostRequest postRequest) {
        Posts post = Posts.builder()
                .userId(postRequest.getUserId())
                .boardId(postRequest.getBoardId())
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .postImage(postRequest.getPostImage())
                .build();

        Posts savedPost = postRepository.save(post);

        return new PostResponse(
                savedPost.getPostId(),
                savedPost.getUserId(),
                savedPost.getBoardId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getPostImage(),
                savedPost.getViewCount(),
                savedPost.getCreatedAt(),
                savedPost.getModifiedAt(),
                savedPost.getIsDeleted()
        );
    }

    //게시글 수정 (작성자만 가능)
    public PostResponse modifyPost(Long postId, Long userId, PostRequest postRequest) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setPostImage(postRequest.getPostImage());

        Posts updatedPost = postRepository.save(post);

        return new PostResponse(
                updatedPost.getPostId(),
                updatedPost.getUserId(),
                updatedPost.getBoardId(),
                updatedPost.getTitle(),
                updatedPost.getContent(),
                updatedPost.getPostImage(),
                updatedPost.getViewCount(),
                updatedPost.getCreatedAt(),
                updatedPost.getModifiedAt(),
                updatedPost.getIsDeleted()
        );
    }

    //게시글 삭제 (작성자만 가능)
    public void deletePost(Long postId, Long userId) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        post.setIsDeleted(true);
        postRepository.save(post);

    }
}