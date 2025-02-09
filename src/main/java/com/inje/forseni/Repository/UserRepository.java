package com.inje.forseni.Repository;

import com.inje.forseni.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    //  userId 중복 확인
    boolean existsByMembershipId(String membershipId);

    //  userId로 사용자 찾기
    Optional<User> findByMembershipId(String membershipId);
}
