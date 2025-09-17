package com.facilityassist.model;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing tasks related to facilities
 */
@Entity
@Table(name = "FACILITY_TASK")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"assignments"})
public class FacilityTask {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @Column(name = "info", columnDefinition = "CLOB")
    private String info;
    
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // One-to-many relationship with Assignment (join table)
    @OneToMany(mappedBy = "facilityTask", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Assignment> assignments = new ArrayList<>();
}
