package com.walking.project_walking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Point {
  WRITE_COMMENT_POINT(1),
  WRITE_ARTICLE_POINT(3),
  LOGIN_POINT(5);

  private final Integer Amount;
}
