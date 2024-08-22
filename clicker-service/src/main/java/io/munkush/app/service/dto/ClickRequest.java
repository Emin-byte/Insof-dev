package io.munkush.app.service.dto;

import jakarta.validation.constraints.NotNull;

public record ClickRequest(
        @NotNull(message = "x coordinate should not be null")
        String x,
        @NotNull(message = "y coordinate should not be null")
        String y,
        String username
) {
}
