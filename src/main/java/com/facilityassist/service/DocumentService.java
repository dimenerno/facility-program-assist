package com.facilityassist.service;

import com.facilityassist.dto.DocumentListResponse;
import com.facilityassist.dto.DocumentResponse;
import com.facilityassist.model.Document;
import com.facilityassist.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for document-related operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {
    
    private final DocumentRepository documentRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Get recent documents with pagination
     * @param page page number (0-based)
     * @param size page size
     * @return DocumentListResponse with paginated documents
     */
    public DocumentListResponse getRecentDocuments(int page, int size) {
        try {
            log.debug("Getting recent documents - page: {}, size: {}", page, size);
            
            Pageable pageable = PageRequest.of(page, size);
            Page<Document> documentPage = documentRepository.findAllActiveOrderByUploadedAtDesc(pageable);
            
            List<DocumentResponse.DocumentSummary> documentSummaries = documentPage.getContent()
                .stream()
                .map(this::convertToDocumentSummary)
                .collect(Collectors.toList());
            
            return DocumentListResponse.builder()
                .documents(documentSummaries)
                .totalCount((int) documentPage.getTotalElements())
                .currentPage(page + 1) // Convert to 1-based page number
                .totalPages(documentPage.getTotalPages())
                .hasNext(documentPage.hasNext())
                .hasPrevious(documentPage.hasPrevious())
                .build();
                
        } catch (Exception e) {
            log.error("Error getting recent documents", e);
            throw new RuntimeException("문서를 가져오는 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * Get document details by ID
     * @param id document ID
     * @return DocumentResponse with full document details
     */
    public Optional<DocumentResponse> getDocumentById(Long id) {
        try {
            log.debug("Getting document by ID: {}", id);
            
            return documentRepository.findByIdAndIsActiveTrue(id)
                .map(this::convertToDocumentResponse);
                
        } catch (Exception e) {
            log.error("Error getting document by ID: {}", id, e);
            throw new RuntimeException("문서를 가져오는 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * Get document for download by ID
     * @param id document ID
     * @return DocumentDownload with file content
     */
    public Optional<DocumentResponse.DocumentDownload> getDocumentForDownload(Long id) {
        try {
            log.debug("Getting document for download by ID: {}", id);
            
            return documentRepository.findByIdAndIsActiveTrue(id)
                .map(this::convertToDocumentDownload);
                
        } catch (Exception e) {
            log.error("Error getting document for download by ID: {}", id, e);
            throw new RuntimeException("문서 다운로드 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * Get all documents (for admin purposes)
     * @return List of all active documents
     */
    public List<DocumentResponse.DocumentSummary> getAllDocuments() {
        try {
            log.debug("Getting all documents");
            
            return documentRepository.findAllActiveOrderByUploadedAtDesc()
                .stream()
                .map(this::convertToDocumentSummary)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("Error getting all documents", e);
            throw new RuntimeException("문서를 가져오는 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * Convert Document entity to DocumentResponse
     * @param document Document entity
     * @return DocumentResponse
     */
    private DocumentResponse convertToDocumentResponse(Document document) {
        return DocumentResponse.builder()
            .id(document.getId())
            .title(document.getTitle())
            .description(document.getDescription())
            .fileName(document.getFileName())
            .fileType(document.getFileType())
            .fileSize(document.getFileSize())
            .uploaderName(document.getUploadedBy() != null ? document.getUploadedBy().getName() : "알 수 없음")
            .uploaderUsername(document.getUploadedBy() != null ? document.getUploadedBy().getUsername() : "unknown")
            .uploadedAt(document.getUploadedAt())
            .formattedDate(document.getUploadedAt().format(dateFormatter))
            .formattedFileSize(formatFileSize(document.getFileSize()))
            .build();
    }
    
    /**
     * Convert Document entity to DocumentSummary
     * @param document Document entity
     * @return DocumentSummary
     */
    private DocumentResponse.DocumentSummary convertToDocumentSummary(Document document) {
        return DocumentResponse.DocumentSummary.builder()
            .id(document.getId())
            .title(document.getTitle())
            .description(document.getDescription())
            .fileName(document.getFileName())
            .fileType(document.getFileType())
            .fileSize(document.getFileSize())
            .uploaderName(document.getUploadedBy() != null ? document.getUploadedBy().getName() : "알 수 없음")
            .uploadedAt(document.getUploadedAt())
            .formattedDate(document.getUploadedAt().format(dateFormatter))
            .formattedFileSize(formatFileSize(document.getFileSize()))
            .build();
    }
    
    /**
     * Convert Document entity to DocumentDownload
     * @param document Document entity
     * @return DocumentDownload
     */
    private DocumentResponse.DocumentDownload convertToDocumentDownload(Document document) {
        return DocumentResponse.DocumentDownload.builder()
            .id(document.getId())
            .title(document.getTitle())
            .fileName(document.getFileName())
            .fileType(document.getFileType())
            .fileContent(document.getFileContent())
            .fileSize(document.getFileSize())
            .build();
    }
    
    /**
     * Format file size in human readable format
     * @param bytes file size in bytes
     * @return formatted file size string
     */
    private String formatFileSize(Long bytes) {
        if (bytes == null || bytes == 0) {
            return "0 B";
        }
        
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = bytes.doubleValue();
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        return String.format("%.1f %s", size, units[unitIndex]);
    }
}

