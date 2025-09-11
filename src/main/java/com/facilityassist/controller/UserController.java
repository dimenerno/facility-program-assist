package com.facilityassist.controller;

import com.facilityassist.dto.ApiResponse;
import com.facilityassist.dto.UserInfoResponse;
import com.facilityassist.model.User;
import com.facilityassist.repository.UserRepository;
import com.facilityassist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/managers")
    public List<User> getManagers() {
        return userRepository.findByRole(com.facilityassist.model.UserRole.MANAGER);
    }

    /**
     * Get current logged-in user information
     * @return ResponseEntity containing user information
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUserInfo() {
        try {
            log.info("Getting current user information");
            
            Optional<UserInfoResponse> userInfo = userService.getCurrentUserInfo();
            
            if (userInfo.isPresent()) {
                return ResponseEntity.ok(
                    ApiResponse.<UserInfoResponse>builder()
                        .success(true)
                        .message("사용자 정보를 성공적으로 조회했습니다.")
                        .data(userInfo.get())
                        .build()
                );
            } else {
                log.warn("No user information found for current user");
                return ResponseEntity.badRequest().body(
                    ApiResponse.<UserInfoResponse>builder()
                        .success(false)
                        .message("사용자 정보를 찾을 수 없습니다.")
                        .build()
                );
            }
            
        } catch (Exception e) {
            log.error("Error getting current user information", e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.<UserInfoResponse>builder()
                    .success(false)
                    .message("사용자 정보 조회 중 오류가 발생했습니다.")
                    .build()
            );
        }
    }
}
