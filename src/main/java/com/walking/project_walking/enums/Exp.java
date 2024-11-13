package com.walking.project_walking.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Exp {
  WRITE_COMMENT_POINT(10),
  WRITE_ARTICLE_POINT(30),
  LOGIN_POINT(50);

  private final Integer Amount;
}
