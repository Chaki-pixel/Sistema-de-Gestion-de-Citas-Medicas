package com.tuorg.businesslogic.services.appointment;

import com.tuorg.businesslogic.dto.appointment.AppointmentDto;
import com.tuorg.businesslogic.dto.appointment.CreateAppointmentDto;
import com.tuorg.businesslogic.dto.appointment.RescheduleAppointmentDto;
import com.tuorg.businesslogic.dto.appointment.CancelAppointmentDto;
import com.tuorg.businesslogic.dependencies.IUnitOfWorkDependency;
import com.tuorg.businesslogic.mapper.AppointmentMapper;
import com.tuorg.domain.entities.Appointment;
import com.tuorg.domain.entities.TimeSlot;
import com.tuorg.domain.repositories.AppointmentRepository;
import com.tuorg.domain.repositories.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentAppService implements IAppointmentAppService {

    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final AppointmentMapper mapper;
    private final IUnitOfWorkDependency uow;
    private final LockProvider lockProvider;

    public AppointmentAppService(
            AppointmentRepository appointmentRepository,
            TimeSlotRepository timeSlotRepository,
            AppointmentMapper mapper,
            IUnitOfWorkDependency uow,
            LockProvider lockProvider) {
        this.appointmentRepository = appointmentRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.mapper = mapper;
        this.uow = uow;
        this.lockProvider = lockProvider;
    }

    @Override
    public List<AppointmentDto> getAll(UUID patientId, UUID doctorId) {
        return appointmentRepository.findByOptionalFilters(patientId, doctorId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDto getById(UUID id) {
        Appointment ap = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        return mapper.toDto(ap);
    }

    @Override
    public AppointmentDto create(CreateAppointmentDto dto) {
        String lockKey = lockKeyFor(dto.doctorId, dto.desiredStart);
        if (!lockProvider.acquire(lockKey, 5_000)) {
            throw new IllegalStateException("Slot is being reserved by another request");
        }
        try {
            // Start transaction managed by Spring @Transactional on repository level
            // Validate availability
            OffsetDateTime start = dto.desiredStart;
            OffsetDateTime end = start.plusMinutes(30); // asumimos 30 min por defecto
            boolean conflict = appointmentRepository.existsOverlap(dto.doctorId, start, end);
            if (conflict) throw new IllegalStateException("Requested slot not available");

            Appointment entity = mapper.fromCreateDto(dto);
            entity.setStart(start);
            entity.setEnd(end);
            appointmentRepository.save(entity);
            // mark timeslot if using explicit TimeSlot entity
            TimeSlot ts = timeSlotRepository.findByDoctorAndStart(dto.doctorId, start).orElse(null);
            if (ts != null) {
                ts.setStatus("BOOKED");
                timeSlotRepository.save(ts);
            }
            return mapper.toDto(entity);
        } finally {
            lockProvider.release(lockKey);
        }
    }

    @Override
    public AppointmentDto reschedule(UUID id, RescheduleAppointmentDto dto) {
        Appointment ap = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        String lockKey = lockKeyFor(ap.getDoctorId(), dto.newStart);
        if (!lockProvider.acquire(lockKey, 5_000)) {
            throw new IllegalStateException("Slot is being reserved by another request");
        }
        try {
            OffsetDateTime newStart = dto.newStart;
            OffsetDateTime newEnd = newStart.plusMinutes(30);
            boolean conflict = appointmentRepository.existsOverlap(ap.getDoctorId(), newStart, newEnd);
            if (conflict) throw new IllegalStateException("Requested new slot not available");

            // update appointment
            ap.setStart(newStart);
            ap.setEnd(newEnd);
            appointmentRepository.save(ap);
            return mapper.toDto(ap);
        } finally {
            lockProvider.release(lockKey);
        }
    }

    @Override
    public void cancel(UUID id, CancelAppointmentDto dto) {
        Appointment ap = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        ap.setStatus("CANCELLED");
        ap.setReason(dto.reason);
        appointmentRepository.save(ap);
        // optional: free timeslot
        timeSlotRepository.findByDoctorAndStart(ap.getDoctorId(), ap.getStart()).ifPresent(ts -> {
            ts.setStatus("FREE");
            timeSlotRepository.save(ts);
        });
    }

    private String lockKeyFor(UUID doctorId, OffsetDateTime start) {
        return "doctor:" + doctorId.toString() + ":start:" + start.toString();
    }
}
