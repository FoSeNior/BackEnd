package com.inje.forseni.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyHospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospitalAlarmId")
    private int hospitalAlarmId;

    private String date;
    private int hourTime;
    private int minTime;
    private String hospitalAlarmDetail;
    private String addMemo;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "hospitalId", nullable = false)
    private Hospital hospital;

}