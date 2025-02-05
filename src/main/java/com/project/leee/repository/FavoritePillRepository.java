package com.project.leee.repository;

import com.project.leee.entity.FavoritePill;
import com.project.leee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritePillRepository extends JpaRepository<FavoritePill, Integer> {
    List<FavoritePill> findByUid(Integer uid); // 🔹 uId 기준으로 검색하도록 변경


    //  uId와 itemSeq를 기반으로 삭제
    void deleteByUidAndItemSeq(Integer uId, Long itemSeq);
}
