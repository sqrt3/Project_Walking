package com.walking.project_walking.exception;

public enum ErrorCode {
  EMPTY_FILE_EXCEPTION("빈 파일입니다."),
  IO_EXCEPTION_ON_IMAGE_UPLOAD("이미지 업로드 중 입출력 오류가 발생했습니다."),
  IO_EXCEPTION_ON_IMAGE_DELETE("이미지 삭제 중 입출력 오류가 발생했습니다."),
  PUT_OBJECT_EXCEPTION("S3에 이미지 업로드 중 오류가 발생했습니다."),
  NO_FILE_EXTENTION("파일 확장자가 없습니다."),
  INVALID_FILE_EXTENTION("허용되지 않는 파일 확장자입니다.");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
