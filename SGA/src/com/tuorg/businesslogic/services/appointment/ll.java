package com.tuorg.businesslogic.services.appointment;

import com.tuorg.businesslogic.dto.appointment.CreateAppointmentDto;
import com.tuorg.businesslogic.mapper.AppointmentMapper;
import com.tuorg.domain.entities.Appointment;
import com.tuorg.domain.repositories.AppointmentRepository;
import com.tuorg.domain.repositories.TimeSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppointmentAppServiceTest {

    @Mock AppointmentRepository appointmentRepository;
    @Mock TimeSlotRepository timeSlotRepository;
    @Mock AppointmentMapper mapper;
    @Mock LockProvider lockProvider;
    @InjectMocks AppointmentAppService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_callsSaveAndReturnsDto() {
        CreateAppointmentDto dto = new CreateAppointmentDto();
        dto.doctorId = UUID.randomUUID();
        dto.patientId = UUID.randomUUID();
        dto.desiredStart = OffsetDateTime.now().plusDays(1);
        dto.reason = "Consulta";
        Appointment entity = new Appointment();
        when(lockProvider.acquire(any(), anyLong())).thenReturn(true);
        when(appointmentRepository.existsOverlap(dto.doctorId, dto.desiredStart, dto.desiredStart.plusMinutes(30))).thenReturn(false);
        when(mapper.fromCreateDto(dto)).thenReturn(entity);
        when(appointmentRepository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(new com.tuorg.businesslogic.dto.appointment.AppointmentDto());
        service.create(dto);
        verify(appointmentRepository, times(1)).save(entity);
        verify(lockProvider, times(1)).release(any());
    }
}
