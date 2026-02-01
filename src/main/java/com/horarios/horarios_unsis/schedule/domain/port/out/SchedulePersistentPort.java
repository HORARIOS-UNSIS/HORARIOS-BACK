package com.horarios.horarios_unsis.schedule.domain.port.out;

import com.horarios.horarios_unsis.schedule.domain.model.Schedule;
import org.springframework.stereotype.Component;

@Component
public interface SchedulePersistentPort {
    void save(Schedule schedule);
}
