package com.walking.project_walking.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PostRequestDto {

  @NotNull(message = "게시글 작성자는 필수입니다.")
  private Long userId;

  @NotNull(message = "게시판 ID는 필수입니다.")
  private Long boardId;

  @NotBlank(message = "제목은 필수입니다.")
  private String title;

  @NotBlank(message = "내용은 필수입니다.")
  private String content;

  private List<String> postImage;
}
