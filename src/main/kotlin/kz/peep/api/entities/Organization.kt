package kz.peep.api.entities

import kz.peep.api.entities.audit.UserDateAudit
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import kotlin.math.max

@Entity
@Table(name = "ORGANIZATION")
data class Organization (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @NotBlank
        @Size(max = 50)
        val name: String,

        @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        @JoinColumn(name = "MANAGER_ID")
        val manager: AppUser
) : UserDateAudit()