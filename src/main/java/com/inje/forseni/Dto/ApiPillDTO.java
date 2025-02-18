package com.inje.forseni.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiPillDTO {
    private Long itemSeq;      // 고유 품목번호
    private String itemName;   // 약 이름
    private String entpName;   // 제조사명
    private String efcyQesitm; // 효능
    private String atpnWarnQesitm; // 경고사항
    private String atpnQesitm; // 주의사항
    private String itemImage;  // 약 이미지 URL
}
