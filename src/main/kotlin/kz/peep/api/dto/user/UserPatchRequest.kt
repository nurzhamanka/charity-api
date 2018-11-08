package kz.peep.api.dto.user

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class UserPatchRequest (
        @NotBlank
        val name: String? = null,

        @NotBlank
        @Size(min = 6, max = 20)
        var password: String? = null,

        @Pattern(regexp="^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
        val phoneNumber: String? = null,

        val avatarStyle: String? = null,

        val avatarColor: String? = null
)