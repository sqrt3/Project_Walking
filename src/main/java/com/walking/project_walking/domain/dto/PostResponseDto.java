package com.walking.project_walking.domain.dto;

import com.walking.project_walking.domain.Posts;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long boardId;
    private LocalDateTime createdAt;
    private String postImage;
    private String title;
    private Integer commentsCount;
    private String content;
    private Integer viewCount;
    private Integer likes;
    private String nickname;

    public static PostResponseDto fromEntity(Posts post, Integer commentsNumber, String nickname) {
        PostResponseDto dto = new PostResponseDto();
        dto.boardId = post.getBoardId();
        dto.createdAt = post.getCreatedAt();
        dto.postImage = post.getPostImage();
        dto.title = post.getTitle();
        dto.commentsCount = commentsNumber;
        dto.content = post.getContent();
        dto.viewCount = post.getViewCount();
        dto.likes = post.getLikes();
        dto.nickname = nickname;
        return dto;
    }

}