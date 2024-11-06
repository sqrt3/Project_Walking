package com.walking.project_walking.domain.dto;

import lombok.*;


import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder

public class PostCreateResponseDto {

    private Long postId;
    private Long userId;
    private Long boardId;
    private String title;
    private String content;
    private String postImage;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean isDeleted;
}

