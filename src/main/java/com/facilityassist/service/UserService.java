package com.facilityassist.service;

import com.facilityassist.dto.UserInfoResponse;
import com.facilityassist.model.User;
import com.facilityassist.repository.UserRepository;
import com.facilityassist.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for user-related operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * Get current logged-in user information
     * @return UserInfoResponse containing user details
     */
    public Optional<UserInfoResponse> getCurrentUserInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("No authenticated user found");
                return Optional.empty();
            }
            
            Object principal = authentication.getPrincipal();
            
            if (principal instanceof UserPrincipal userPrincipal) {
                String username = userPrincipal.getUsername();
                log.debug("Getting user info for username: {}", username);
                
                return userRepository.findByUsername(username)
                    .map(this::convertToUserInfoResponse);
            } else {
                log.warn("Principal is not of type UserPrincipal: {}", principal.getClass().getName());
                return Optional.empty();
            }
            
        } catch (Exception e) {
            log.error("Error getting current user info", e);
            return Optional.empty();
        }
    }
    
    /**
     * Get user information by username
     * @param username the username to search for
     * @return UserInfoResponse containing user details
     */
    public Optional<UserInfoResponse> getUserInfoByUsername(String username) {
        try {
            log.debug("Getting user info for username: {}", username);
            return userRepository.findByUsername(username)
                .map(this::convertToUserInfoResponse);
        } catch (Exception e) {
            log.error("Error getting user info for username: {}", username, e);
            return Optional.empty();
        }
    }
    
    /**
     * Convert User entity to UserInfoResponse DTO
     * @param user the User entity
     * @return UserInfoResponse DTO
     */
    private UserInfoResponse convertToUserInfoResponse(User user) {
        UserInfoResponse.UnitInfo unitInfo = null;
        
        if (user.getUnit() != null) {
            unitInfo = UserInfoResponse.UnitInfo.builder()
                .id(user.getUnit().getId())
                .name(user.getUnit().getName())
                .code(user.getUnit().getCode())
                .build();
        }
        
        return UserInfoResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .name(user.getName())
            .role(user.getRole())
            .unit(unitInfo)
            .build();
    }
}
