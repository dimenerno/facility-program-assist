package com.facilityassist.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity representing uploaded documents
 */
@Entity
@Table(name = "DOCUMENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"uploadedBy"})
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "description", columnDefinition = "CLOB")
    private String description;
    
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "file_type", nullable = false)
    private String fileType;
    
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    @Lob
    @Column(name = "file_content", nullable = false)
    private byte[] fileContent;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;
    
    @Column(name = "uploaded_at")
    @Builder.Default
    private LocalDateTime uploadedAt = LocalDateTime.now();
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}
