package com.tuorg.businesslogic.services.appointment;

import com.tuorg.businesslogic.dto.appointment.AppointmentDto;
import com.tuorg.businesslogic.dto.appointment.CreateAppointmentDto;
import com.tuorg.businesslogic.dto.appointment.RescheduleAppointmentDto;
import com.tuorg.businesslogic.dto.appointment.CancelAppointmentDto;

import java.util.List;
import java.util.UUID;

public interface IAppointmentAppService {
    List<AppointmentDto> getAll(UUID patientId, UUID doctorId);
    AppointmentDto getById(UUID id);
    AppointmentDto create(CreateAppointmentDto dto);
    AppointmentDto reschedule(UUID id, RescheduleAppointmentDto dto);
    void cancel(UUID id, CancelAppointmentDto dto);
}
