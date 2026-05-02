package com.crustcloud.production.controller;

import com.crustcloud.production.dto.DashboardDTO;
import com.crustcloud.production.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardDTO.Stats> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }

    @GetMapping("/gantt")
    public ResponseEntity<List<DashboardDTO.GanttProduct>> getGanttData() {
        return ResponseEntity.ok(dashboardService.getGanttData());
    }

    @GetMapping("/output")
    public ResponseEntity<List<DashboardDTO.OutputProduct>> getOutputData() {
        return ResponseEntity.ok(dashboardService.getOutputData());
    }

    @GetMapping("/table")
    public ResponseEntity<List<DashboardDTO.TableRow>> getTableData() {
        return ResponseEntity.ok(dashboardService.getTableData());
    }
}
