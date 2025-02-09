package com.inje.forseni.Service;

import com.inje.forseni.Dto.UserDTO;
import com.inje.forseni.Entity.User;
import com.inje.forseni.Repository.UserRepository;
import com.inje.forseni.Util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        if (userRepository.existsByMembershipId(userDTO.getMembershipId())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "이미 사용 중인 아이디입니다."
            ));
        }

        //  새 사용자 생성
        User newUser = new User();
        newUser.setMembershipId(userDTO.getMembershipId());
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

        userRepository.save(newUser);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "회원가입 성공",
                "data", Map.of("u_id", newUser.getUserId())
        ));
    }


    // membershipId 중복 확인
    public boolean isUserIdAvailable(String membershipId) {
        return !userRepository.existsByMembershipId(membershipId);
    }


    //  로그인 로직
    public ResponseEntity<Map<String, Object>> signIn(UserDTO userDTO) {
        User user = userRepository.findByMembershipId(userDTO.getMembershipId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!user.getPassword().equals(userDTO.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }


        // JWT 토큰 생성
        Long userIdLong = Optional.ofNullable(user.getUserId()).map(Integer::longValue).orElse(0L);
        String accessToken = jwtTokenProvider.createAccessToken(userIdLong, user.getUserName());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        Map<String, Object> userInfo = Map.of(
                "u_id", user.getUserId(),
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
    public ResponseEntity<Map<String, Object>> getUserById(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "사용자를 찾을 수 없습니다."
            ));
        }

        Map<String, Object> data = Map.of(
                "u_id", user.get().getUserId(),
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
