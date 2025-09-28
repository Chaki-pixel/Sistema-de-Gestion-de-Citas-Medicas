package com.sga.domain.entities.usuario;

import com.sga.domain.base.AuditEntity;

public class Usuario extends AuditEntity {
    private int id;
    private String nombreUsuario;
    private String claveHash;
    private String rol = "Recepcionista";

    // Getters and setters
}
