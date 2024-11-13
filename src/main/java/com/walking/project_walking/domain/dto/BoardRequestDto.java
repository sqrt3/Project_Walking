package com.walking.project_walking.domain.dto;

import com.walking.project_walking.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardRequestDto {

  private Long boardId;
  private String name;

  public Board toEntity() {
    return new Board(this.boardId, this.name);
  }
}