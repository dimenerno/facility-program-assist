package com.facilityassist.controller;

import com.facilityassist.dto.ApiResponse;
import com.facilityassist.dto.DocumentListResponse;
import com.facilityassist.dto.DocumentResponse;
import com.facilityassist.dto.DocumentResponse.DocumentSummary;
import com.facilityassist.dto.UploadDocumentRequest;
import com.facilityassist.service.DocumentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Controller for document-related API endpoints
 */
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {
    
    private final DocumentService documentService;
    
    /**
     * Get recent documents with pagination
     * @param page page number (0-based, defaults to 0)
     * @param size page size (defaults to 5)
     * @return ResponseEntity containing paginated documents
     */
    @GetMapping
    public ResponseEntity<ApiResponse<DocumentListResponse>> getRecentDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            log.info("Getting recent documents - page: {}, size: {}", page, size);
            
            DocumentListResponse documents = documentService.getRecentDocuments(page, size);
            
            return ResponseEntity.ok(
                ApiResponse.<DocumentListResponse>builder()
                    .success(true)
                    .message("문서를 성공적으로 조회했습니다.")
                    .data(documents)
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error getting recent documents", e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.<DocumentListResponse>builder()
                    .success(false)
                    .message("문서 조회 중 오류가 발생했습니다.")
                    .build()
            );
        }
    }
    
    /**
     * Get document details by ID
     * @param id document ID
     * @return ResponseEntity containing document details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocumentById(@PathVariable Long id) {
        try {
            log.info("Getting document by ID: {}", id);
            
            Optional<DocumentResponse> document = documentService.getDocumentById(id);
            
            if (document.isPresent()) {
                return ResponseEntity.ok(
                    ApiResponse.<DocumentResponse>builder()
                        .success(true)
                        .message("문서를 성공적으로 조회했습니다.")
                        .data(document.get())
                        .build()
                );
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            log.error("Error getting document by ID: {}", id, e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.<DocumentResponse>builder()
                    .success(false)
                    .message("문서 조회 중 오류가 발생했습니다.")
                    .build()
            );
        }
    }
    
    /**
     * Download document by ID
     * @param id document ID
     * @return ResponseEntity with file content for download
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        try {
            log.info("Downloading document by ID: {}", id);
            
            Optional<DocumentResponse.DocumentDownload> document = documentService.getDocumentForDownload(id);
            
            if (document.isPresent()) {
                DocumentResponse.DocumentDownload doc = document.get();
                
                HttpHeaders headers = new HttpHeaders();
                
                // Set proper content type based on file type
                MediaType contentType = MediaType.parseMediaType(doc.getFileType());
                headers.setContentType(contentType);
                
                headers.setContentDispositionFormData("attachment", doc.getFileName());
                headers.setContentLength(doc.getFileSize());
                
                return new ResponseEntity<>(doc.getFileContent(), headers, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            log.error("Error downloading document by ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get all documents (for admin purposes)
     * @return ResponseEntity containing all documents
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<DocumentListResponse>> getAllDocuments() {
        try {
            log.info("Getting all documents");
            
            // Get all documents and convert to list response format
            List<DocumentSummary> allDocuments = documentService.getAllDocuments();
            DocumentListResponse response = DocumentListResponse.builder()
                .documents(allDocuments)
                .totalCount(allDocuments.size())
                .currentPage(1)
                .totalPages(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();
            
            return ResponseEntity.ok(
                ApiResponse.<DocumentListResponse>builder()
                    .success(true)
                    .message("전체 문서를 성공적으로 조회했습니다.")
                    .data(response)
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error getting all documents", e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.<DocumentListResponse>builder()
                    .success(false)
                    .message("문서 조회 중 오류가 발생했습니다.")
                    .build()
            );
        }
    }
    
    /**
     * Upload a new document
     * @param title document title
     * @param description document description
     * @param file uploaded file
     * @return ResponseEntity containing the uploaded document
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<DocumentResponse>> uploadDocument(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("file") MultipartFile file) {
        try {
            log.info("Uploading new document with title: {}", title);
            
            // Create request object
            UploadDocumentRequest request = UploadDocumentRequest.builder()
                .title(title)
                .description(description)
                .file(file)
                .build();
            
            DocumentResponse uploadedDocument = documentService.uploadDocument(request);
            
            return ResponseEntity.ok(
                ApiResponse.<DocumentResponse>builder()
                    .success(true)
                    .message("문서가 성공적으로 업로드되었습니다.")
                    .data(uploadedDocument)
                    .build()
            );
            
        } catch (Exception e) {
            log.error("Error uploading document", e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.<DocumentResponse>builder()
                    .success(false)
                    .message("문서 업로드 중 오류가 발생했습니다: " + e.getMessage())
                    .build()
            );
        }
    }
}

