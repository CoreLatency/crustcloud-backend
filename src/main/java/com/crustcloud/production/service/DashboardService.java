package com.crustcloud.production.service;

import com.crustcloud.production.dto.DashboardDTO;
import com.crustcloud.production.model.ProductionBatch;
import com.crustcloud.production.repository.ProductionBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProductionBatchRepository batchRepository;

    private static final String[] COLORS = {
        "from-amber-600 to-amber-400",
        "from-emerald-600 to-emerald-400",
        "from-rose-500 to-rose-400",
        "from-violet-600 to-violet-400",
        "from-cyan-600 to-cyan-400"
    };

    public DashboardDTO.Stats getStats() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");

        List<ProductionBatch> batches = batchRepository.findByPlanDate(today);
        int totalBatches = batches.size();
        int totalOutput = batches.stream()
            .mapToInt(b -> b.getTotalQty() != null ? b.getTotalQty() : 0)
            .sum();

        String firstStart = batchRepository.findFirstStartByPlanDate(today);
        String lastEnd = batchRepository.findLastEndByPlanDate(today);

        return DashboardDTO.Stats.builder()
            .totalBatches(totalBatches)
            .totalOutput(totalOutput + " pkg")
            .planDate(today.format(formatter))
            .firstStart(today.format(formatter) + " " + (firstStart != null ? firstStart : "--:--"))
            .lastEnd(today.format(formatter) + " " + (lastEnd != null ? lastEnd : "--:--"))
            .build();
    }

    public List<DashboardDTO.GanttProduct> getGanttData() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<ProductionBatch> batches = batchRepository.findByPlanDate(today);

        return batches.stream().map(batch -> {
            List<DashboardDTO.TimelineSegment> timeline = generateTimeline(batch);
            return DashboardDTO.GanttProduct.builder()
                .id(batch.getId().toString())
                .name(batch.getProduct().getName())
                .timeline(timeline)
                .build();
        }).collect(Collectors.toList());
    }

    private List<DashboardDTO.TimelineSegment> generateTimeline(ProductionBatch batch) {
        List<DashboardDTO.TimelineSegment> segments = new ArrayList<>();
        String[] stages = {"mixing", "dividing", "panning", "baking", "packaging"};
        int[] widths = {28, 6, 6, 15, 20};

        int startPercent = batch.getId().intValue() * 10 % 30;

        for (int i = 0; i < stages.length; i++) {
            segments.add(DashboardDTO.TimelineSegment.builder()
                .stage(stages[i])
                .start(startPercent)
                .width(widths[i])
                .build());
            startPercent += widths[i];
        }

        return segments;
    }

    public List<DashboardDTO.OutputProduct> getOutputData() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<ProductionBatch> batches = batchRepository.findByPlanDate(today);

        int maxOutput = batches.stream()
            .mapToInt(b -> b.getTotalQty() != null ? b.getTotalQty() : 0)
            .max()
            .orElse(25);
        maxOutput = Math.max(maxOutput + 5, 25);

        List<DashboardDTO.OutputProduct> result = new ArrayList<>();
        int colorIndex = 0;

        for (ProductionBatch batch : batches) {
            result.add(DashboardDTO.OutputProduct.builder()
                .id(batch.getId().toString())
                .name(batch.getProduct().getName())
                .output(batch.getTotalQty() != null ? batch.getTotalQty() : 0)
                .maxOutput(maxOutput)
                .color(COLORS[colorIndex % COLORS.length])
                .build());
            colorIndex++;
        }

        return result;
    }

    public List<DashboardDTO.TableRow> getTableData() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<ProductionBatch> batches = batchRepository.findByPlanDate(today);

        return batches.stream().map(this::mapToTableRow).collect(Collectors.toList());
    }

    private DashboardDTO.TableRow mapToTableRow(ProductionBatch batch) {
        return DashboardDTO.TableRow.builder()
            .id(batch.getId().toString())
            .status(batch.getDisplayStatus())
            .product(batch.getProduct().getName())
            .salesOrder(batch.getSalesOrder() != null ? batch.getSalesOrder() : "—")
            .totalQty(batch.getTotalQty())
            .soCoExcess(batch.getSoCoExcess())
            .exchLoss(batch.getExchLoss())
            .excess(batch.getExcess())
            .samples(batch.getSamples())
            .carryOver(batch.getCarryOver())
            .theorExcess(batch.getTheorExcess())
            .batchQty(batch.getBatchQty())
            .capacity(batch.getCapacity())
            .dough(batch.getDough())
            .procTime(batch.getProcTime())
            .startSponge(batch.getStartSponge())
            .endDough(batch.getEndDough())
            .endBatch(batch.getEndBatch())
            .orderBatch(batch.getOrderBatch())
            .lineBatch(batch.getLineBatch())
            .build();
    }
}
