package com.demo.community.posts.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

public class PostRequestDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class PostCreateRequest {
        @NotNull
        private String title;
        @NotNull
        private String content;
        @Size(max = 3, message = "you can only upload URL up to 3.")
        private List<String> imageUrl;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class PostUpdateRequest {
        private String title;
        private String content;
        @Size(max = 3, message = "you can only upload URL up to 3.")
        private List<String> imageUrl;
    }
}
