package com.inje.forseni.Controller;

import com.inje.forseni.Service.MedicationApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/search/pill")
public class MedicationApiController {

    private final MedicationApiService medicationApiService;

    public MedicationApiController(MedicationApiService medicationApiService) {
        this.medicationApiService = medicationApiService;
    }

    // 모든 약 데이터를 API에서 가져와 DB에 저장하는 엔드포인트
    @PostMapping("/loadAll")
    public ResponseEntity<Map<String, Object>> loadAllPills() {
        return medicationApiService.loadAllPills();
    }

    //  제품명 목록 전체 조회
    @GetMapping("/all/{pillName}")
    public ResponseEntity<Map<String, Object>> getPillList(@PathVariable String pillName) {
        return medicationApiService.searchPillList(pillName);
    }

    // 모든 약 목록 조회 (새로 추가된 엔드포인트)
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllPills() {
        return medicationApiService.getAllPills();
    }


    //  제품 상세 조회
    @GetMapping("/{pillName}")
    public ResponseEntity<Map<String, Object>> getPillDetail(@PathVariable String pillName) {
        return medicationApiService.searchPillDetail(pillName);
    }

    // 찜 추가
    @PostMapping("/favorite")
    public ResponseEntity<Map<String, Object>> addFavorite(@RequestBody Map<String, Object> requestData) {
        return medicationApiService.addFavoritePill(requestData);
    }
}
