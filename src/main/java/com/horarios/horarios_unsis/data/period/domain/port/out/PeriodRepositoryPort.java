package com.horarios.horarios_unsis.data.period.domain.port.out;

import com.horarios.horarios_unsis.data.period.domain.model.Period;
import java.util.List;
import java.util.Optional;

public interface PeriodRepositoryPort {
    Period save(Period period);
    Optional<Period> findById(Integer id);
    Period findByClave(String clave);
    List<Period> findAll();
}
