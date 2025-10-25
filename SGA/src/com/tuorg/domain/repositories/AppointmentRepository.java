package com.tuorg.domain.repositories;

import com.tuorg.domain.entities.Appointment;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {
    List<Appointment> findByOptionalFilters(UUID patientId, UUID doctorId);
    Optional<Appointment> findById(UUID id);
    Appointment save(Appointment appointment);
    void deleteById(UUID id);
    boolean existsOverlap(UUID doctorId, OffsetDateTime start, OffsetDateTime end);
}
