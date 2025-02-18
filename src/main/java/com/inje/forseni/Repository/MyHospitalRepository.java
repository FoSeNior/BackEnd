package com.inje.forseni.Repository;

import com.inje.forseni.Entity.Hospital;
import com.inje.forseni.Entity.MyHospital;
import com.inje.forseni.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MyHospitalRepository extends JpaRepository<MyHospital, Integer> {
    List<MyHospital> findByUser_UserId(int userId);
    List<MyHospital> findByUser_UserIdAndDate(int userId, String date);

    List<MyHospital> findByUserAndHospital(User user, Hospital hospital);
    boolean existsByUserAndHospital(User user, Hospital hospital);

}