package com.facilityassist.repository;

import com.facilityassist.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DocumentRepositoryCustom {
    List<Document> findAllActiveOrderByUploadedAtDesc();
    Page<Document> findAllActiveOrderByUploadedAtDesc(Pageable pageable);
    Optional<Document> findByIdAndIsActiveTrue(Long id);
    List<Document> findByUploaderIdAndIsActiveTrue(Long uploaderId);
    long countActiveDocuments();
}
