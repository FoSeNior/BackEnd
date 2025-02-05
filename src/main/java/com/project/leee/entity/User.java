package com.project.leee.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_id")
    private Integer uId;

    @Column(name = "user_id", nullable = false, unique = true, length = 50)
    private String userId;

    @Column(name = "user_name", nullable = false, length = 30)
    private String userName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(name = "font_size", nullable = false)
    private Integer fontSize;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medication> medications;
}
