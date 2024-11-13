package com.walking.project_walking.domain.dto;

import java.time.LocalDateTime;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Transactional

public class PostCreateResponseDto {

  private Long postId;
  private Long userId;
  private Long boardId;
  private String title;
  private String content;
  private int viewCount;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private Boolean isDeleted;
}

