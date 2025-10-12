package com.demo.community.posts.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PostCreateResponse {
        private Long postId;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PostUpdateResponse {
        private Long postId;
        private String title;
        private String content;
        private List<String> postImages;
        private Long userId;
        private String nickname;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PostListResponse {
        private Long postId;
        private String title;
        private String nickname;
        private String userImage;
        private int likeCount;
        private int replyCount;
        private int viewCount;
        private LocalDateTime createdAt;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class PostListSliceResponse {
        private List<PostListResponse> items;
        private boolean hasNext;
        private Long nextCursorId;
    }
}
