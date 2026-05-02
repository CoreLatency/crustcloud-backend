package com.crustcloud.production.service;

import com.crustcloud.production.dto.ScheduleDTO;
import com.crustcloud.production.model.Product;
import com.crustcloud.production.model.Schedule;
import com.crustcloud.production.model.ScheduleShift;
import com.crustcloud.production.repository.ProductRepository;
import com.crustcloud.production.repository.ScheduleRepository;
import com.crustcloud.production.repository.ScheduleShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleShiftRepository shiftRepository;
    private final ProductRepository productRepository;

    public List<ScheduleDTO.ScheduleResponse> getSchedules() {
        return scheduleRepository.findUpcomingSchedules(LocalDate.now().minusDays(7)).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public ScheduleDTO.ScheduleResponse getScheduleById(Long id) {
        return scheduleRepository.findByIdWithShifts(id)
            .map(this::mapToResponse)
            .orElse(null);
    }

    @Transactional
    public ScheduleDTO.ScheduleResponse createSchedule(ScheduleDTO.CreateSchedule request) {
        Schedule schedule = Schedule.builder()
            .scheduleDate(request.getDate())
            .build();

        return mapToResponse(scheduleRepository.save(schedule));
    }

    @Transactional
    public ScheduleDTO.ScheduleResponse findOrCreateByDate(LocalDate date) {
        return scheduleRepository.findByScheduleDate(date)
                .map(this::mapToResponse)
                .orElseGet(() -> {
                    Schedule schedule = Schedule.builder()
                            .scheduleDate(date)
                            .build();
                    return mapToResponse(scheduleRepository.save(schedule));
                });
    }

    @Transactional
    public ScheduleDTO.ScheduleResponse addShift(Long scheduleId, ScheduleDTO.CreateShift request) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new RuntimeException("Schedule not found"));

        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

        ScheduleShift shift = ScheduleShift.builder()
            .schedule(schedule)
            .product(product)
            .timeSlot(request.getTimeSlot())
            .batches(request.getBatches())
            .status("scheduled")
            .build();

        shiftRepository.save(shift);

        return mapToResponse(scheduleRepository.findByIdWithShifts(scheduleId).orElse(schedule));
    }

    @Transactional
    public boolean deleteShift(Long scheduleId, Long shiftId) {
        ScheduleShift shift = shiftRepository.findById(shiftId)
            .orElse(null);

        if (shift != null && shift.getSchedule().getId().equals(scheduleId)) {
            shiftRepository.delete(shift);
            return true;
        }
        return false;
    }

    private ScheduleDTO.ScheduleResponse mapToResponse(Schedule schedule) {
        List<ScheduleDTO.ShiftResponse> shifts = schedule.getShifts().stream()
            .map(shift -> ScheduleDTO.ShiftResponse.builder()
                .id(shift.getId())
                .timeSlot(shift.getTimeSlot())
                .product(shift.getProduct().getName())
                .productId(shift.getProduct().getId())
                .status(shift.getStatus())
                .batches(shift.getBatches())
                .build())
            .collect(Collectors.toList());

        return ScheduleDTO.ScheduleResponse.builder()
            .id(schedule.getId())
            .date(schedule.getScheduleDate().format(DateTimeFormatter.ISO_DATE))
            .shifts(shifts)
            .build();
    }
}
