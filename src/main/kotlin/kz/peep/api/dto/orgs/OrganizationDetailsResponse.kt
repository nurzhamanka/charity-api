package kz.peep.api.dto.orgs

import kz.peep.api.entities.Location
import kz.peep.api.infrastructure.structs.EffortType

data class OrganizationDetailsResponse (
        val name : String,
        val description: String?,
        val location: Location?,
        val donationTypes: List<EffortType>
)