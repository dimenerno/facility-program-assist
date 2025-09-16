package com.facilityassist.service;

import com.facilityassist.dto.CreateNoticeRequest;
import com.facilityassist.dto.NoticeListResponse;
import com.facilityassist.dto.NoticeResponse;
import com.facilityassist.model.Notice;
import com.facilityassist.model.User;
import com.facilityassist.repository.NoticeRepository;
import com.facilityassist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for notice-related operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {
    
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Get recent notices with pagination
     * @param page page number (0-based)
     * @param size page size
     * @return NoticeListResponse with paginated notices
     */
    public NoticeListResponse getRecentNotices(int page, int size) {
        try {
            log.debug("Getting recent notices - page: {}, size: {}", page, size);
            
            Pageable pageable = PageRequest.of(page, size);
            Page<Notice> noticePage = noticeRepository.findAllOrderByCreatedAtDesc(pageable);
            
            List<NoticeResponse.NoticeSummary> noticeSummaries = noticePage.getContent()
                .stream()
                .map(this::convertToNoticeSummary)
                .collect(Collectors.toList());
            
            return NoticeListResponse.builder()
                .notices(noticeSummaries)
                .totalCount((int) noticePage.getTotalElements())
                .currentPage(page + 1) // Convert to 1-based page number
                .totalPages(noticePage.getTotalPages())
                .hasNext(noticePage.hasNext())
                .hasPrevious(noticePage.hasPrevious())
                .build();
                
        } catch (Exception e) {
            log.error("Error getting recent notices", e);
            throw new RuntimeException("공지사항을 가져오는 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * Get notice details by ID
     * @param id notice ID
     * @return NoticeResponse with full notice details
     */
    public Optional<NoticeResponse> getNoticeById(Long id) {
        try {
            log.debug("Getting notice by ID: {}", id);
            
            return noticeRepository.findById(id)
                .map(this::convertToNoticeResponse);
                
        } catch (Exception e) {
            log.error("Error getting notice by ID: {}", id, e);
            throw new RuntimeException("공지사항을 가져오는 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * Get all notices (for admin purposes)
     * @return List of all notices
     */
    public List<NoticeResponse.NoticeSummary> getAllNotices() {
        try {
            log.debug("Getting all notices");
            
            return noticeRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToNoticeSummary)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("Error getting all notices", e);
            throw new RuntimeException("공지사항을 가져오는 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * Create a new notice
     * @param request CreateNoticeRequest containing title and content
     * @return NoticeResponse of the created notice
     */
    @Transactional
    public NoticeResponse createNotice(CreateNoticeRequest request) {
        try {
            log.info("Creating new notice with title: {}", request.getTitle());
            
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new RuntimeException("인증되지 않은 사용자입니다.");
            }
            
            String username = authentication.getName();
            User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));
            
            // Create new notice
            Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writtenBy(currentUser)
                .build();
            
            // Save notice
            Notice savedNotice = noticeRepository.save(notice);
            log.info("Successfully created notice with ID: {}", savedNotice.getId());
            
            // Convert to response
            return convertToNoticeResponse(savedNotice);
            
        } catch (Exception e) {
            log.error("Error creating notice", e);
            throw new RuntimeException("공지사항 생성 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * Convert Notice entity to NoticeResponse
     * @param notice Notice entity
     * @return NoticeResponse
     */
    private NoticeResponse convertToNoticeResponse(Notice notice) {
        return NoticeResponse.builder()
            .id(notice.getId())
            .title(notice.getTitle())
            .content(notice.getContent())
            .authorName(notice.getWrittenBy() != null ? notice.getWrittenBy().getName() : "알 수 없음")
            .authorUsername(notice.getWrittenBy() != null ? notice.getWrittenBy().getUsername() : "unknown")
            .createdAt(notice.getCreatedAt())
            .formattedDate(notice.getCreatedAt().format(dateFormatter))
            .build();
    }
    
    /**
     * Convert Notice entity to NoticeSummary
     * @param notice Notice entity
     * @return NoticeSummary
     */
    private NoticeResponse.NoticeSummary convertToNoticeSummary(Notice notice) {
        return NoticeResponse.NoticeSummary.builder()
            .id(notice.getId())
            .title(notice.getTitle())
            .authorName(notice.getWrittenBy() != null ? notice.getWrittenBy().getName() : "알 수 없음")
            .createdAt(notice.getCreatedAt())
            .formattedDate(notice.getCreatedAt().format(dateFormatter))
            .build();
    }
}

