package com.walking.project_walking.service;

import com.walking.project_walking.domain.Comments;
import com.walking.project_walking.domain.Users;
import com.walking.project_walking.domain.dto.CommentRequestDto;
import com.walking.project_walking.domain.dto.CommentResponseDto;
import com.walking.project_walking.enums.Exp;
import com.walking.project_walking.enums.Point;
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

        Users user = comment.getUser();
        if (user.getRole().name().equals("ROLE_USER"))
        {
            user.setPoint(user.getPoint() + Point.WRITE_COMMENT_POINT.getAmount());
            user.setUserExp(user.getUserExp() + Exp.WRITE_COMMENT_POINT.getAmount());
        } else {
            user.setPoint(user.getPoint() + Point.WRITE_COMMENT_POINT.getAmount() * 2);
            user.setUserExp(user.getUserExp() + Exp.WRITE_COMMENT_POINT.getAmount() * 2);
        }
        userRepository.save(user);

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
    public void deleteComment(Long commentId, Long userId) {
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        Users user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 ID를 찾을 수 없습니다."));

        if (!comment.getUser().getUserId().equals(user.getUserId()) && !user.getRole().name().equals("ROLE_ADMIN")) {
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

    // 등록순 댓글 조회
    public List<CommentResponseDto> getCommentsByPostIdOldest(Long postId) {
        return commentRepository.findCommentsByPostIdOrderByCreatedAtAsc(postId)
                .stream().map(CommentResponseDto::new)
                .toList();
    }



}


