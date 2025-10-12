package com.demo.community.posts.controller;

import com.demo.community.common.dto.ApiResponse;
import com.demo.community.posts.dto.PostRequestDTO;
import com.demo.community.posts.dto.PostResponseDTO;
import com.demo.community.posts.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    // 글 작성
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponseDTO.PostCreateResponse>> createPost(
            @RequestBody @Valid PostRequestDTO.PostCreateRequest request
    ){
        // 원래 header에서 받아와야 하지만, 토큰 구현이 안되어있으므로 일단 userId = 1 로 고정
        Long userId = 1L;

        PostResponseDTO.PostCreateResponse result = postService.createPost(request, userId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getPostId())
                .toUri();

        return ResponseEntity.created(location).body(new ApiResponse<>("post created", result));
    }

    // 글 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PostResponseDTO.PostListSliceResponse>> getPostList(
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long lastSeenId
    ) {
        PostResponseDTO.PostListSliceResponse slice = postService.getListPost(lastSeenId, size);

        return ResponseEntity.ok(new ApiResponse<>("post list provided", slice));
    }

    // 글 상세 조회
    // @GetMapping("/{postId}")

    // 글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable("postId") Long postId
    ){
        // 원래 header에서 받아와야 하지만, 토큰 구현이 안되어있으므로 일단 userId = 1 로 고정
        Long userId = 1L;

        postService.deletePost(postId, userId);

        return ResponseEntity.noContent().build();
    }

    // 글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDTO.PostUpdateResponse>> updatePost(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid PostRequestDTO.PostUpdateRequest request
    ){
        // 원래 header에서 받아와야 하지만, 토큰 구현이 안되어있으므로 일단 userId = 1 로 고정
        Long userId = 1L;

        PostResponseDTO.PostUpdateResponse result = postService.updatePost(request, postId, userId);

        return ResponseEntity.ok(new ApiResponse<>("post modified", result));
    }


}
