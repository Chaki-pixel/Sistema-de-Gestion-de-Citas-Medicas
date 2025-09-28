package com.sga.domain.entities.cita;

import com.sga.domain.base.AuditEntity;
import java.time.LocalDateTime;

public class Cita extends AuditEntity {
    private int id;
    private int pacienteId;
    private int medicoId;
    private LocalDateTime fechaHora;
    private String estado = "Pendiente";

    // Getters and setters
}
