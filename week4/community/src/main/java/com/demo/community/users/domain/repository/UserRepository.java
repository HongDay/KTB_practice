package com.demo.community.users.domain.repository;

import com.demo.community.users.domain.enitty.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
}
