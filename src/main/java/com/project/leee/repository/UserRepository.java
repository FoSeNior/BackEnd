package com.project.leee.repository;

import com.project.leee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    //  userId 중복 확인
    boolean existsByUserId(String userId);

    //  userId로 사용자 찾기
    Optional<User> findByUserId(String userId);
}
