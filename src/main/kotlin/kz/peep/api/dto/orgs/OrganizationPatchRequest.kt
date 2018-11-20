package kz.peep.api.dto.orgs

import kz.peep.api.entities.Location
import javax.validation.constraints.NotBlank

data class OrganizationPatchRequest (
        @NotBlank
        val name : String? = null,

        @NotBlank
        val description: String? = null,
        val location: Location? = null
)