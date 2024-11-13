package com.walking.project_walking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  public void sendPasswordResetEmail(String toEmail, String newPassword) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject("비밀번호 재설정");
    message.setText("비밀번호가 재설정 되었으니 새로운 비밀번호로 로그인 해주세요. \n새로운 비밀번호 : " + newPassword);
    mailSender.send(message);
  }
}
