package com.facilityassist.dto;

import com.facilityassist.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user information response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    
    private Long id;
    private String username;
    private String name;
    private UserRole role;
    private UnitInfo unit;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UnitInfo {
        private Long id;
        private String name;
        private String code;
    }
}
