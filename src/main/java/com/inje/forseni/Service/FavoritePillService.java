package com.inje.forseni.Service;

import com.inje.forseni.Entity.FavoritePill;
import com.inje.forseni.Repository.FavoritePillRepository;
import com.inje.forseni.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FavoritePillService {

    private final FavoritePillRepository favoritePillRepository;
    private final UserRepository userRepository;

    public FavoritePillService(FavoritePillRepository favoritePillRepository, UserRepository userRepository) {
        this.favoritePillRepository = favoritePillRepository;
        this.userRepository = userRepository;
    }


    // 찜 목록 가져오기
    public List<FavoritePill> getFavoritePills(Integer userId) {
        return favoritePillRepository.findByUser_UserId(userId); //  uId 기반으로 검색하도록 변경
    }

    //  찜 삭제 기능 추가
    @Transactional
    public String removeFavoritePill(Integer userId, Long itemSeq) {
        Optional<FavoritePill> favoritePill = favoritePillRepository.findByUser_UserId(userId)
                .stream()
                .filter(pill -> pill.getItemSeq().equals(itemSeq))
                .findFirst();

        if (favoritePill.isPresent()) {
            favoritePillRepository.deleteByUser_UserIdAndItemSeq(userId, itemSeq);
            return "찜 목록에서 삭제되었습니다.";
        } else {
            return "해당 찜 목록이 존재하지 않습니다.";
        }
    }
}
