package com.walking.project_walking.domain.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CommentRequestDto {

    private Long postId;
    private Long userId;
    private Long parentCommentId;

    @NotBlank(message = "댓글을 작성해주세요.")
    private String content;

}



