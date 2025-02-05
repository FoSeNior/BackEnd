package com.inje.forseni.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int hospitalId;

    private String hName;
    private String hAddress;
    private String hNumber;

    // Hospital 엔티티에서 MyHospital 엔티티로의 관계를 매핑
    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY)
    private List<MyHospital> myHospitals;
}
