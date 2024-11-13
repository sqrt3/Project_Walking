package com.walking.project_walking.domain.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class PostSearchResultDto {

  private final List<PostResponseDto> posts;
  private final int totalPages;

  public PostSearchResultDto(List<PostResponseDto> posts, int totalPages) {
    this.posts = posts;
    this.totalPages = totalPages;
  }
}
