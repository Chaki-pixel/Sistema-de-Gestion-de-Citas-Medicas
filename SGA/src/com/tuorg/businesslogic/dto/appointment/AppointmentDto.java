package com.tuorg.businesslogic.dto.appointment;

import java.time.OffsetDateTime;
import java.util.UUID;

public class AppointmentDto {
    public UUID id;
    public UUID patientId;
    public UUID doctorId;
    public OffsetDateTime start;
    public OffsetDateTime end;
    public String status;
    public OffsetDateTime createdAt;
    public OffsetDateTime updatedAt;
    public String reason;
}
