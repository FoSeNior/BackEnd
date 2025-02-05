package com.inje.forseni.Controller;

import com.inje.forseni.Dto.HospitalAlarmDayResponseDTO;
import com.inje.forseni.Dto.HospitalAlarmListResponseDTO;
import com.inje.forseni.Dto.MyHospitalRequestDTO;
import com.inje.forseni.Service.MyHospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/alarm/hospital")
@RequiredArgsConstructor
public class MyHospitalController {
    private final MyHospitalService myHospitalService;

    //병원 알람
    @PostMapping("/{u_id}")
    public ResponseEntity<String> createHospitalAlarm(
            @PathVariable("u_id") int userId,
            @RequestBody MyHospitalRequestDTO requestDto) {

        String responseMessage = myHospitalService.createHospitalAlarm(userId, requestDto);

        return ResponseEntity.ok(responseMessage);
    }
    // 유저 병원 예약 정보 조회
    @GetMapping("/all/{u_id}")
    public ResponseEntity<HospitalAlarmListResponseDTO> getHospitalAlarmsByUserId(
            @PathVariable("u_id") int userId) {

        HospitalAlarmListResponseDTO response = myHospitalService.getHospitalAlarmsByUserId(userId);

        return ResponseEntity.ok(response);
    }
    // 당일 병원 예약 정보 조회
    @GetMapping("/day/{u_id}/{date}")
    public ResponseEntity<HospitalAlarmDayResponseDTO> getHospitalAlarmsByUserIdAndDate(
            @PathVariable("u_id") int userId,
            @PathVariable("date") String date) {

        HospitalAlarmDayResponseDTO response = myHospitalService.getHospitalAlarmsByUserIdAndDate(userId, date);

        return ResponseEntity.ok(response);
    }
    // 병원 예약 정보 업데이트
    @PutMapping("/{u_id}/{hospitalAlarm_id}")
    public ResponseEntity<String> updateHospitalAlarm(
            @PathVariable("u_id") int userId,
            @PathVariable("hospitalAlarm_id") int hospitalAlarmId,
            @RequestBody MyHospitalRequestDTO requestDto) {

        String responseMessage = myHospitalService.updateHospitalAlarm(userId, hospitalAlarmId, requestDto);

        return ResponseEntity.ok(responseMessage);
    }
    //병원 예약 알람 삭제
    @DeleteMapping("/{u_id}/{hospitalAlarm_id}")
    public ResponseEntity<Map<String,Object>> deleteHospitalAlarm(
            @PathVariable("u_id") int userId,
            @PathVariable("hospitalAlarm_id") int hospitalAlarmId){
        return myHospitalService.deleteHospitalAlarm(userId,hospitalAlarmId);
    }
}
