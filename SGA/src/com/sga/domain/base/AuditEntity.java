package com.sga.domain.base;

import java.time.LocalDateTime;

public abstract class AuditEntity {
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    // Getters and setters
}
