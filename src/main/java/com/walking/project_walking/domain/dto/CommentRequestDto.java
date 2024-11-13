package com.walking.project_walking.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CommentRequestDto {

  private Long postId;
  private Long userId;
  private Long parentCommentId;

  @NotBlank(message = "댓글을 작성해주세요.")
  private String content;

}



