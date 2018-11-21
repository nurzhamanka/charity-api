package kz.peep.api.dto.orgs

import javax.validation.constraints.NotBlank

data class OrganizationPatchRequest (
        @NotBlank
        val name : String? = null,

        @NotBlank
        val description: String? = null
)