package kz.peep.api.dto.auth

import javax.validation.constraints.NotBlank

data class LoginRequest (
        @NotBlank
        var username: String,

        @NotBlank
        var password: String
)