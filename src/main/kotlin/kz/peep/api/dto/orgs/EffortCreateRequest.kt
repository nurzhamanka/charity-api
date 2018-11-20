package kz.peep.api.dto.orgs

import kz.peep.api.infrastructure.structs.ClothingType
import kz.peep.api.infrastructure.structs.EffortType

data class EffortCreateRequest (
        val type : EffortType,
        val moneyAmount: Double? = null,
        val quantity: Int? = null,
        val clothingType: ClothingType? = null
)