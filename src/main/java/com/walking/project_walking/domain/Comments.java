package com.walking.project_walking.domain;

import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

//    @Column(name = "user_id", nullable = false)
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
