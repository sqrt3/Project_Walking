package com.walking.project_walking.domain.dto;

import com.walking.project_walking.domain.Posts;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostSummuryResponseDto {

  private Long postId;
  private LocalDateTime createdAt;
  private String title;
  private Integer commentsCount;

  public static PostSummuryResponseDto fromEntity(Posts posts, Integer commentsNumber) {
    PostSummuryResponseDto dto = new PostSummuryResponseDto();
    dto.postId = posts.getPostId();
    dto.createdAt = posts.getCreatedAt();
    dto.title = posts.getTitle();
    dto.commentsCount = commentsNumber;
    return dto;
  }
}
