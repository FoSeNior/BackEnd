package com.inje.forseni.Controller;

import com.inje.forseni.Dto.UserDTO;
import com.inje.forseni.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signUp")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody UserDTO userDTO) {
        return userService.signUp(userDTO);
    }

    // 아이디 중복 확인
    @PostMapping("/signUp/idAvailable")
    public ResponseEntity<Map<String, Object>> checkMembershipIdAvailability(@RequestBody Map<String, String> request) {
        String membershipId = request.get("membershipId");
        return userService.checkMembershipIdAvailability(membershipId);
    }


    // 로그인
    @PostMapping("/signIn")
    public ResponseEntity<Map<String, Object>> signIn(@RequestBody UserDTO userDTO) {
        return userService.signIn(userDTO);
    }

    // 사용자 정보 조회
    @GetMapping("/{u_id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable("u_id") Integer userId) {
        return userService.getUserById(userId);
    }
}
