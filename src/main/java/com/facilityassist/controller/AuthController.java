package com.facilityassist.controller;

import com.facilityassist.dto.ApiResponse;
import com.facilityassist.dto.LoginRequest;
import com.facilityassist.dto.LoginResponse;
import com.facilityassist.security.UserPrincipal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            log.info("Login attempt for username: {}", loginRequest.getUsername());
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            LoginResponse loginResponse = LoginResponse.builder()
                .id(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .name(userPrincipal.getName())
                .role(userPrincipal.getRole())
                .unitId(userPrincipal.getUnitId())
                .unitName(userPrincipal.getUnitName())
                .token("authenticated") // Simple token for now
                .message("로그인 성공")
                .build();

            log.info("Login successful for user: {} ({})", userPrincipal.getName(), userPrincipal.getUsername());
            return ResponseEntity.ok(ApiResponse.success("로그인 성공", loginResponse));

        } catch (Exception e) {
            log.warn("Login failed for username: {} - {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("아이디 또는 비밀번호가 올바르지 않습니다."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                log.info("User logged out: {}", authentication.getName());
            }
            return ResponseEntity.ok(ApiResponse.success("로그아웃 성공", null));
        } catch (Exception e) {
            log.error("Logout error: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("로그아웃 중 오류가 발생했습니다."));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<LoginResponse>> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("인증되지 않은 사용자입니다."));
            }

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            LoginResponse userResponse = LoginResponse.builder()
                .id(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .name(userPrincipal.getName())
                .role(userPrincipal.getRole())
                .unitId(userPrincipal.getUnitId())
                .unitName(userPrincipal.getUnitName())
                .build();

            return ResponseEntity.ok(ApiResponse.success(userResponse));
        } catch (Exception e) {
            log.error("Get current user error: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("사용자 정보를 가져올 수 없습니다."));
        }
    }
}
