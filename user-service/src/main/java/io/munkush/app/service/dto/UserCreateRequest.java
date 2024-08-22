package io.munkush.app.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotNull(message = "login should not be null")
        @NotEmpty(message = "login should not be empty")
        @NotBlank(message = "login should not be blank")
        @Size(min = 4, max = 16, message = "code should be in 4-16 symbols")
        String login,
        @NotNull(message = "code should not be null")
        @NotEmpty(message = "code should not be empty")
        @NotBlank(message = "code should not be blank")
        String code) {
}
