package com.horarios.horarios_unsis.data.career.domain.port.out;

import com.horarios.horarios_unsis.data.career.domain.model.Career;
import java.util.List;
import java.util.Optional;

public interface CareerRepositoryPort {
    Career save(Career career);
    Optional<Career> findById(Integer id);
    Career findByClave(String clave);
    List<Career> findAll();
}
