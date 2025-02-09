package com.inje.forseni.Service;


import com.inje.forseni.Dto.HospitalAlarmDayResponseDTO;
import com.inje.forseni.Dto.HospitalAlarmListResponseDTO;
import com.inje.forseni.Dto.HospitalAlarmResponseDTO;
import com.inje.forseni.Dto.MyHospitalRequestDTO;
import com.inje.forseni.Entity.Hospital;
import com.inje.forseni.Entity.MyHospital;
import com.inje.forseni.Entity.User;
import com.inje.forseni.Repository.HospitalRepository;
import com.inje.forseni.Repository.MyHospitalRepository;
import com.inje.forseni.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyHospitalService {
    private final MyHospitalRepository myHospitalRepository;
    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;

    public String createHospitalAlarm(int userId, MyHospitalRequestDTO requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Hospital hospital = hospitalRepository.findById(requestDto.getHospitalId())
                .orElseThrow(() -> new IllegalArgumentException("해당 병원이 존재하지 않습니다."));

        MyHospital hospitalAlarm = MyHospital.builder()
                .user(user)
                .hospital(hospital)
                .date(requestDto.getDate())
                .hourTime(requestDto.getHourTime())
                .minTime(requestDto.getMinTime())
                .hospitalAlarmDetail(requestDto.getHospitalAlarmDetail())
                .addMemo(requestDto.getAddMemo())
                .build();

        myHospitalRepository.save(hospitalAlarm);

        return "병원 알람이 저장되었습니다.";
    }

    // 유저의 병원 예약 정보 조회
    public HospitalAlarmListResponseDTO getHospitalAlarmsByUserId(int userId) {
        // 유저의 병원 예약 정보 조회
        List<MyHospital> myHospitals = myHospitalRepository.findByUser_UserId(userId);

        // 예약 정보가 없으면 예외 처리
        if (myHospitals.isEmpty()) {
            throw new IllegalArgumentException("해당 유저의 예약 정보가 존재하지 않습니다.");
        }

        // 병원 예약 정보를 HospitalAlarmResponseDto 형식으로 변환
        List<HospitalAlarmResponseDTO> alarms = myHospitals.stream()
                .map(myHospital -> {
                    Hospital hospital = myHospital.getHospital();
                    return new HospitalAlarmResponseDTO(
                            myHospital.getHospitalAlarmId(),
                            myHospital.getDate(),
                            myHospital.getHourTime(),
                            myHospital.getMinTime(),
                            hospital.getHName(),
                            myHospital.getHospitalAlarmDetail(),
                            myHospital.getAddMemo()
                    );
                })
                .collect(Collectors.toList());

        // 병원 예약 정보 리스트를 감싸서 반환
        return new HospitalAlarmListResponseDTO(true, "전체 병원 예약 정보를 성공적으로 조회했습니다.", new HospitalAlarmListResponseDTO.Data(alarms));
    }

    // 유저 ID와 날짜로 병원 예약 정보 조회
    public HospitalAlarmDayResponseDTO getHospitalAlarmsByUserIdAndDate(int userId, String date) {
        // 해당 userId와 date로 MyHospital 리스트 조회
        List<MyHospital> myHospitals = myHospitalRepository.findByUser_UserIdAndDate(userId, date);

        // 조회된 데이터가 없으면 예외 처리
        if (myHospitals.isEmpty()) {
            throw new IllegalArgumentException("해당 유저의 당일 예약 정보가 존재하지 않습니다.");
        }

        // 데이터 리스트 변환
        List<HospitalAlarmResponseDTO> alarms = myHospitals.stream()
                .map(myHospital -> new HospitalAlarmResponseDTO(
                        myHospital.getHospitalAlarmId(),
                        myHospital.getDate(),
                        myHospital.getHourTime(),
                        myHospital.getMinTime(),
                        myHospital.getHospital().getHName(),
                        myHospital.getHospitalAlarmDetail(),
                        myHospital.getAddMemo()
                ))
                .collect(Collectors.toList());

        // 당일 예약 개수
        int dailyHospitalAlarmCount = alarms.size();

        // DTO로 반환
        return new HospitalAlarmDayResponseDTO(true, "당일 병원 예약 정보를 성공적으로 조회했습니다.",
                new HospitalAlarmDayResponseDTO.Data(dailyHospitalAlarmCount, alarms));
    }

    // 병원 예약 정보 업데이트
    public String updateHospitalAlarm(int userId, int hospitalAlarmId, MyHospitalRequestDTO requestDto) {
        // 해당 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 해당 병원 예약 정보 조회
        MyHospital hospitalAlarm = myHospitalRepository.findById(hospitalAlarmId)
                .orElseThrow(() -> new IllegalArgumentException("해당 병원 예약 정보가 존재하지 않습니다."));

        System.out.println("조회할 hospitalAlarmId: " + hospitalAlarm);

        // 유저와 병원 정보가 맞는지 확인
        if (hospitalAlarm.getUser().getUserId() != userId) {
            throw new IllegalArgumentException("해당 유저의 병원 예약 정보가 아닙니다.");
        }

        int hospitalId = hospitalAlarm.getHospital().getHospitalId();
        System.out.println("RequestDto 병원 ID: " + hospitalId);

        // 병원 정보 조회
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 병원이 존재하지 않습니다."));

        // 정보 업데이트
        hospitalAlarm.setDate(requestDto.getDate());
        hospitalAlarm.setHourTime(requestDto.getHourTime());
        hospitalAlarm.setMinTime(requestDto.getMinTime());
        hospitalAlarm.setHospitalAlarmDetail(requestDto.getHospitalAlarmDetail());
        hospitalAlarm.setAddMemo(requestDto.getAddMemo());
        hospitalAlarm.setHospital(hospital);

        // 저장
        myHospitalRepository.save(hospitalAlarm);

        return "병원 예약 정보가 업데이트되었습니다.";
    }

    //병원 예약 알람 삭제
    public ResponseEntity<Map<String, Object>> deleteHospitalAlarm(int userId, int hospitalAlarmId) {
        //유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        //병원 예약 정보 조회
        MyHospital hospitalAlarm = myHospitalRepository.findById(hospitalAlarmId)
                .orElseThrow(() -> new IllegalArgumentException("해당 병원 예약 정보가 존재하지 않습니다."));

        // 유저와 병원 정보가 맞는지 확인
        if (hospitalAlarm.getUser().getUserId() != userId) {
            throw new IllegalArgumentException("해당 유저의 예약 정보가 아닙니다.");
        }

        myHospitalRepository.delete(hospitalAlarm);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "병원 예약 정보를 성공적으로 삭제했습니다.");

        Map<String, Integer> data = new HashMap<>();
        data.put("u_id", userId);
        data.put("hospitalAlarm_id", hospitalAlarmId);

        response.put("data", data);

        return ResponseEntity.ok(response);
    }
}
