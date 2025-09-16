package com.facilityassist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for uploading a new document
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadDocumentRequest {
    
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다.")
    private String title;
    
    @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다.")
    private String description;
    
    @NotNull(message = "파일은 필수입니다.")
    private MultipartFile file;
}
