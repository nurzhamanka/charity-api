package kz.peep.api.dto.orgs

import kz.peep.api.entities.Location
import kz.peep.api.infrastructure.structs.EffortType
import javax.validation.constraints.NotBlank

data class OrganizationCreateRequest (
        @NotBlank
        val name : String,

        @NotBlank
        val description: String? = null,
        val location: Location? = null,
        val donationTypes: List<EffortType>
)