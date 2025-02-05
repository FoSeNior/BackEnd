package com.project.leee.controller;

import com.project.leee.dto.MedicationDTO;
import com.project.leee.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/alarm/pill")
public class MedicationController {

    private final MedicationService medicationService;

    @Autowired
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    // 하루 투약 정보 조회
    @GetMapping("/day/{u_id}/{medication_date}")
    public ResponseEntity<Map<String, Object>> getDailyPillAlarm(@PathVariable Integer u_id, @PathVariable String medication_date) {
        return medicationService.getDailyPillAlarm(u_id, medication_date);
    }

    // 전체 투약 정보 조회
    @GetMapping("/all/{u_id}")
    public ResponseEntity<Map<String, Object>> getAllPillAlarms(@PathVariable Integer u_id) {
        return medicationService.getAllPillAlarms(u_id);
    }

    //  약물 추가
    @PostMapping("/{u_id}")
    public ResponseEntity<Map<String, Object>> addMedication(@PathVariable Integer u_id, @RequestBody MedicationDTO medicationDTO) {
        return medicationService.addMedication(u_id, medicationDTO);
    }

    //  약물 수정
    @PutMapping("/{u_id}/{medicine_id}")
    public ResponseEntity<Map<String, Object>> updateMedication(@PathVariable Integer u_id, @PathVariable Integer medicine_id, @RequestBody MedicationDTO medicationDTO) {
        return medicationService.updateMedication(u_id, medicine_id, medicationDTO);
    }

    //  약물 삭제
    @DeleteMapping("/{u_id}/{medicine_id}")
    public ResponseEntity<Map<String, Object>> deleteMedication(@PathVariable Integer u_id, @PathVariable Integer medicine_id) {
        return medicationService.deleteMedication(u_id, medicine_id);
    }
}
