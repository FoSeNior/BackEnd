package com.inje.forseni.Service;

import com.inje.forseni.Controller.LikelyHospitalController;
import com.inje.forseni.Dto.HospitalAlarmDayResponseDTO;
import com.inje.forseni.Dto.HospitalAlarmResponseDTO;
import com.inje.forseni.Dto.HospitalDTO;
import com.inje.forseni.Entity.Hospital;
import com.inje.forseni.Entity.MyHospital;
import com.inje.forseni.Entity.User;
import com.inje.forseni.Repository.HospitalRepository;
import com.inje.forseni.Repository.MyHospitalRepository;
import com.inje.forseni.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikelyHospitalService {
    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;
    private final MyHospitalRepository myHospitalRepository;

    //병원 찜하기
    public String savedHospital(int userId, HospitalDTO hospitalDTO){
        User user =userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        boolean exists = hospitalRepository.existsByHospital(
                hospitalDTO.getHName(),hospitalDTO.getHAddress(),hospitalDTO.getHNumber());

        if(exists) {
            Hospital hospital = hospitalRepository.findByHospitalDetails(
                            hospitalDTO.getHName(), hospitalDTO.getHAddress(), hospitalDTO.getHNumber())
                    .orElseThrow(() -> new IllegalArgumentException("병원을 찾을 수 없습니다."));

            boolean isAlreadySaved = myHospitalRepository.existsByUserAndHospital(user, hospital);
            if (isAlreadySaved) {
                return "이미 찜한 병원입니다.";
            } else {
                // 찜하지 않은 병원일 경우에만 MyHospital에 추가
                MyHospital myHospital = MyHospital.builder()
                        .user(user)
                        .hospital(hospital)
                        .build();
                myHospitalRepository.save(myHospital);
                return "병원 찜하기 성공";
            }
        } else {
            // 병원이 존재하지 않는 경우 새 병원을 추가
            Hospital hospital = Hospital.builder()
                    .hAddress(hospitalDTO.getHAddress())
                    .hName(hospitalDTO.getHName())
                    .hNumber(hospitalDTO.getHNumber())
                    .build();

            hospitalRepository.save(hospital);

            // 병원을 찜하는 MyHospital 저장
            MyHospital myHospital = MyHospital.builder()
                    .user(user)
                    .hospital(hospital)
                    .build();
            myHospitalRepository.save(myHospital);

            return "병원 찜하기 성공";
        }
    }
    //사용자별 찜한 병원 리스트
    public List<HospitalDTO> getSavedHospitals(int userId){
        List<MyHospital> myHospitals = myHospitalRepository.findByUser_UserId(userId);

        List<HospitalDTO> saved = myHospitals.stream()
                .map(myHospital -> new HospitalDTO(
                        String.valueOf(myHospital.getHospital().getHospitalId()),
                        myHospital.getHospital().getHName(),
                        myHospital.getHospital().getHAddress(),
                        myHospital.getHospital().getHNumber()
                ))
                .collect(Collectors.toList());
        return saved;
    }

    // 병원 찜한 정보 삭제
    public String deleteSavedHospital(int userId, int hospitalId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalArgumentException("해당 병원이 존재하지 않습니다."));

        // 사용자가 찜한 병원 정보 조회
        List<MyHospital> myHospitals = myHospitalRepository.findByUserAndHospital(user, hospital);

        // 찜한 병원이 없다면 예외 처리
        if (myHospitals.isEmpty()) {
            throw new IllegalArgumentException("찜한 병원이 존재하지 않습니다.");
        }

        // 해당 병원의 알람 정보 삭제
        myHospitalRepository.deleteAll(myHospitals);

        return "병원 찜과 알람 정보가 삭제되었습니다.";
    }
}
