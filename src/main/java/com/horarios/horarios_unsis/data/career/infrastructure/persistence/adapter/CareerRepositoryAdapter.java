package com.horarios.horarios_unsis.data.career.infrastructure.persistence.adapter;

import com.horarios.horarios_unsis.data.career.domain.model.Career;
import com.horarios.horarios_unsis.data.career.domain.port.out.CareerRepositoryPort;
import com.horarios.horarios_unsis.data.career.infrastructure.persistence.entity.CareerEntity;
import com.horarios.horarios_unsis.data.career.infrastructure.persistence.repository.CareerRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CareerRepositoryAdapter implements CareerRepositoryPort {

    private final CareerRepository careerRepository;

    public CareerRepositoryAdapter(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    @Override
    public Career save(Career career) {
        CareerEntity entity = toEntity(career);
        return toDomain(careerRepository.save(entity));
    }

    @Override
    public Optional<Career> findById(Integer id) {
        return careerRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Career findByClave(String clave) {
        return careerRepository.findByClave(clave)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<Career> findAll() {
        return careerRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private CareerEntity toEntity(Career domain) {
        if (domain == null) return null;
        CareerEntity entity = new CareerEntity();
        entity.setIdCarrera(domain.getIdCarrera());
        entity.setClave(domain.getClave());
        entity.setNombre(domain.getNombre());
        entity.setVigente(domain.getVigente());
        return entity;
    }

    private Career toDomain(CareerEntity entity) {
        if (entity == null) return null;
        return new Career(
                entity.getIdCarrera(),
                entity.getClave(),
                entity.getNombre(),
                entity.getVigente()
        );
    }
}
