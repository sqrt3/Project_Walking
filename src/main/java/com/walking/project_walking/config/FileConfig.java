//package com.walking.project_walking.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.UUID;
//
//@Configuration
//public class FileConfig {
//
//    // 업로드 경로 설정
//    private static final String UPLOAD_DIR = "C:\\Users\\0furi\\IdeaProjects\\Project_Walking\\src\\main\\resources\\static\\imgs";
//    // 리눅스 서버에서 변경 =>
//    // private static final String UPLOAD_DIR = "/home/user/Project_Walking/src/main/resources/static/imgs/";
//
//    // 파일을 저장하는 메서드
//    public String saveFile(MultipartFile file) throws IOException {
//
//        // 고유한 파일명 생성
//        String originalFilename = file.getOriginalFilename();
//        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
//
//        // 파일을 저장할 전체 경로 생성
//        String fullPath = Paths.get(UPLOAD_DIR, uniqueFileName).toString();
//        file.transferTo(new File(fullPath));
//
//        // 상대 경로 반환
//        return "/imgs/" + uniqueFileName;
//    }
//}
//
