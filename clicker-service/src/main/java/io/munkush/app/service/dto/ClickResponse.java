package io.munkush.app.service.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ClickResponse(Long id,
                            String username,
                            LocalDateTime createDate,
                            String x,
                            String y) {
}
