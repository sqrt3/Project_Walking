package com.walking.project_walking.domain.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostSearchResultDto {
    private List<PostResponseDto> posts;
    private int totalPages;

    public PostSearchResultDto(List<PostResponseDto> posts, int totalPages) {
        this.posts = posts;
        this.totalPages = totalPages;
    }
}
