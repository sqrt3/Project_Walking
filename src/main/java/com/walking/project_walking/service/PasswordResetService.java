package com.walking.project_walking.service;

import com.walking.project_walking.domain.Users;
import com.walking.project_walking.exception.UserNotFoundException;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

  private final UserService userService;
  private final EmailService emailService;

  public String resetPassword(String email) {
    Users user = userService.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException("등록되지 않은 이메일입니다."));

    String newPassword = generateRandomPassword();

    userService.updatePassword(user, newPassword);

    emailService.sendPasswordResetEmail(user.getEmail(), newPassword);

    return newPassword;
  }

  private String generateRandomPassword() {
    String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String lowercase = "abcdefghijklmnopqrstuvwxyz";
    String digits = "0123456789";
    String specialChars = "!@#$%^&*()_+";

    Random random = new Random();
    StringBuilder password = new StringBuilder(12);

    password.append(uppercase.charAt(random.nextInt(uppercase.length())));
    password.append(lowercase.charAt(random.nextInt(lowercase.length())));
    password.append(digits.charAt(random.nextInt(digits.length())));
    password.append(specialChars.charAt(random.nextInt(specialChars.length())));

    String allChars = uppercase + lowercase + digits + specialChars;
    for (int i = 4; i < 12; i++) {
      password.append(allChars.charAt(random.nextInt(allChars.length())));
    }

    for (int i = 0; i < password.length(); i++) {
      int j = random.nextInt(password.length());
      char temp = password.charAt(i);
      password.setCharAt(i, password.charAt(j));
      password.setCharAt(j, temp);
    }

    return password.toString();
  }
}
