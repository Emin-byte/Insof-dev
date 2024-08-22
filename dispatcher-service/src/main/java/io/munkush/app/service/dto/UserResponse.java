package io.munkush.app.service.dto;


import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String login,
        String code,
        LocalDateTime createDate) {
}
