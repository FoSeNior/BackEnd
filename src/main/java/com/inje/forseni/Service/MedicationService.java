package com.inje.forseni.Service;

import com.inje.forseni.Dto.MedicationDTO;
import com.inje.forseni.Entity.Medication;
import com.inje.forseni.Entity.User;
import com.inje.forseni.Repository.MedicationRepository;
import com.inje.forseni.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Comparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository, UserRepository userRepository) {
        this.medicationRepository = medicationRepository;
        this.userRepository = userRepository;
    }

    //  하루 투약 정보 조회
    public ResponseEntity<Map<String, Object>> getDailyPillAlarm(Integer userId, String medicationDate) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "사용자를 찾을 수 없습니다."
            ));
        }

        //  DB에서 데이터 조회
        int alarmCount = medicationRepository.countByUserAndMedicationDate(user.get(), medicationDate);
        List<Medication> meds = medicationRepository.findByUserAndMedicationDate(user.get(), medicationDate);

        //시간순 정렬
        meds.sort(Comparator.comparingInt(Medication::getMedicineTime));

        List<Map<String, Object>> alarms = new ArrayList<>();
        for (Medication med : meds) {
            alarms.add(Map.of(
                    "date", med.getMedicationDate(),
                    "hourTime", med.getMedicineTime() / 100,
                    "minTime", med.getMedicineTime() % 100,
                    "pillAlarmDetail", med.getMedicineName(),
                    "addMemo", med.getMedicineMemo() != null ? med.getMedicineMemo() : ""
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "하루 투약 정보를 성공적으로 조회했습니다.",
                "data", Map.of(
                        "dailyPillAlarmCount", alarmCount,
                        "alarms", alarms
                )
        ));
    }

    //  전체 투약 정보 조회
    public ResponseEntity<Map<String, Object>> getAllPillAlarms(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "사용자를 찾을 수 없습니다."
            ));
        }

        List<Medication> meds = medicationRepository.findByUser(user.get());

        //  날짜 + 시간순 정렬 추가
        meds.sort(Comparator.comparing(Medication::getMedicationDate)
                .thenComparingInt(Medication::getMedicineTime));

        List<Map<String, Object>> alarms = new ArrayList<>();
        for (Medication med : meds) {
            alarms.add(Map.of(
                    "date", med.getMedicationDate(),
                    "hourTime", med.getMedicineTime() / 100,
                    "minTime", med.getMedicineTime() % 100,
                    "pillAlarmDetail", med.getMedicineName(),
                    "addMemo", med.getMedicineMemo() != null ? med.getMedicineMemo() : ""
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "전체 투약 정보를 성공적으로 조회했습니다.",
                "data", Map.of("alarms", alarms)
        ));
    }

    //  약물 추가
    public ResponseEntity<Map<String, Object>> addMedication(Integer userId, MedicationDTO medicationDTO) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "사용자를 찾을 수 없습니다."
            ));
        }

        if (medicationDTO.getMedicineName() == null || medicationDTO.getMedicineName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "medicineName이 누락되었습니다!"
            ));
        }

        Medication medication = new Medication();
        medication.setUser(user.get());
        medication.setMedicineName(medicationDTO.getMedicineName());
        medication.setMedicineTime(medicationDTO.getHourTime() * 100 + medicationDTO.getMinTime());
        medication.setMedicineMemo(medicationDTO.getMedicineMemo());

        // medicationDate가 없으면 startDay로 설정
        if (medicationDTO.getMedicationDate() == null || medicationDTO.getMedicationDate().trim().isEmpty()) {
            medication.setMedicationDate(medicationDTO.getStartDay());
        } else {
            medication.setMedicationDate(medicationDTO.getMedicationDate());
        }

        medication.setStartDay(medicationDTO.getStartDay());
        medication.setEndDay(medicationDTO.getEndDay());

        // 저장
        medication = medicationRepository.save(medication);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "알람을 성공적으로 등록했습니다",
                "data", Map.of(
                        "pillAlarm_id", medication.getMedicineId(),
                        "u_id", medication.getUser().getUserId(),
                        "startDay", medication.getStartDay(),
                        "endDay", medication.getEndDay(),
                        "medicationDate", medication.getMedicationDate(),
                        "hourTime", medication.getMedicineTime() / 100,
                        "minTime", medication.getMedicineTime() % 100,
                        "pillAlarmDetail", medication.getMedicineName(),
                        "addMemo", medication.getMedicineMemo()
                )
        ));
    }

    //  약물 수정
    public ResponseEntity<Map<String, Object>> updateMedication(Integer userId, Integer medicineId, MedicationDTO medicationDTO) {
        Optional<Medication> medicationOpt = medicationRepository.findById(medicineId);
        if (medicationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "해당 약물 알람을 찾을 수 없습니다."
            ));
        }

        Medication medication = medicationOpt.get();

        medication.setMedicineName(medicationDTO.getMedicineName());
        medication.setMedicineTime(medicationDTO.getHourTime() * 100 + medicationDTO.getMinTime());
        medication.setMedicineMemo(medicationDTO.getMedicineMemo());

        //  medicationDate가 없으면 startDay로 설정
        if (medicationDTO.getMedicationDate() == null || medicationDTO.getMedicationDate().trim().isEmpty()) {
            medication.setMedicationDate(medicationDTO.getStartDay());
        } else {
            medication.setMedicationDate(medicationDTO.getMedicationDate());
        }

        medication.setStartDay(medicationDTO.getStartDay());
        medication.setEndDay(medicationDTO.getEndDay());

        // 저장
        medication = medicationRepository.save(medication);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "알람을 성공적으로 수정했습니다.",
                "data", Map.of(
                        "pillAlarm_id", medication.getMedicineId(),
                        "u_id", medication.getUser().getUserId(),
                        "startDay", medication.getStartDay(),
                        "endDay", medication.getEndDay(),
                        "medicationDate", medication.getMedicationDate(),
                        "hourTime", medication.getMedicineTime() / 100,
                        "minTime", medication.getMedicineTime() % 100,
                        "pillAlarmDetail", medication.getMedicineName(),
                        "addMemo", medication.getMedicineMemo()
                )
        ));
    }

    //  약물 삭제
    public ResponseEntity<Map<String, Object>> deleteMedication(Integer userId, Integer medicineId) {
        Optional<Medication> medicationOpt = medicationRepository.findById(medicineId);
        if (medicationOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "해당 약물 알람을 찾을 수 없습니다."
            ));
        }

        Medication medication = medicationOpt.get();

        // 삭제 전에 u_id 가져오기
        userId = medication.getUser().getUserId();

        //  삭제
        medicationRepository.delete(medication);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "알람을 성공적으로 삭제했습니다.",
                "data", Map.of(
                        "pillAlarm_id", medicineId,
                        "u_id", userId
                )
        ));
    }
}
