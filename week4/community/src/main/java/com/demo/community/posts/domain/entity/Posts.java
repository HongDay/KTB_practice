package com.demo.community.posts.domain.entity;

import com.demo.community.common.domain.entity.BaseEntity;
import com.demo.community.users.domain.enitty.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Posts extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users userId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean includeImage = false;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostsImages> postImages = new ArrayList<>();

}
