package com.walking.project_walking.domain.dto;

import com.walking.project_walking.domain.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeResponseDto {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static NoticeResponseDto fromEntity(Posts post){
        NoticeResponseDto dto = new NoticeResponseDto();
        dto.postId = post.getPostId();
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.createdAt = post.getCreatedAt();
        return dto;
    }
}
