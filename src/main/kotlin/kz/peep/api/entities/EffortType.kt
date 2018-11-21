package kz.peep.api.entities

import kz.peep.api.infrastructure.structs.EffortType
import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name = "EFFORT_TYPE")
data class EffortType (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1,

        @Enumerated(EnumType.STRING)
        @NaturalId
        @Column(length = 60)
        val name: EffortType
)