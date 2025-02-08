package com.inje.forseni.Controller;

import com.inje.forseni.Dto.MedicationDTO;
import com.inje.forseni.Service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/alarm/pill")
public class MedicationController {

    private final MedicationService medicationService;

    @Autowired
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    // 하루 투약 정보 조회
    @GetMapping("/day/{u_id}/{medication_date}")
    public ResponseEntity<Map<String, Object>> getDailyPillAlarm(@PathVariable("u_id") int userId , @PathVariable("medication_date") String medicationDate) {
        return medicationService.getDailyPillAlarm(userId, medicationDate);
    }

    // 전체 투약 정보 조회
    @GetMapping("/all/{u_id}")
    public ResponseEntity<Map<String, Object>> getAllPillAlarms(@PathVariable("u_id") int userId) {
        return medicationService.getAllPillAlarms(userId);
    }

    //  약물 추가
    @PostMapping("/{u_id}")
    public ResponseEntity<Map<String, Object>> addMedication(@PathVariable("u_id") int userId, @RequestBody MedicationDTO medicationDTO) {
        return medicationService.addMedication(userId, medicationDTO);
    }

    //  약물 수정
    @PutMapping("/{u_id}/{medicine_id}")
    public ResponseEntity<Map<String, Object>> updateMedication(@PathVariable("u_id") int userId, @PathVariable("medicine_id") int medicineId, @RequestBody MedicationDTO medicationDTO) {
        return medicationService.updateMedication(userId, medicineId, medicationDTO);
    }

    //  약물 삭제
    @DeleteMapping("/{u_id}/{medicine_id}")
    public ResponseEntity<Map<String, Object>> deleteMedication(@PathVariable("u_id") int userId, @PathVariable("medicine_id") int medicineId) {
        return medicationService.deleteMedication(userId, medicineId);
    }
}
