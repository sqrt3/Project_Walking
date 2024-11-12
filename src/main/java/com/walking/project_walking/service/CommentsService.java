package com.walking.project_walking.service;

import com.walking.project_walking.domain.Comments;
import com.walking.project_walking.domain.dto.CommentRequestDto;
import com.walking.project_walking.domain.dto.CommentResponseDto;
import com.walking.project_walking.repository.CommentsRepository;
import com.walking.project_walking.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentRepository;
    private final UserRepository userRepository;

    // 댓글, 답글 생성
    public CommentResponseDto createComment (CommentRequestDto request) {
        Comments comment = Comments.builder()
                .postId(request.getPostId())
                .user(userRepository.findById(request.getUserId()).orElse(null))
                .content(request.getContent())
                .parentCommentId(request.getParentCommentId())
                .createdAt(LocalDateTime.now())
                .isDeleted(Boolean.FALSE)
                .build();

        Comments savedComment = commentRepository.save(comment);

        return new CommentResponseDto(
                savedComment.getCommentId(),
                savedComment.getParentCommentId(),
                savedComment.getPostId(),
                savedComment.getUser().getUserId(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getIsDeleted(),
                savedComment.getUser().getNickname()
        );
    }


    //댓글, 답글 삭제 (작성자만 가능)
    public void deleteComment(Long commentId,Long userId) {
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        comment.setIsDeleted(true);
        commentRepository.save(comment);

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        comment.setIsDeleted(true);
        commentRepository.save(comment);

    }


    //댓글, 답글 수정 (작성자만 가능)
    public CommentResponseDto modifyComment(Long commentId, Long userId, CommentRequestDto request) {
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        comment.setContent(request.getContent());
        Comments updatedComment = commentRepository.save(comment);

        return new CommentResponseDto(
                updatedComment.getCommentId(),
                updatedComment.getParentCommentId(),
                updatedComment.getPostId(),
                updatedComment.getUser().getUserId(),
                updatedComment.getContent(),
                updatedComment.getCreatedAt(),
                updatedComment.getIsDeleted(),
                updatedComment.getUser().getNickname()
        );
    }

    // 최신순 댓글 조회
    public List<CommentResponseDto> getCommentsByPostIdLatest(Long postId) {
        return commentRepository.findCommentsByPostIdOrderByCreatedAtDesc(postId)
                .stream().map(CommentResponseDto::new)
                .toList();
    }

    // 등록순 댓글 조회
    public List<CommentResponseDto> getCommentsByPostIdOldest(Long postId) {
        return commentRepository.findCommentsByPostIdOrderByCreatedAtAsc(postId)
                .stream().map(CommentResponseDto::new)
                .toList();
    }



}


