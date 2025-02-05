package com.project.leee.controller;

import com.project.leee.service.MedicationApiService;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search/pill")
public class MedicationApiController {

    private final MedicationApiService medicationApiService;

    public MedicationApiController(MedicationApiService medicationApiService) {
        this.medicationApiService = medicationApiService;
    }

    //  제품명 목록 전체 조회
    @GetMapping("/all/{pillName}")
    public List<Map<String, String>> getPillList(@PathVariable String pillName) {
        return medicationApiService.searchPillList(pillName);
    }

    //  제품 상세 조회
    @GetMapping("/{pillName}")
    public Map<String, String> getPillDetail(@PathVariable String pillName) {
        return medicationApiService.searchPillDetail(pillName);
    }

    //  찜 추가 itemSeq와 u_id로
    @PostMapping("/favorite")
    public String addFavorite(@RequestBody Map<String, Object> requestData) {
        Integer uId = (Integer) requestData.get("u_id");
        Long itemSeq = ((Number) requestData.get("itemSeq")).longValue();

        return medicationApiService.addFavoritePill(uId, itemSeq);
    }
}

