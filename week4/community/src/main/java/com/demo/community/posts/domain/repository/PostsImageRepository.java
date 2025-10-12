package com.demo.community.posts.domain.repository;

import com.demo.community.posts.domain.entity.PostsImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsImageRepository extends JpaRepository<PostsImages, Long> {
}
