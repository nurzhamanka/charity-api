package kz.peep.api.dto.orgs

import kz.peep.api.infrastructure.structs.ClothingType
import kz.peep.api.infrastructure.structs.EffortType
import java.time.Instant

data class EffortDetailsResponse (
        val donationType : EffortType,
        val moneyAmount: Double? = null,
        val quantity: Int? = null,
        val clothingType: ClothingType? = null,
        val donatedBy: String,
        val date: Instant
)