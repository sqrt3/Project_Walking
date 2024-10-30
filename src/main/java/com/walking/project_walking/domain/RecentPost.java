package com.walking.project_walking.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "post_id", nullable = false)
    private Long postId;

//    RecentPost (User user, Post post) {
//        this.userId = user.getUserId();
//        this.postId = post.getPostId();
//    }
}
