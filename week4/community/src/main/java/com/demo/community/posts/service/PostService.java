package com.demo.community.posts.service;

import com.demo.community.posts.domain.entity.Posts;
import com.demo.community.posts.domain.entity.QPostViewCounts;
import com.demo.community.posts.domain.entity.QPosts;
import com.demo.community.posts.domain.entity.QPostsCounts;
import com.demo.community.posts.domain.repository.PostRepository;
import com.demo.community.posts.dto.PostRequestDTO;
import com.demo.community.posts.dto.PostResponseDTO;
import com.demo.community.users.domain.enitty.QUsers;
import com.demo.community.users.domain.enitty.Users;
import com.demo.community.users.domain.repository.UserRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public PostResponseDTO.PostCreateResponse createPost(PostRequestDTO.PostCreateRequest request, Long userId) {
        Users user = userRepository.findById(userId)
                // 이 예외는 나중에 커스텀 에외 (실패코드, 메세지를 응답으로 반환하는)로 변경 예정
                .orElseThrow(() -> new EntityNotFoundException("user not found"));

        Boolean includeImage = request.getImageUrl().isEmpty();

        Posts post = Posts.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .includeImage(includeImage).build();

        if (includeImage) {post.addImages(request.getImageUrl());}

        postRepository.save(post);

        return PostResponseDTO.PostCreateResponse.builder().postId(post.getId()).build();
    }

    @Transactional
    public void deletePost(Long postId, Long userId){
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not found"));

        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("post not found"));

        // 여기서 lazy 로딩 될듯?
        if (!post.getUser().getId().equals(userId)){
            // 이 예외도 나중에 실패코드를 응답하는 커스텀 예외로 변경해야함.
            throw new EntityNotFoundException("delete forbidden user");
        }

        postRepository.delete(post);
    }

    @Transactional
    public PostResponseDTO.PostUpdateResponse updatePost(PostRequestDTO.PostUpdateRequest request, Long postId, Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not found"));

        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("post not found"));

        if (!post.getUser().getId().equals(userId)){
            throw new EntityNotFoundException("delete forbidden user");
        }

        post.updatePost(request.getTitle(), request.getContent(), request.getImageUrl());

        postRepository.flush();

        return PostResponseDTO.PostUpdateResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .postImages(post.getImages())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .nickname(post.getUser().getNickname()) // lazy loading 될듯 여기서
                .build();
    }

    @Transactional(readOnly = true)
    public PostResponseDTO.PostListSliceResponse getListPost(Long cursorId, int size) {

        QPosts p = QPosts.posts;
        QUsers u = QUsers.users;
        QPostsCounts pc = QPostsCounts.postsCounts;
        QPostViewCounts pv = QPostViewCounts.postViewCounts;

        List<PostResponseDTO.PostListResponse> posts = jpaQueryFactory
                    .select(Projections.constructor(PostResponseDTO.PostListResponse.class,
                            p.id, p.title,
                            u.nickname, u.profileImage,
                            pc.likeCounts, pc.replyCounts, pv.viewCounts,
                            p.createdAt
                    ))
                    .from(p)
                    .join(p.user, u)
                    .leftJoin(pc).on(pc.posts.eq(p))
                    .leftJoin(pv).on(pv.posts.eq(p))
                    .where(cursorId != null ? p.id.lt(cursorId) : null)
                    .orderBy(p.id.desc())
                    .limit(size + 1)
                    .fetch();


        boolean hasNext = posts.size() > size;
        if (hasNext){
            posts = posts.subList(0, size);
        }

        Long nextCursor = posts.isEmpty() ? null : posts.getLast().getPostId();

        PostResponseDTO.PostListSliceResponse body = PostResponseDTO.PostListSliceResponse.builder()
                //.items()
                .hasNext(hasNext)
                .nextCursorId(nextCursor).build();

        return body;
    }


}
