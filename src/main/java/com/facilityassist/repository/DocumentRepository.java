package com.facilityassist.repository;

import com.facilityassist.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    /**
     * Find all active documents ordered by upload date descending
     */
    @Query("SELECT d FROM Document d WHERE d.isActive = true ORDER BY d.uploadedAt DESC")
    List<Document> findAllActiveOrderByUploadedAtDesc();
    
    /**
     * Find all active documents with pagination ordered by upload date descending
     */
    @Query("SELECT d FROM Document d WHERE d.isActive = true ORDER BY d.uploadedAt DESC")
    Page<Document> findAllActiveOrderByUploadedAtDesc(Pageable pageable);
    
    /**
     * Find active document by ID
     */
    @Query("SELECT d FROM Document d WHERE d.id = :id AND d.isActive = true")
    Optional<Document> findByIdAndIsActiveTrue(Long id);
    
    /**
     * Find documents by uploader
     */
    @Query("SELECT d FROM Document d WHERE d.uploadedBy.id = :uploaderId AND d.isActive = true ORDER BY d.uploadedAt DESC")
    List<Document> findByUploaderIdAndIsActiveTrue(Long uploaderId);
    
    /**
     * Count active documents
     */
    @Query("SELECT COUNT(d) FROM Document d WHERE d.isActive = true")
    long countActiveDocuments();
}
