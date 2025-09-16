package com.facilityassist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for notice list response with pagination
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeListResponse {
    
    private List<NoticeResponse.NoticeSummary> notices;
    private int totalCount;
    private int currentPage;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}

