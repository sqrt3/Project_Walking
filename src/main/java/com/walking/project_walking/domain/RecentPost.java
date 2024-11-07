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
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 외래 키 관계 설정
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private Users user; // users의 pk와 연결

    @Column(name = "post_id", nullable = false)
    private Long postId;

    RecentPost (Users user, Posts post) {
        this.userId = user.getUserId();
        this.postId = post.getPostId();
    }
}
