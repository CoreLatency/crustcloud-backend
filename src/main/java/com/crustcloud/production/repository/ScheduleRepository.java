package com.crustcloud.production.repository;

import com.crustcloud.production.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByScheduleDate(LocalDate date);

    @Query("SELECT s FROM Schedule s WHERE s.scheduleDate >= :startDate AND s.scheduleDate <= :endDate ORDER BY s.scheduleDate ASC")
    List<Schedule> findByScheduleDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT s FROM Schedule s WHERE s.scheduleDate >= :today ORDER BY s.scheduleDate ASC")
    List<Schedule> findUpcomingSchedules(@Param("today") LocalDate today);

    @Query("SELECT s FROM Schedule s LEFT JOIN FETCH s.shifts WHERE s.id = :id")
    Optional<Schedule> findByIdWithShifts(@Param("id") Long id);
}
