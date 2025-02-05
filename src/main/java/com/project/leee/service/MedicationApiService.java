package com.project.leee.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.transaction.Transactional;

import com.project.leee.entity.FavoritePill;
import com.project.leee.repository.FavoritePillRepository;

@Service
public class MedicationApiService {

    private static final String API_URL = "http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList";
    private static final String API_KEY = "hmMf8tBh7F7wZKI/hArP0M0bDg4MeA88l5SNmpLEPKS6FcgagVJKKedo23ux05tbu4zA92Ekqo5ku19S4kMm2w==";

    private final FavoritePillRepository favoritePillRepository;


    public MedicationApiService(FavoritePillRepository favoritePillRepository) {
        this.favoritePillRepository = favoritePillRepository;
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
    public String addFavoritePill(Integer uid, Long itemSeq) {
        Optional<JSONObject> pillDetailOpt = getPillDetail(itemSeq);

        if (pillDetailOpt.isEmpty()) {
            return "해당 약 정보를 찾을 수 없습니다.";
        }

        JSONObject item = pillDetailOpt.get();

        // 데이터 저장
        FavoritePill favoritePill = new FavoritePill();
        favoritePill.setUid(uid);
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