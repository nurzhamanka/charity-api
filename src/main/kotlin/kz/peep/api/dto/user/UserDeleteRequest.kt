package kz.peep.api.dto.user

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserDeleteRequest (
        @NotBlank
        @Size(min = 6, max = 20)
        var password: String
)