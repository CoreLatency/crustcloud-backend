package com.crustcloud.production.service;

import com.crustcloud.production.dto.ProductionDTO;
import com.crustcloud.production.model.ProductionLine;
import com.crustcloud.production.repository.ProductionLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductionService {

    private final ProductionLineRepository lineRepository;

    public List<ProductionDTO.LineResponse> getProductionLines() {
        return lineRepository.findAll().stream()
            .map(this::mapToLineResponse)
            .collect(Collectors.toList());
    }

    public ProductionDTO.LineResponse getProductionLine(Long id) {
        return lineRepository.findById(id)
            .map(this::mapToLineResponse)
            .orElse(null);
    }

    @Transactional
    public ProductionDTO.LineResponse updateLineStatus(Long id, String status) {
        return lineRepository.findById(id)
            .map(line -> {
                line.setStatus(status);
                return mapToLineResponse(lineRepository.save(line));
            })
            .orElse(null);
    }

    @Transactional
    public ProductionDTO.LineResponse updateLineProgress(Long id, Integer progress) {
        return lineRepository.findById(id)
            .map(line -> {
                line.setProgress(progress);
                return mapToLineResponse(lineRepository.save(line));
            })
            .orElse(null);
    }

    private ProductionDTO.LineResponse mapToLineResponse(ProductionLine line) {
        return ProductionDTO.LineResponse.builder()
            .id(line.getId())
            .name(line.getName())
            .product(line.getCurrentProduct() != null ? line.getCurrentProduct().getName() : "—")
            .status(line.getStatus())
            .progress(line.getProgress())
            .currentStage(line.getCurrentStage())
            .batchNumber(line.getCurrentBatch() != null ? line.getCurrentBatch().getBatchNumber() : "—")
            .startTime(line.getStartTime())
            .estimatedEnd(line.getEstimatedEnd())
            .temperature(line.getTemperature())
            .humidity(line.getHumidity())
            .build();
    }
}
