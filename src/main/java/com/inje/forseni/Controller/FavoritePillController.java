package com.inje.forseni.Controller;

import com.inje.forseni.Entity.FavoritePill;
import com.inje.forseni.Service.FavoritePillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favorite")
public class FavoritePillController {

    private final FavoritePillService favoritePillService;

    public FavoritePillController(FavoritePillService favoritePillService) {
        this.favoritePillService = favoritePillService;
    }

    // 찜 목록 조회
    @GetMapping("/pill/{u_id}")
    public ResponseEntity<Map<String, Object>> getFavoriteList(@PathVariable("u_id") int userId) {
        return favoritePillService.getFavoritePills(userId);
    }

    // 찜 삭제 기능
    @DeleteMapping("/remove")
    public ResponseEntity<Map<String, Object>> removeFavorite(@RequestBody Map<String, Object> requestData) {
        return favoritePillService.removeFavoritePill(requestData);
    }
}
