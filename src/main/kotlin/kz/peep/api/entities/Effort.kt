package kz.peep.api.entities

import kz.peep.api.entities.audit.UserDateAudit
import kz.peep.api.infrastructure.structs.ClothingType
import javax.persistence.*

@Entity
@Table(name = "EFFORT")
data class Effort (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "EFFORT_TYPE_ID")
        val donationType : EffortType,

        val moneyAmount: Double? = null,
        val quantity: Int? = null,

        @Enumerated(EnumType.STRING)
        val clothingType: ClothingType? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "ORGANIZATION_ID")
        val organization: Organization
) : UserDateAudit()