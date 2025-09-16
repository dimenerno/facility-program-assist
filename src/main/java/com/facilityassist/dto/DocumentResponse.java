package com.facilityassist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for document response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponse {
    
    private Long id;
    private String title;
    private String description;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String uploaderName;
    private String uploaderUsername;
    private LocalDateTime uploadedAt;
    private String formattedDate;
    private String formattedFileSize;
    
    /**
     * Simple document response for list views (without file content)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DocumentSummary {
        private Long id;
        private String title;
        private String description;
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String uploaderName;
        private LocalDateTime uploadedAt;
        private String formattedDate;
        private String formattedFileSize;
    }
    
    /**
     * Document download response with file content
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DocumentDownload {
        private Long id;
        private String title;
        private String fileName;
        private String fileType;
        private byte[] fileContent;
        private Long fileSize;
    }
}

