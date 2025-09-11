package com.facilityassist.model;

import lombok.*;
import java.io.Serializable;

/**
 * Composite key class for Assignment entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AssignmentId implements Serializable {
    
    private Long facilityTask;
    private Long unit;
}
