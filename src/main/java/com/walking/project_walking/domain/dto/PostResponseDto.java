package com.walking.project_walking.domain.dto;

import com.walking.project_walking.domain.Posts;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long postId;
    private Long boardId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String title;
    private Integer commentsCount;
    private String content;
    private Integer viewCount;
    private Integer likes;
    private String nickname;
    private List<String> imageUrl;

    public static PostResponseDto fromEntity(Posts post, Integer commentsNumber, String nickname, List<String> imageUrl) {
        PostResponseDto dto = new PostResponseDto();
        dto.postId = post.getPostId();
        dto.boardId = post.getBoardId();
        dto.createdAt = post.getCreatedAt();
        dto.modifiedAt = post.getModifiedAt();
        dto.title = post.getTitle();
        dto.commentsCount = commentsNumber;
        dto.content = post.getContent();
        dto.viewCount = post.getViewCount();
        dto.likes = post.getLikes();
        dto.nickname = nickname;
        dto.imageUrl = imageUrl;
        return dto;
    }
}