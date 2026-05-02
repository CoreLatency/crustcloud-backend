package com.crustcloud.production.controller;

import com.crustcloud.production.dto.ProductionDTO;
import com.crustcloud.production.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionService productionService;

    @GetMapping("/lines")
    public ResponseEntity<List<ProductionDTO.LineResponse>> getProductionLines() {
        return ResponseEntity.ok(productionService.getProductionLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<ProductionDTO.LineResponse> getProductionLine(@PathVariable Long id) {
        ProductionDTO.LineResponse line = productionService.getProductionLine(id);
        if (line != null) {
            return ResponseEntity.ok(line);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/lines/{id}/status")
    public ResponseEntity<ProductionDTO.LineResponse> updateLineStatus(
            @PathVariable Long id,
            @RequestBody ProductionDTO.StatusUpdate request) {
        ProductionDTO.LineResponse updated = productionService.updateLineStatus(id, request.getStatus());
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/lines/{id}/progress")
    public ResponseEntity<ProductionDTO.LineResponse> updateLineProgress(
            @PathVariable Long id,
            @RequestBody ProductionDTO.ProgressUpdate request) {
        ProductionDTO.LineResponse updated = productionService.updateLineProgress(id, request.getProgress());
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
}
