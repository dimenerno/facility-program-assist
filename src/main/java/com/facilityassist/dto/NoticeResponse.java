package com.facilityassist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for notice response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeResponse {
    
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private String authorUsername;
    private LocalDateTime createdAt;
    private String formattedDate;
    
    /**
     * Simple notice response for list views (without full content)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NoticeSummary {
        private Long id;
        private String title;
        private String authorName;
        private LocalDateTime createdAt;
        private String formattedDate;
    }
}

