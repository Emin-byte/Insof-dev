package io.munkush.app.service.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
        Long id,
        String login,
        String code,
        LocalDateTime createDate) {
}
