package com.inje.forseni.Controller;

import com.inje.forseni.Entity.FavoritePill;
import com.inje.forseni.Service.FavoritePillService;
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

    //  찜 목록 조회
    @GetMapping("/list/{u_id}")
    public List<FavoritePill> getFavoriteList(@PathVariable("u_id") int userId) {
        return favoritePillService.getFavoritePills(userId);
    }
    // 찜 삭제 기능
    @DeleteMapping("/remove")
    public String removeFavorite(@RequestBody Map<String, Object> requestData) {
        // JSON에서 Long, Integer를 올바르게 변환
        Integer userId = Integer.parseInt(requestData.get("u_id").toString());
        Long itemSeq = Long.parseLong(requestData.get("itemSeq").toString());

        return favoritePillService.removeFavoritePill(userId, itemSeq);
    }
}
