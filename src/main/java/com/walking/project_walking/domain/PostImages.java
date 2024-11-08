package com.walking.project_walking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post_images")
@Getter
@Setter
@NoArgsConstructor
public class PostImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "image_url", length = 512, nullable = false)
    private String imageUrl;

    public PostImages(Long postId, String imageUrl) {
        this.postId = postId;
        this.imageUrl = imageUrl;
    }
}
