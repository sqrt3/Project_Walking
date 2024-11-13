package com.walking.project_walking.controller;

import com.walking.project_walking.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class ImageController {

  private final ImageService imageService;

  @PostMapping("/s3/upload")
  public ResponseEntity<?> s3Upload(
      @RequestPart(value = "image", required = false) MultipartFile image) {
    String profileImage = imageService.upload(image);
    return ResponseEntity.ok(profileImage);
  }

  @GetMapping("/s3/delete")
  public ResponseEntity<?> s3delete(@RequestParam String addr) {
    imageService.deleteImageFromS3(addr);
    return ResponseEntity.ok(null);
  }
}
