package kz.peep.api.entities

import kz.peep.api.entities.audit.UserDateAudit
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "EFFORT")
data class Effort (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @NotBlank
        @Size(max = 50)
        val name: String
) : UserDateAudit()