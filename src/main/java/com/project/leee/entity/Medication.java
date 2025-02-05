package com.project.leee.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@Table(name = "medication")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medicine_id")
    private Integer medicineId;

    @ManyToOne
    @JoinColumn(name = "u_id", nullable = false)
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

    //  기본 생성자에서 medicationDate 초기화
    public Medication() {
        if (this.medicationDate == null || this.medicationDate.trim().isEmpty()) {
            this.medicationDate = this.startDay;
        }
    }

    //  @PrePersist -> medicationDate 자동 설정
    @PrePersist
    public void prePersist() {
        if (this.medicationDate == null || this.medicationDate.trim().isEmpty()) {
            System.out.println("📌 @PrePersist 실행됨: medicationDate가 NULL이므로 startDay로 설정");
            this.medicationDate = this.startDay;
        }
    }
}
