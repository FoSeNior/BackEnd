package com.inje.forseni.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "api_pill")  // DB 테이블 이름 지정
public class ApiPill {

    @Id
    private Long itemSeq;

    @Column(columnDefinition = "TEXT")
    private String itemName;

    @Column(length = 255)
    private String entpName;

    @Column(columnDefinition = "TEXT") // 🔥 긴 데이터 저장을 위해 TEXT 타입 사용
    private String efcyQesitm;

    @Column(columnDefinition = "TEXT") // 🔥 긴 데이터 저장을 위해 TEXT 타입 사용
    private String atpnWarnQesitm;

    @Column(columnDefinition = "TEXT") // 🔥 긴 데이터 저장을 위해 TEXT 타입 사용
    private String atpnQesitm;

    @Column(length = 500)
    private String itemImage;
}
