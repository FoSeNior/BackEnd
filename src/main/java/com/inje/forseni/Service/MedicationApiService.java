package com.inje.forseni.Service;

import com.inje.forseni.Entity.ApiPill;
import com.inje.forseni.Entity.FavoritePill;
import com.inje.forseni.Entity.User;
import com.inje.forseni.Repository.ApiPillRepository;
import com.inje.forseni.Repository.FavoritePillRepository;
import com.inje.forseni.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class MedicationApiService {

    @Value("${MedicationApi.url}")
    private String API_URL;

    @Value("${MedicationKey.url}")
    private String API_KEY;

    private final ApiPillRepository apiPillRepository;
    private final FavoritePillRepository favoritePillRepository;
    private final UserRepository userRepository;

    public MedicationApiService(ApiPillRepository apiPillRepository,
                                FavoritePillRepository favoritePillRepository,
                                UserRepository userRepository) {
        this.apiPillRepository = apiPillRepository;
        this.favoritePillRepository = favoritePillRepository;
        this.userRepository = userRepository;
    }

    // API에서 모든 약 데이터를 가져와 DB에 저장
    @Transactional
    public ResponseEntity<Map<String, Object>> loadAllPills() {
        JSONArray dataArray = fetchAllPillData();
        int savedCount = savePillDataToDB(dataArray);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "API에서 모든 데이터를 가져와 DB에 저장 완료",
                "savedCount", savedCount
        ));
    }

    // ✅ 모든 약 목록 조회 (추가된 메서드)
    public ResponseEntity<Map<String, Object>> getAllPills() {
        List<ApiPill> pills = apiPillRepository.findAll();

        if (pills.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "message", "등록된 약물이 없습니다.",
                    "data", Collections.emptyList()
            ));
        }

        List<Map<String, String>> results = new ArrayList<>();
        for (ApiPill pill : pills) {
            results.add(Map.of(
                    "itemName", pill.getItemName(),
                    "efcyQesitm", pill.getEfcyQesitm()
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "모든 약의 정보를 성공적으로 조회했습니다.",
                "data", results
        ));
    }

    // DB에서 약 목록 조회
    public ResponseEntity<Map<String, Object>> searchPillList(String pillName) {
        List<ApiPill> pills = apiPillRepository.findByItemNameContainingIgnoreCase(pillName);

        if (pills.isEmpty()) {
            JSONArray dataArray = fetchPillData(pillName);
            savePillDataToDB(dataArray);
            pills = apiPillRepository.findByItemNameContainingIgnoreCase(pillName);
        }

        List<Map<String, String>> results = new ArrayList<>();
        for (ApiPill pill : pills) {
            results.add(Map.of(
                    "itemName", pill.getItemName(),
                    "efcyQesitm", pill.getEfcyQesitm()
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", !results.isEmpty(),
                "message", results.isEmpty() ? "해당 약물이 없습니다." : "제품 목록 조회 성공",
                "data", results
        ));
    }

    //  DB에서 약 상세 조회
    public ResponseEntity<Map<String, Object>> searchPillDetail(String pillName) {
        Optional<ApiPill> pillOpt = apiPillRepository.findByItemName(pillName);

        if (pillOpt.isEmpty()) {
            JSONArray dataArray = fetchPillData(pillName);
            savePillDataToDB(dataArray);
            pillOpt = apiPillRepository.findByItemName(pillName);
        }

        return ResponseEntity.ok(Map.of(
                "success", pillOpt.isPresent(),
                "message", pillOpt.isPresent() ? "제품 상세 조회 성공" : "해당 약물이 없습니다.",
                "data", pillOpt.orElse(null)
        ));
    }

    // 찜 추가 기능
    @Transactional
    public ResponseEntity<Map<String, Object>> addFavoritePill(Map<String, Object> requestData) {
        Integer userId;
        Long itemSeq;

        //  요청 데이터 검증
        try {
            userId = (Integer) requestData.get("userId");
            itemSeq = ((Number) requestData.get("itemSeq")).longValue();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "userId 또는 itemSeq가 올바르지 않습니다."
            ));
        }

        //  유저 존재 여부 확인
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "해당 userId를 가진 유저가 존재하지 않습니다."
            ));
        }

        // 이미 찜 목록에 있는지 확인
        boolean alreadyExists = favoritePillRepository.existsByUser_UserIdAndItemSeq(userId, itemSeq);
        if (alreadyExists) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "이미 찜 목록에 존재하는 아이템입니다."
            ));
        }

        //  약 정보 조회
        Optional<ApiPill> pillOpt = apiPillRepository.findById(itemSeq);
        if (pillOpt.isEmpty()) {
            JSONArray dataArray = fetchPillData(itemSeq.toString());
            savePillDataToDB(dataArray);
            pillOpt = apiPillRepository.findById(itemSeq);
        }

        //  itemSeq가 존재하지 않는 경우
        if (pillOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "해당 itemSeq에 대한 약 정보를 찾을 수 없습니다."
            ));
        }

        // 찜 목록에 추가
        FavoritePill favoritePill = new FavoritePill();
        favoritePill.setUser(userOpt.get());
        favoritePill.setItemSeq(itemSeq);
        favoritePill.setItemName(pillOpt.get().getItemName());
        favoritePill.setEntpName(pillOpt.get().getEntpName());
        favoritePill.setEfcyQesitm(pillOpt.get().getEfcyQesitm());
        favoritePill.setAtpnWarnQesitm(pillOpt.get().getAtpnWarnQesitm());
        favoritePill.setAtpnQesitm(pillOpt.get().getAtpnQesitm());
        favoritePill.setItemImage(pillOpt.get().getItemImage());

        favoritePillRepository.save(favoritePill);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", favoritePill.getItemName() + "이(가) 찜 목록에 추가되었습니다."
        ));
    }

    // JPA 트랜잭션 강제 커밋 추가
    @Transactional
    public int savePillDataToDB(JSONArray dataArray) {
        List<ApiPill> pillList = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject item = dataArray.optJSONObject(i);
            if (item == null) continue;

            ApiPill apiPill = new ApiPill(item.optLong("itemSeq"), item.optString("itemName"),
                    item.optString("entpName"), item.optString("efcyQesitm"),
                    item.optString("atpnWarnQesitm"), item.optString("atpnQesitm"),
                    item.optString("itemImage"));

            pillList.add(apiPill);
        }
        apiPillRepository.saveAll(pillList);
        apiPillRepository.flush(); // 트랜잭션 강제 커밋

        return pillList.size();
    }

    //  API 호출 및 데이터 변환
    private JSONArray fetchAllPillData() {
        return fetchApiData("");
    }

    private JSONArray fetchPillData(String queryParam) {
        return fetchApiData("&itemName=" + URLEncoder.encode(queryParam, StandardCharsets.UTF_8));
    }

    private JSONArray fetchApiData(String queryParam) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = API_URL + "?serviceKey=" + API_KEY + queryParam;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject jsonResponse = XML.toJSONObject(response.getBody());

            System.out.println("🔍 API 응답 데이터: " + jsonResponse.toString(2));

            return jsonResponse.optJSONObject("response")
                    .optJSONObject("body")
                    .optJSONObject("items")
                    .optJSONArray("item");

        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}