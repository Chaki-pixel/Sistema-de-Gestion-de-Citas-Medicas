package com.tuorg.businesslogic.dto.appointment;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CreateAppointmentDto {
    public UUID patientId;
    public UUID doctorId;
    public OffsetDateTime desiredStart;
    public String reason;
    public String source;
}
