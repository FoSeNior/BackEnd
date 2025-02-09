package com.inje.forseni.Repository;

import com.inje.forseni.Entity.Medication;
import com.inje.forseni.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {

    //  특정 날짜의 투약 정보 조회
    @Query("SELECT m FROM Medication m WHERE m.user = :user AND m.medicationDate = :medicationDate")
    List<Medication> findByUserAndMedicationDate(@Param("user") User user, @Param("medicationDate") String medicationDate);

    //  특정 날짜의 알람 개수 조회
    @Query("SELECT COUNT(m) FROM Medication m WHERE m.user = :user AND m.medicationDate = :medicationDate")
    int countByUserAndMedicationDate(@Param("user") User user, @Param("medicationDate") String medicationDate);

    //  사용자의 모든 투약 정보 조회
    List<Medication> findByUser(User userId);


}
