package com.walking.project_walking.service;

import com.walking.project_walking.domain.Comments;
import com.walking.project_walking.domain.dto.CommentRequest;
import com.walking.project_walking.domain.dto.CommentResponse;
import com.walking.project_walking.repository.CommentsRepository;
import org.springframework.stereotype.Service;


@Service

public class CommentService {
    private final CommentsRepository commentRepository;

    public CommentService(CommentsRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    // 댓글, 답글 생성
    public CommentResponse createComment (CommentRequest request) {
        Comments comment = Comments.builder()
                .postId(request.getPostId())
                .userId(request.getUserId())
                .content(request.getContent())
                .parentCommentId(request.getParentCommentId())
                .build();

        Comments savedComment = commentRepository.save(comment);

        return new CommentResponse(
                savedComment.getCommentId(),
                savedComment.getParentCommentId(),
                savedComment.getPostId(),
                savedComment.getUserId(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getIsDeleted());
    }


    //댓글, 답글 삭제 (작성자만 가능)
    public void deleteComment(Long commentId,Long userId) {
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        comment.setIsDeleted(true);
        commentRepository.save(comment);

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        comment.setIsDeleted(true);
        commentRepository.save(comment);

    }


    //댓글, 답글 수정 (작성자만 가능)
    public CommentResponse modifyComment(Long commentId, Long userId, CommentRequest request) {
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        comment.setContent(request.getContent());
        Comments updatedComment = commentRepository.save(comment);

        return new CommentResponse(
                updatedComment.getCommentId(),
                updatedComment.getParentCommentId(),
                updatedComment.getPostId(),
                updatedComment.getUserId(),
                updatedComment.getContent(),
                updatedComment.getCreatedAt(),
                updatedComment.getIsDeleted());
    }


    //댓글, 답글 수 조회
    public int getCommentCount(Long postId) {
        return commentRepository.countByPostId(postId);
    }








}


