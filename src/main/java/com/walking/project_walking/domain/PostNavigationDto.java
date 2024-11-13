package com.walking.project_walking.domain;

import com.walking.project_walking.domain.dto.PostResponseDto;


public class PostNavigationDto {

  private final PostResponseDto currentPost;
  private final PostResponseDto previousPost;
  private final PostResponseDto nextPost;

  public PostNavigationDto(PostResponseDto currentPost, PostResponseDto previousPost,
      PostResponseDto nextPost) {
    this.currentPost = currentPost;
    this.previousPost = previousPost;
    this.nextPost = nextPost;
  }

}
