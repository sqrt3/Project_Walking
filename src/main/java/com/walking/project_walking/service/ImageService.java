package com.walking.project_walking.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.walking.project_walking.exception.ErrorCode;
import com.walking.project_walking.exception.S3Exception;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

  @Value("${cloud.aws.s3.bucketName}")
  private String bucketName;

  private final AmazonS3 amazonS3;

  //입력받은 이미지 파일이 빈 파일인지 검증
  public String upload(MultipartFile image) {
    if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
      throw new S3Exception(ErrorCode.EMPTY_FILE_EXCEPTION);
    }
    //uploadImage를 호출하여 S3에 저장된 이미지의 public url을 반환 (실제 DB에 저장될 이미지 주소)
    return this.uploadImage(image);
  }

  // 입력받은 이미지 파일의 확장자명 검증
  private String uploadImage(MultipartFile image) {
    this.validateImageFileExtention(image.getOriginalFilename());
    try {
      return this.uploadImageToS3(image);
    } catch (IOException e) {
      throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
    }
  }

  private void validateImageFileExtention(String filename) {
    int lastDotIndex = filename.lastIndexOf(".");
    if (lastDotIndex == -1) {
      throw new S3Exception(ErrorCode.NO_FILE_EXTENTION);
    }

    String extention = filename.substring(lastDotIndex + 1).toLowerCase();
    List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");

    if (!allowedExtentionList.contains(extention)) {
      throw new S3Exception(ErrorCode.INVALID_FILE_EXTENTION);
    }
  }

  // 실제 이미지를 s3로 업로드
  private String uploadImageToS3(MultipartFile image) throws IOException {
    String originalFilename = image.getOriginalFilename(); //원본 파일 명
    String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자 명

    String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명

    InputStream is = image.getInputStream();
    byte[] bytes = IOUtils.toByteArray(is); //image를 byte[]로 변환

    ObjectMetadata metadata = new ObjectMetadata(); //metadata 생성
    metadata.setContentType("image/" + extention);
    metadata.setContentLength(bytes.length);

    //S3에 요청할 때 사용할 byteInputStream 생성
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

    try {
      //S3로 putObject 할 때 사용할 요청 객체
      //생성자 : bucket 이름, 파일 명, byteInputStream, metadata
      PutObjectRequest putObjectRequest =
          new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
              .withCannedAcl(CannedAccessControlList.PublicRead);

      //실제로 S3에 이미지 데이터를 넣는 부분이다.
      amazonS3.putObject(putObjectRequest); // put image to S3
    } catch (Exception e) {
      throw new S3Exception(ErrorCode.PUT_OBJECT_EXCEPTION, e);
    } finally {
      byteArrayInputStream.close();
      is.close();
    }

    return amazonS3.getUrl(bucketName, s3FileName).toString();
  }

  public void deleteImageFromS3(String imageAddress) {
    String key = getKeyFromImageAddress(imageAddress);
    try {
      amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
    } catch (Exception e) {
      throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
    }
  }

  private String getKeyFromImageAddress(String imageAddress) {
    try {
      URL url = new URL(imageAddress);
      String decodingKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
      return decodingKey.substring(1); // 맨 앞의 '/' 제거
    } catch (MalformedURLException e) {
      throw new S3Exception(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
    }
  }

}
