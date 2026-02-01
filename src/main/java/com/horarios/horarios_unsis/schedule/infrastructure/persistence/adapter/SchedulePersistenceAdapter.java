package com.horarios.horarios_unsis.schedule.infrastructure.persistence.adapter;

import com.horarios.horarios_unsis.schedule.domain.model.Schedule;
import com.horarios.horarios_unsis.schedule.domain.port.out.SchedulePersistentPort;
import com.horarios.horarios_unsis.schedule.infrastructure.persistence.entity.ScheduleEntity;
import com.horarios.horarios_unsis.schedule.infrastructure.persistence.repository.ScheduleRepository;
import com.horarios.horarios_unsis.schedule.application.mapper.ScheduleMapper;
import org.springframework.stereotype.Component;

@Component
public class SchedulePersistenceAdapter implements SchedulePersistentPort {

    private final ScheduleRepository scheduleRepository;

    public SchedulePersistenceAdapter(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void save(Schedule schedule) {
        ScheduleEntity entity = ScheduleMapper.toEntity(schedule);
        scheduleRepository.save(entity);
    }
}
