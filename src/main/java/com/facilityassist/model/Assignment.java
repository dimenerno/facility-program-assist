package com.facilityassist.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Join table entity to resolve the N:M relationship between FACILITY_TASK and UNIT
 */
@Entity
@Table(name = "ASSIGNMENT")
@IdClass(AssignmentId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"facilityTask", "unit"})
public class Assignment {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_task_id", nullable = false)
    private FacilityTask facilityTask;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;
    
    @Column(name = "assigned_at")
    @Builder.Default
    private LocalDateTime assignedAt = LocalDateTime.now();
}
