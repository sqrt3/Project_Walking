package com.walking.project_walking.domain.dto;

import com.walking.project_walking.domain.Comments;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentResponseDto {

  private Long commentId;
  private Long parentCommentId;
  private Long postId;
  private Long userId;
  private String content;
  private LocalDateTime createdAt;
  private Boolean isDeleted;
  private String nickname;

  public CommentResponseDto(Comments comment) {
    this.setCommentId(comment.getCommentId());
    this.setParentCommentId(comment.getParentCommentId());
    this.setPostId(comment.getPostId());
    this.setUserId(comment.getUser().getUserId());
    this.setContent(comment.getContent());
    this.setCreatedAt(comment.getCreatedAt());
    this.setIsDeleted(comment.getIsDeleted());
    this.setNickname(comment.getUser().getNickname());
  }
}
