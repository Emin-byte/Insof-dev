package io.munkush.app.service.dto;

import java.time.LocalDateTime;

public record ClickResponse(Long id,
                            String username,
                            LocalDateTime createDate,
                            String x,
                            String y) {
}