package com.sga.persistence.repositories.paciente;

import com.sga.domain.entities.paciente.Paciente;
import com.sga.persistence.interfaces.PacienteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacienteRepositoryImpl implements PacienteRepository {

    private final List<Paciente> pacientes = new ArrayList<>();

    @Override
    public List<Paciente> findAll() {
        return pacientes;
    }

    @Override
    public Optional<Paciente> findById(int id) {
        return pacientes.stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public void save(Paciente entity) {
        pacientes.add(entity);
    }

    @Override
    public void update(Paciente entity) {
        delete(entity.getId());
        save(entity);
    }

    @Override
    public void delete(int id) {
        pacientes.removeIf(p -> p.getId() == id);
    }
}
