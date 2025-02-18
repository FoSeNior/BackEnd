package com.inje.forseni.Repository;

import com.inje.forseni.Entity.FavoritePill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritePillRepository extends JpaRepository<FavoritePill, Integer> {
    //List<FavoritePill> findByUid(Integer userId); //  userId 기준으로 검색하도록 변경
    List<FavoritePill> findByUser_UserId(Integer userId);

    //  userId와 itemSeq를 기반으로 삭제
    //void deleteByUidAndItemSeq(Integer userId, Long itemSeq);
    void deleteByUser_UserIdAndItemSeq(Integer userId, Long itemSeq);

    //  특정 유저가 특정 itemSeq를 찜했는지 확인
    boolean existsByUser_UserIdAndItemSeq(Integer userId, Long itemSeq);
}
