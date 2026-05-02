package com.crustcloud.production.service;

import com.crustcloud.production.dto.AlertDTO;
import com.crustcloud.production.model.Alert;
import com.crustcloud.production.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    public List<AlertDTO> getAlerts() {
        return alertRepository.findTop20ByOrderByCreatedAtDesc().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public long getUnreadCount() {
        return alertRepository.countByIsReadFalse();
    }

    @Transactional
    public AlertDTO markAsRead(Long id) {
        return alertRepository.findById(id)
            .map(alert -> {
                alert.setIsRead(true);
                return mapToDTO(alertRepository.save(alert));
            })
            .orElse(null);
    }

    @Transactional
    public boolean deleteAlert(Long id) {
        if (alertRepository.existsById(id)) {
            alertRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private AlertDTO mapToDTO(Alert alert) {
        return AlertDTO.builder()
            .id(alert.getId())
            .type(alert.getType())
            .message(alert.getMessage())
            .time(alert.getTimeAgo())
            .isRead(alert.getIsRead())
            .build();
    }
}
