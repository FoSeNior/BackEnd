package com.inje.forseni.Controller;

import com.inje.forseni.Dto.HospitalDTO;
import com.inje.forseni.Service.LikelyHospitalService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite/hospital")
@RequiredArgsConstructor
public class LikelyHospitalController {
    private final LikelyHospitalService likelyHospitalService;
    @PostMapping("/{u_id}")
    public ResponseEntity<String> savedHospital(
            @PathVariable("u_id") int userId,
            @RequestBody HospitalDTO requestDto){
        String responseMessage = likelyHospitalService.savedHospital(userId,requestDto);

        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/{u_id}")
    public ResponseEntity<List<HospitalDTO>> getSavedHospitals(@PathVariable("u_id") int userId){
        List<HospitalDTO> hospitalList = likelyHospitalService.getSavedHospitals(userId);

        return ResponseEntity.ok(hospitalList);
    }
    @DeleteMapping("/delete/{u_id}/{hospital_id}")
    public ResponseEntity<String> deletedSavedHospital(@PathVariable("u_id") int userId, @PathVariable("hospital_id") int hospitalId){
        String result = likelyHospitalService.deleteSavedHospital(userId,hospitalId);

        return ResponseEntity.ok(result);
    }
}
