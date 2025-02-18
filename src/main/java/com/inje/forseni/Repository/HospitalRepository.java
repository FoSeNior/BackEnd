package com.inje.forseni.Repository;

import com.inje.forseni.Entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    @Query("SELECT h FROM Hospital h WHERE h.hName = :hName AND h.hAddress = :hAddress AND h.hNumber = :hNumber")
    Optional<Hospital> findByHospitalDetails(@Param("hName") String hName, @Param("hAddress") String hAddress, @Param("hNumber") String hNumber);
    @Query("SELECT COUNT(h) > 0 FROM Hospital h WHERE h.hName = :hName AND h.hAddress = :hAddress AND h.hNumber = :hNumber")
    boolean existsByHospital(@Param("hName") String hName, @Param("hAddress") String hAddress, @Param("hNumber") String hNumber);
}