package com.inje.forseni.Repository;

import com.inje.forseni.Entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    //boolean existsByhNameAndhAddressAndhNumber(String hName, String hAddress, String hNumber);

    @Query("SELECT h FROM Hospital h WHERE h.hName = :hName AND h.hAddress = :hAddress AND h.hNumber = :hNumber")
    Optional<Hospital> findByHospitalDetails(@Param("hName") String hName, @Param("hAddress") String hAddress, @Param("hNumber") String hNumber);

    @Query("select  count(h) > 0 from Hospital h where h.hName = :hName and h.hAddress = :hAddress and h.hNumber = :hNumber")
    boolean existsByHospital(@Param("hName") String hName, @Param("hAddress")String hAddress, @Param("hNumber") String hNumber);
}