package com.project.leee.controller;

import com.project.leee.dto.UserDTO;
import com.project.leee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO) {
        try {
            return userService.signUp(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "회원가입 중 오류 발생", "error", e.getMessage()));
        }
    }

    // 아이디 중복 확인
    @PostMapping("/signUp/idAvailable")
    public ResponseEntity<Map<String, Object>> isUserIdAvailable(@RequestBody Map<String, String> request) {
        String userId = request.get("userId"); // JSON에서 userId 추출
        boolean available = userService.isUserIdAvailable(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", available);
        response.put("message", available ? "해당 아이디는 사용 가능합니다" : "이미 사용 중인 아이디입니다.");
        //  요청한 JSON 응답 형식 추가

        if (available) {
            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", "string");
            data.put("refreshToken", "string");

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", userId);

            data.put("userInfo", userInfo);
            response.put("data", data);
        }

        return ResponseEntity.ok(response);
    }

    // 로그인
    @PostMapping("/signIn")
    public ResponseEntity<Map<String, Object>> signIn(@RequestBody UserDTO userDTO) {
        if (userDTO.getUserId() == null || userDTO.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "userId 또는 password가 누락되었습니다."
            ));
        }

        try {
            return userService.signIn(userDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // 사용자 정보 조회
    @GetMapping("/{u_id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Integer u_id) {
        return userService.getUserById(u_id);
    }
}
