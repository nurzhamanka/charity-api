package kz.peep.api.entities

import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Embeddable
data class Location(
        @NotBlank
        var longitude: Double,

        @NotBlank
        var latitude: Double
)