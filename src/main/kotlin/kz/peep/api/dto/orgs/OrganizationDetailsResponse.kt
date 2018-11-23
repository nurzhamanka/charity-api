package kz.peep.api.dto.orgs

import kz.peep.api.infrastructure.structs.EffortType

data class OrganizationDetailsResponse (
        val id: Long,
        val name : String,
        val description: String?,
        val createdBy: String,
        val donationTypes: List<EffortType>,
        val donationCount: Int
)