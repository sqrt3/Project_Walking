package com.walking.project_walking.domain.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder

public class CommentResponseDto {
    private Long commentId;
    private Long parentCommentId;
    private Long postId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isDeleted;

}
