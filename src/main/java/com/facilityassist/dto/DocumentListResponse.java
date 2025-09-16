package com.facilityassist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for document list response with pagination
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentListResponse {
    
    private List<DocumentResponse.DocumentSummary> documents;
    private int totalCount;
    private int currentPage;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}

