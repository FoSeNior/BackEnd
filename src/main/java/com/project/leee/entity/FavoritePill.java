package com.project.leee.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "favorite_pill", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"u_id", "item_seq"}) // 중복 방지
})
public class FavoritePill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fav_id")
    private Integer favId;

    @Column(name = "u_id", nullable = false)
    private Integer uid;

    @Column(name = "item_seq", nullable = false)
    private Long itemSeq; // 고유 품목번호 (중복 방지)

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "entp_name")
    private String entpName;

    @Column(name = "efcy_qesitm", columnDefinition = "TEXT")
    private String efcyQesitm;

    @Column(name = "atpn_warn_qesitm", columnDefinition = "TEXT")
    private String atpnWarnQesitm;

    @Column(name = "atpn_qesitm", columnDefinition = "TEXT")
    private String atpnQesitm;

    @Column(name = "item_image")
    private String itemImage;
}
