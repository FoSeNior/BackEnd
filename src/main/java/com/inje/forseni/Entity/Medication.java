package com.inje.forseni.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "MEDICATION")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id")
    private Integer medicineId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "medicine_name", nullable = false, length = 30)
    private String medicineName;

    @Column(name = "medication_date", length = 10, nullable = true)
    private String medicationDate;

    @Column(name = "medicine_time", nullable = false)
    private Integer medicineTime;

    @Column(name = "medicine_memo", length = 100)
    private String medicineMemo;

    @Column(name = "start_day", nullable = false, length = 10)
    private String startDay;

    @Column(name = "end_day", nullable = false, length = 10)
    private String endDay;

}
