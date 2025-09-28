package com.sga.domain.entities.paciente;

import com.sga.domain.base.AuditEntity;
import java.time.LocalDate;

public class Paciente extends AuditEntity {
    private int id;
    private String nombre;
    private String cedula;
    private LocalDate fechaNacimiento;
    private String telefono;

    // Getters and setters
}
