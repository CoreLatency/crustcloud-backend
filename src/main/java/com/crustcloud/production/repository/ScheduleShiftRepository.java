package com.crustcloud.production.repository;

import com.crustcloud.production.model.ScheduleShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleShiftRepository extends JpaRepository<ScheduleShift, Long> {
    List<ScheduleShift> findByScheduleId(Long scheduleId);
}
