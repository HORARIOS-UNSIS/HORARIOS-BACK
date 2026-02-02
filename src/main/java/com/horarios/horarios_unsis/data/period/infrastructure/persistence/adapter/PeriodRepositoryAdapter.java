package com.horarios.horarios_unsis.data.period.infrastructure.persistence.adapter;

import com.horarios.horarios_unsis.data.period.domain.model.Period;
import com.horarios.horarios_unsis.data.period.domain.port.out.PeriodRepositoryPort;
import com.horarios.horarios_unsis.data.period.infrastructure.persistence.entity.PeriodEntity;
import com.horarios.horarios_unsis.data.period.infrastructure.persistence.repository.PeriodRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PeriodRepositoryAdapter implements PeriodRepositoryPort {

    private final PeriodRepository periodRepository;

    public PeriodRepositoryAdapter(PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    @Override
    public Period save(Period period) {
        PeriodEntity entity = toEntity(period);
        return toDomain(periodRepository.save(entity));
    }

    @Override
    public Optional<Period> findById(Integer id) {
        return periodRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Period findByClave(String clave) {
        return periodRepository.findByClave(clave)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<Period> findAll() {
        return periodRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private PeriodEntity toEntity(Period domain) {
        if (domain == null) return null;
        PeriodEntity entity = new PeriodEntity();
        entity.setIdPeriodo(domain.getIdPeriodo());
        entity.setActivo(domain.getActivo());
        entity.setClave(domain.getClave());
        entity.setFechaFin(domain.getFechaFin());
        entity.setFechaInicio(domain.getFechaInicio());
        entity.setNombre(domain.getNombre());
        entity.setTipo(domain.getTipo());
        return entity;
    }

    private Period toDomain(PeriodEntity entity) {
        if (entity == null) return null;
        return new Period(
                entity.getIdPeriodo(),
                entity.getActivo(),
                entity.getClave(),
                entity.getFechaFin(),
                entity.getFechaInicio(),
                entity.getNombre(),
                entity.getTipo()
        );
    }
}
