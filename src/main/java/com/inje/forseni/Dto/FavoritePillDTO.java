package com.inje.forseni.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritePillDTO {
    private Integer favId;
    private Long itemSeq; // 고유 품목번호
    private String itemName;
    private String entpName;
    private String efcyQesitm;
    private String atpnWarnQesitm;
    private String atpnQesitm;
    private String itemImage;


}
