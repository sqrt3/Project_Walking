package com.walking.project_walking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
//@Setter <- 필요할 시 활성화
@NoArgsConstructor
@Table(name = "recent_post")
public class RecentPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    RecentPost (Users user, Posts post) {
        this.userId = user.getUserId();
        this.postId = post.getPostId();
    }
}
