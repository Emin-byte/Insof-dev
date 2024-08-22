package io.munkush.app.service.dto;

import jakarta.validation.constraints.*;

public record UserLoginRequest(
        @NotNull(message = "login should not be null")
        @NotEmpty(message = "login should not be empty")
        @NotBlank(message = "login should not be blank")
        @Size(min = 4, max = 16, message = "login should be in 4-16 symbols")
        String login,
        @NotNull(message = "code should not be null")
        @NotEmpty(message = "code should not be empty")
        @NotBlank(message = "code should not be blank")
        String code
) {

}
