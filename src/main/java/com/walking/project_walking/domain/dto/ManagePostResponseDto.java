package com.walking.project_walking.domain.dto;

import com.walking.project_walking.domain.Posts;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ManagePostResponseDto {

  private Long postId;
  private Long boardId;
  private String title;
  private LocalDateTime createdAt;
  private Boolean isDeleted;

  public static ManagePostResponseDto fromEntity(Posts post) {
    ManagePostResponseDto dto = new ManagePostResponseDto();
    dto.postId = post.getPostId();
    dto.boardId = post.getBoardId();
    dto.title = post.getTitle();
    dto.createdAt = post.getCreatedAt();
    dto.isDeleted = post.getIsDeleted();
    return dto;
  }
}
