package kz.peep.api.dto.auth

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class RegisterRequest (
        @NotBlank
        @Size(min = 2, max = 40)
        var name: String,

        @NotBlank
        @Size(min = 5, max = 15)
        var username: String,

        @NotBlank
        @Size(min = 6, max = 20)
        var password: String
)