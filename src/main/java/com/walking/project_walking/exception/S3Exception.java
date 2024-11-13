package com.walking.project_walking.exception;

public class S3Exception extends RuntimeException {

  private final ErrorCode errorCode;

  public S3Exception(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public S3Exception(ErrorCode errorCode, Throwable cause) {
    super(errorCode.getMessage(), cause);
    this.errorCode = errorCode;
  }
}
