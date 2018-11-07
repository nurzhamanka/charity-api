package kz.peep.api.dto.user

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserRegisterRequest (
        @NotBlank
        val name: String,

        @NotBlank
        @Email
        val username: String,

        @NotBlank
        @Size(min = 6, max = 20)
        val password: String
)