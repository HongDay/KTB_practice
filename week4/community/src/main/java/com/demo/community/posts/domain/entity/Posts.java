package com.demo.community.posts.domain.entity;

import com.demo.community.common.domain.entity.BaseEntity;
import com.demo.community.users.domain.enitty.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Users user;

    @Column(nullable = false)
    @Builder.Default
    private Boolean includeImage = false;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostsImages> postImages = new ArrayList<>();

    public void addImages(List<String> urls) {
        for (String u : urls) {
            PostsImages img = PostsImages.builder().imageUrl(u).posts(this).build();
            this.postImages.add(img);
        }
    }

    public List<String> getImages() {
        return this.postImages.stream()
                .map(PostsImages::getImageUrl)
                .collect(Collectors.toList());
    }

    public void updateImages(List<String> urls) {
        Set<String> existingUrls = this.postImages.stream()
                .map(PostsImages::getImageUrl)
                .collect(Collectors.toSet());

        List<String> toAdd = urls.stream()
                .filter(url -> !existingUrls.contains(url))
                .toList();

        List<PostsImages> toRemove = this.postImages.stream()
                .filter(img -> !urls.contains(img.getImageUrl()))
                .toList();

        this.postImages.removeAll(toRemove);

        addImages(toAdd);
    }

    public void updatePost(String title, String content, List<String> urls) {
        if (title != null) {this.title = title;}
        if (content != null) {this.content = content;}
        if (urls != null && !urls.isEmpty()) {
            this.postImages.clear();
            updateImages(urls);
        }
    }

}
