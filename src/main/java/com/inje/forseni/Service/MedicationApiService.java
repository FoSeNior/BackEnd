package com.inje.forseni.Service;

import com.inje.forseni.Entity.FavoritePill;
import com.inje.forseni.Entity.User;
import com.inje.forseni.Repository.FavoritePillRepository;
import com.inje.forseni.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final FavoritePillRepository favoritePillRepository;

    private final UserRepository userRepository;

    public MedicationApiService(FavoritePillRepository favoritePillRepository,UserRepository userRepository) {
        this.favoritePillRepository = favoritePillRepository;
        this.userRepository = userRepository;
    }

    //  제품 리스트 조회 (이름 + 효능)
    public List<Map<String, String>> searchPillList(String pillName) {
        JSONArray dataArray = fetchPillData(pillName);

        if (dataArray.isEmpty()) {
            return Collections.singletonList(Map.of(
                    "itemName", "정보 없음",
                    "efcyQesitm", "정보 없음"
            ));
        }

        List<Map<String, String>> results = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject item = dataArray.getJSONObject(i);
            results.add(Map.of(
                    "itemName", item.optString("itemName", "정보 없음"),
                    "efcyQesitm", item.optString("efcyQesitm", "정보 없음")
            ));
        }

        return results;
    }

    //  제품 상세 조회
    public Map<String, String> searchPillDetail(String pillName) {
        JSONArray dataArray = fetchPillData(pillName);

        if (dataArray.isEmpty()) {
            return Map.of("error", "해당 약 정보를 찾을 수 없습니다.");
        }

        JSONObject item = dataArray.getJSONObject(0);
        return Map.of(
                "itemSeq", item.optString("itemSeq", "정보 없음"),
                "itemName", item.optString("itemName", "정보 없음"),
                "entpName", item.optString("entpName", "정보 없음"),
                "efcyQesitm", item.optString("efcyQesitm", "정보 없음"),
                "atpnWarnQesitm", item.optString("atpnWarnQesitm", "정보 없음"),
                "atpnQesitm", item.optString("atpnQesitm", "정보 없음"),
                "itemImage", item.optString("itemImage", "이미지 없음")
        );
    }

    //  찜 기능 (uid와 itemSeq만으로 자동 저장)
    @Transactional
    public String addFavoritePill(Integer userId, Long itemSeq) {
        Optional<JSONObject> pillDetailOpt = getPillDetail(itemSeq);

        if (pillDetailOpt.isEmpty()) {
            return "해당 약 정보를 찾을 수 없습니다.";
        }

        JSONObject item = pillDetailOpt.get();

        // 데이터 저장
        FavoritePill favoritePill = new FavoritePill();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        favoritePill.setUser(user);
        favoritePill.setItemSeq(itemSeq);
        favoritePill.setItemName(item.optString("itemName", "정보 없음"));
        favoritePill.setEntpName(item.optString("entpName", "정보 없음"));
        favoritePill.setEfcyQesitm(item.optString("efcyQesitm", "정보 없음"));
        favoritePill.setAtpnWarnQesitm(item.optString("atpnWarnQesitm", "정보 없음"));
        favoritePill.setAtpnQesitm(item.optString("atpnQesitm", "정보 없음"));
        favoritePill.setItemImage(item.optString("itemImage", "이미지 없음"));

        favoritePillRepository.save(favoritePill);

        return favoritePill.getItemName() + "이(가) 찜 목록에 추가되었습니다.";
    }

    // itemSeq 기반으로 약 상세 정보 가져오기
    public Optional<JSONObject> getPillDetail(Long itemSeq) {
        JSONArray dataArray = fetchPillData(itemSeq.toString());

        if (dataArray.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(dataArray.getJSONObject(0));
    }

    //  API 호출 및 데이터 처리 함수
    private JSONArray fetchPillData(String queryParam) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            String url;
            if (queryParam.matches("\\d+")) { // 숫자로만 이루어진 경우 itemSeq로 검색
                url = API_URL + "?serviceKey=" + API_KEY + "&itemSeq=" + queryParam;
            } else { // 그렇지 않으면 itemName으로 검색
                url = API_URL + "?serviceKey=" + API_KEY + "&itemName=" + URLEncoder.encode(queryParam, StandardCharsets.UTF_8);
            }

            System.out.println("✅ API 요청 URL: " + url);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String responseBody = response.getBody();
            System.out.println("✅ API 원본 응답:\n" + responseBody);

            JSONObject jsonResponse = XML.toJSONObject(responseBody);

            if (!jsonResponse.has("response")) return new JSONArray();
            JSONObject responseObj = jsonResponse.getJSONObject("response");

            if (!responseObj.has("body")) return new JSONArray();
            JSONObject bodyObj = responseObj.getJSONObject("body");

            if (!bodyObj.has("items")) return new JSONArray();
            Object items = bodyObj.get("items");

            if (items instanceof JSONObject) {
                JSONObject itemsObj = (JSONObject) items;
                if (itemsObj.has("item")) {
                    return itemsObj.get("item") instanceof JSONObject
                            ? new JSONArray().put(itemsObj.getJSONObject("item"))
                            : itemsObj.getJSONArray("item");
                }
            }
            return new JSONArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}