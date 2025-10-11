package com.demo.community.posts.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostsCounts {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Posts posts;

    @Column(nullable = false)
    @Builder.Default
    private int likeCounts = 0;

    @Column(nullable = false)
    @Builder.Default
    private int replyCounts = 0;

}
