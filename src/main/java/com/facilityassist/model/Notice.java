package com.facilityassist.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity representing general notices
 */
@Entity
@Table(name = "NOTICES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"writtenBy"})
public class Notice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "content", columnDefinition = "CLOB")
    private String content;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "written_by", nullable = false)
    private User writtenBy;
    
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
