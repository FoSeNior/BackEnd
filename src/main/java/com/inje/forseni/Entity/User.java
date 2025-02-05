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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String nick;
    private int age;
    private String password;
    private int fontSize;

    // User 엔티티에서 MyHospital 엔티티로의 관계를 매핑
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<MyHospital> myHospitals;

}
