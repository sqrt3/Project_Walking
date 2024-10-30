package com.walking.project_walking.domain;

import javax.persistence.*;

@Entity
@Table(name = "follow")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "follow_user_id", nullable = false)
    private Long followUserId;

    @Column(name = "following_user_id", nullable = false)
    private Long followingUserId;


    public Follow() {}


    public Follow(Long followUserId, Long followingUserId) {
        this.followUserId = followUserId;
        this.followingUserId = followingUserId;
    }


    public Long getFollowUserId() {
        return followUserId;
    }

    public void setFollowUserId(Long followUserId) {
        this.followUserId = followUserId;
    }

    public Long getFollowingUserId() {
        return followingUserId;
    }

    public void setFollowingUserId(Long followingUserId) {
        this.followingUserId = followingUserId;
    }
}