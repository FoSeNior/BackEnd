package com.inje.forseni.Repository;

import com.inje.forseni.Entity.ApiPill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ApiPillRepository extends JpaRepository<ApiPill, Long> {
    Optional<ApiPill> findByItemName(String itemName);

    //  특정 이름을 포함하는 약을 검색
    List<ApiPill> findByItemNameContainingIgnoreCase(String itemName);
}
