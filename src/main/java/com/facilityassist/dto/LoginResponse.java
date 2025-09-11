package com.facilityassist.dto;

import com.facilityassist.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private Long id;
    private String username;
    private String name;
    private UserRole role;
    private Long unitId;
    private String unitName;
    private String token;
    private String message;
}
