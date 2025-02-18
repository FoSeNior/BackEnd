package com.inje.forseni.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "MY_HOSPITAL", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "hospital_id"}))
public class MyHospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospitalAlarm_id")
    private int hospitalAlarmId;

    private String date;
    private int hourTime;
    private int minTime;
    private String hospitalAlarmDetail;
    private String addMemo;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

}