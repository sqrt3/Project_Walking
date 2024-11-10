package com.walking.project_walking.domain;

import com.walking.project_walking.domain.dto.PostResponseDto;


public class PostNavigationDto {
    private PostResponseDto currentPost;
    private PostResponseDto previousPost;
    private PostResponseDto nextPost;

    public PostNavigationDto(PostResponseDto currentPost, PostResponseDto previousPost, PostResponseDto nextPost) {
        this.currentPost = currentPost;
        this.previousPost = previousPost;
        this.nextPost = nextPost;
    }

}
