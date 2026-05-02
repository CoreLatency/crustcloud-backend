package com.crustcloud.production.controller;

import com.crustcloud.production.dto.ScheduleDTO;
import com.crustcloud.production.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class SchedulingController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleDTO.ScheduleResponse>> getSchedules() {
        return ResponseEntity.ok(scheduleService.getSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO.ScheduleResponse> getScheduleById(@PathVariable Long id) {
        ScheduleDTO.ScheduleResponse schedule = scheduleService.getScheduleById(id);
        if (schedule != null) {
            return ResponseEntity.ok(schedule);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ScheduleDTO.ScheduleResponse> createSchedule(@RequestBody ScheduleDTO.CreateSchedule request) {
        return ResponseEntity.ok(scheduleService.createSchedule(request));
    }

    @PostMapping("/find-or-create")
    public ResponseEntity<ScheduleDTO.ScheduleResponse> findOrCreateSchedule(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(scheduleService.findOrCreateByDate(date));
    }

    @PostMapping("/{id}/shifts")
    public ResponseEntity<ScheduleDTO.ScheduleResponse> addShift(
            @PathVariable Long id,
            @RequestBody ScheduleDTO.CreateShift request) {
        try {
            return ResponseEntity.ok(scheduleService.addShift(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{scheduleId}/shifts/{shiftId}")
    public ResponseEntity<Void> deleteShift(
            @PathVariable Long scheduleId,
            @PathVariable Long shiftId) {
        if (scheduleService.deleteShift(scheduleId, shiftId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
