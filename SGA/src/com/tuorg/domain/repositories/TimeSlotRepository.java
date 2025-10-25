package com.tuorg.domain.repositories;

import com.tuorg.domain.entities.TimeSlot;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TimeSlotRepository {
    Optional<TimeSlot> findByDoctorAndStart(UUID doctorId, OffsetDateTime start);
    TimeSlot save(TimeSlot ts);
}
