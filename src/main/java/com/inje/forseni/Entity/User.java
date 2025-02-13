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
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    @Column(name = "membership_id")
    private String membershipId;
    @Column(name = "userName")
    private String userName;
    @Column(name = "age")
    private int age;
    @Column(name = "password")
    private String password;

    // User 엔티티에서 MyHospital 엔티티로의 관계를 매핑
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<MyHospital> myHospitals;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medication> medications;

    // User 엔티티에서 FavoritePill 엔티티로의 관계 매핑
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoritePill> favoritePills;
}

