package com.inje.forseni.Service;

import com.inje.forseni.Dto.UserDTO;
import com.inje.forseni.Entity.User;
import com.inje.forseni.Repository.UserRepository;
import com.inje.forseni.Util.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // 아이디 중복 확인
    public ResponseEntity<Map<String, Object>> checkUserIdAvailability(String membershipId) {
        boolean available = !userRepository.existsByMembershipId(membershipId);
        return ResponseEntity.ok(Map.of(
                "success", available,
                "message", available ? "해당 아이디는 사용 가능합니다" : "이미 사용 중인 아이디입니다."
        ));
    }

    //  회원가입
    public ResponseEntity<Map<String, Object>> signUp(UserDTO userDTO) {
        try {
            // membershipId 중복 확인
            if (userRepository.existsByMembershipId(userDTO.getMembershipId())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "이미 사용 중인 아이디입니다."
                ));
            }

            // 필수 값 체크
            if (userDTO.getMembershipId() == null || userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "membershipId 또는 password가 누락되었습니다."
                ));
            }

            // 새 사용자 생성
            User newUser = new User();
            newUser.setMembershipId(userDTO.getMembershipId());
            newUser.setUserName(userDTO.getUserName());
            newUser.setAge(userDTO.getAge());
            newUser.setPassword(userDTO.getPassword());

            userRepository.save(newUser);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "회원가입 성공",
                    "data", Map.of("u_id", newUser.getUserId())
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "회원가입 중 오류 발생", "error", e.getMessage()));
        }
    }

    // 로그인 로직
    public ResponseEntity<Map<String, Object>> signIn(UserDTO userDTO) {
        try {
            if (userDTO.getMembershipId() == null || userDTO.getPassword() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "membershipId 또는 password가 누락되었습니다."
                ));
            }

            Optional<User> userOpt = userRepository.findByMembershipId(userDTO.getMembershipId());

            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "존재하지 않는 사용자입니다."
                ));
            }

            User user = userOpt.get();
            if (!user.getPassword().equals(userDTO.getPassword())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "비밀번호가 일치하지 않습니다."
                ));
            }

            // JWT 토큰 생성
            String accessToken = jwtTokenProvider.createAccessToken((long) user.getUserId(), user.getUserName());
            String refreshToken = jwtTokenProvider.createRefreshToken();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "로그인 성공",
                    "data", Map.of(
                            "accessToken", accessToken,
                            "refreshToken", refreshToken,
                            "userInfo", Map.of(
                                    "u_id", user.getUserId(),
                                    "userName", user.getUserName()
                            )
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "로그인 중 오류 발생", "error", e.getMessage()));
        }
    }
    // 사용자 정보 조회
    public ResponseEntity<Map<String, Object>> getUserById(Integer userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "사용자를 찾을 수 없습니다."
                ));
            }

            Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "사용자 정보를 불러왔습니다",
                    "data", Map.of(
                            "u_id", user.get().getUserId(),
                            "userName", user.get().getUserName()
                    )
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "사용자 정보 조회 중 오류 발생", "error", e.getMessage()));
        }
    }
}
