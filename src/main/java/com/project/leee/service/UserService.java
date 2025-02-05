package com.project.leee.service;

import com.project.leee.dto.UserDTO;
import com.project.leee.entity.User;
import com.project.leee.repository.UserRepository;
import com.project.leee.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //  회원가입
    public ResponseEntity<Map<String, Object>> signUp(UserDTO userDTO) {
        //  userId가 중복 확인
        if (userRepository.existsByUserId(userDTO.getUserId())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "이미 사용 중인 아이디입니다."
            ));
        }

        //  새 사용자 생성
        User newUser = new User();
        newUser.setUserId(userDTO.getUserId());
        newUser.setUserName(userDTO.getUserName());
        newUser.setAge(userDTO.getAge());

        //  password가 null이면 기본값 설정
        if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "비밀번호는 필수 입력 항목입니다."
            ));
        }
        newUser.setPassword(userDTO.getPassword());

        //  fontSize가 null일 때 기본값 설정
        newUser.setFontSize(userDTO.getFontSize() != null ? userDTO.getFontSize() : 16);


        userRepository.save(newUser);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "회원가입 성공",
                "data", Map.of("u_id", newUser.getUId())
        ));
    }


    // userId 중복 확인
    public boolean isUserIdAvailable(String userId) {
        return !userRepository.existsByUserId(userId);
    }


    //  로그인 로직
    public ResponseEntity<Map<String, Object>> signIn(UserDTO userDTO) {
        User user = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!user.getPassword().equals(userDTO.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getUId().longValue(), user.getUserName());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        Map<String, Object> userInfo = Map.of(
                "u_id", user.getUId(),
                "userName", user.getUserName()
        );


        Map<String, Object> response = Map.of(
                "success", true,
                "message", "로그인 성공",
                "data", Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken,
                        "userInfo", userInfo
                )
        );



        System.out.println("Final Response Data: " + response);

        return ResponseEntity.ok(response);
    }


    // 사용자 정보 조회
    public ResponseEntity<Map<String, Object>> getUserById(Integer u_id) {
        Optional<User> user = userRepository.findById(u_id);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "사용자를 찾을 수 없습니다."
            ));
        }

        Map<String, Object> data = Map.of(
                "u_id", user.get().getUId(),
                "userName", user.get().getUserName()
        );

        Map<String, Object> response = Map.of(
                "success", true,
                "message", "사용자 정보를 불러왔습니다",
                "data", data
        );

        return ResponseEntity.ok(response);
    }
}
