package com.demo.community.posts.domain.repository;

import com.demo.community.posts.domain.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {
    @Query(value = "SELECT * FROM posts ORDER BY id DESC LIMIT :limit", nativeQuery = true)
    List<Posts> listFirstPage(@Param("limit") int limit);

    @Query(value = "SELECT * FROM posts WHERE id < :cursorId ORDER BY id DESC LIMIT :limit", nativeQuery = true)
    List<Posts> listOtherPage(@Param("cursorId") Long cursorId, @Param("limit") int limit);

}
