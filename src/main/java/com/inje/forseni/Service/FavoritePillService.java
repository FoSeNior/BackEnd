package com.inje.forseni.Service;

import com.inje.forseni.Dto.FavoritePillDTO;
import com.inje.forseni.Entity.FavoritePill;
import com.inje.forseni.Repository.FavoritePillRepository;
import com.inje.forseni.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoritePillService {

    private final FavoritePillRepository favoritePillRepository;
    private final UserRepository userRepository;

    public FavoritePillService(FavoritePillRepository favoritePillRepository, UserRepository userRepository) {
        this.favoritePillRepository = favoritePillRepository;
        this.userRepository = userRepository;
    }

    // 찜 목록 가져오기
    public ResponseEntity<Map<String, Object>> getFavoritePills(Integer userId) {
        List<FavoritePill> favoritePills = favoritePillRepository.findByUser_UserId(userId);

        if (favoritePills.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "찜 목록이 없습니다.",
                    "data", List.of()
            ));
        }

        // DTO 변환 후 반환
        List<FavoritePillDTO> favoritePillDTOs = favoritePills.stream()
                .map(pill -> FavoritePillDTO.builder()
                        .favId(pill.getFavId())
                        .itemSeq(pill.getItemSeq())
                        .itemName(pill.getItemName())
                        .entpName(pill.getEntpName())
                        .efcyQesitm(pill.getEfcyQesitm())
                        .atpnWarnQesitm(pill.getAtpnWarnQesitm())
                        .atpnQesitm(pill.getAtpnQesitm())
                        .itemImage(pill.getItemImage())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "찜 목록 조회 성공",
                "data", favoritePillDTOs
        ));
    }

    // 찜 삭제 기능
    @Transactional
    public ResponseEntity<Map<String, Object>> removeFavoritePill(Map<String, Object> requestData) {
        // `userId`와 `itemSeq`를 `Service`에서 추출 및 검증
        Integer userId;
        Long itemSeq;

        try {
            userId = Integer.parseInt(requestData.get("userId").toString());
            itemSeq = Long.parseLong(requestData.get("itemSeq").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "u_id 또는 itemSeq가 올바르지 않습니다."
            ));
        }

        Optional<FavoritePill> favoritePill = favoritePillRepository.findByUser_UserId(userId)
                .stream()
                .filter(pill -> pill.getItemSeq().equals(itemSeq))
                .findFirst();

        if (favoritePill.isPresent()) {
            favoritePillRepository.deleteByUser_UserIdAndItemSeq(userId, itemSeq);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "찜 목록에서 삭제되었습니다."
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "해당 찜 목록이 존재하지 않습니다."
            ));
        }
    }
}
