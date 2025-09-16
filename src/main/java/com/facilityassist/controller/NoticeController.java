package com.facilityassist.controller;

import com.facilityassist.dto.ApiResponse;
import com.facilityassist.dto.CreateNoticeRequest;
import com.facilityassist.dto.NoticeListResponse;
import com.facilityassist.dto.NoticeResponse;
import com.facilityassist.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

/**
 * Controller for notice-related API endpoints
 */
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@Slf4j
public class NoticeController {
    
    private final NoticeService noticeService;
    
    /**
     * Get recent notices with pagination
     * @param page page number (0-based, defaults to 0)
     * @param size page size (defaults to 5)
     * @return ResponseEntity containing paginated notices
     */
    @GetMapping
    public ResponseEntity<ApiResponse<NoticeListResponse>> getRecentNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            log.info("Getting recent notices - page: {}, size: {}", page, size);
            
            NoticeListResponse notices = noticeService.getRecentNotices(page, size);
            
            return ResponseEntity.ok(
                ApiResponse.<NoticeListResponse>builder()
                    .success(true)
                    .message("공지사항을 성공적으로 조회했습니다.")
                    .data(notices)
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error getting recent notices", e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.<NoticeListResponse>builder()
                    .success(false)
                    .message("공지사항 조회 중 오류가 발생했습니다.")
                    .build()
            );
        }
    }
    
    /**
     * Get notice details by ID
     * @param id notice ID
     * @return ResponseEntity containing notice details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NoticeResponse>> getNoticeById(@PathVariable Long id) {
        try {
            log.info("Getting notice by ID: {}", id);
            
            Optional<NoticeResponse> notice = noticeService.getNoticeById(id);
            
            if (notice.isPresent()) {
                return ResponseEntity.ok(
                    ApiResponse.<NoticeResponse>builder()
                        .success(true)
                        .message("공지사항을 성공적으로 조회했습니다.")
                        .data(notice.get())
                        .build()
                );
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            log.error("Error getting notice by ID: {}", id, e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.<NoticeResponse>builder()
                    .success(false)
                    .message("공지사항 조회 중 오류가 발생했습니다.")
                    .build()
            );
        }
    }
    
    /**
     * Get all notices (for admin purposes)
     * @return ResponseEntity containing all notices
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<NoticeListResponse>> getAllNotices() {
        try {
            log.info("Getting all notices");
            
            // Get all notices and convert to list response format
            var allNotices = noticeService.getAllNotices();
            NoticeListResponse response = NoticeListResponse.builder()
                .notices(allNotices)
                .totalCount(allNotices.size())
                .currentPage(1)
                .totalPages(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();
            
            return ResponseEntity.ok(
                ApiResponse.<NoticeListResponse>builder()
                    .success(true)
                    .message("전체 공지사항을 성공적으로 조회했습니다.")
                    .data(response)
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error getting all notices", e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.<NoticeListResponse>builder()
                    .success(false)
                    .message("공지사항 조회 중 오류가 발생했습니다.")
                    .build()
            );
        }
    }
    
    /**
     * Create a new notice
     * @param request CreateNoticeRequest containing title and content
     * @return ResponseEntity containing the created notice
     */
    @PostMapping
    public ResponseEntity<ApiResponse<NoticeResponse>> createNotice(@Valid @RequestBody CreateNoticeRequest request) {
        try {
            log.info("Creating new notice with title: {}", request.getTitle());
            
            NoticeResponse createdNotice = noticeService.createNotice(request);
            
            return ResponseEntity.ok(
                ApiResponse.<NoticeResponse>builder()
                    .success(true)
                    .message("공지사항이 성공적으로 작성되었습니다.")
                    .data(createdNotice)
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error creating notice", e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.<NoticeResponse>builder()
                    .success(false)
                    .message("공지사항 작성 중 오류가 발생했습니다: " + e.getMessage())
                    .build()
            );
        }
    }
}

