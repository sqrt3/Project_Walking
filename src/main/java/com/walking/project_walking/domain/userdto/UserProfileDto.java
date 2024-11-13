package com.walking.project_walking.domain.userdto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

  private LocalDate joinDate;
  private LocalDateTime lastLogin;
  private Long loginCount;
  private String loginBrowser;
  private Integer userLevel;
  private Long userExp;
  private Integer point;
  private Boolean isActive;
  private String profileImage;
}
