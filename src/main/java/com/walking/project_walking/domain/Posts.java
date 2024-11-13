package com.walking.project_walking.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "posts")
public class Posts {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long postId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "board_id", nullable = false)
  private Long boardId;

  @Column(name = "title", nullable = false, length = 256)
  private String title;

  @Column(name = "content", nullable = false, length = 512)
  private String content;

  @Column(name = "view_count")
  private Integer viewCount;

  @Column(name = "likes")
  private Integer likes;

  @Column(name = "weight_value")
  private Integer weightValue;

  @CreatedDate
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "modified_at")
  private LocalDateTime modifiedAt;

  @Column(name = "is_deleted")
  private Boolean isDeleted;
}