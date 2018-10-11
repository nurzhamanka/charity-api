package kz.peep.api.entities

import kz.peep.api.entities.audit.DateAudit
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "APP_USER", uniqueConstraints = [
        UniqueConstraint(columnNames = ["username"])
])
data class AppUser (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1,

        @NotBlank
        @Size(max = 15)
        val username: String,

        @NotBlank
        @Size(max = 100)
        var password: String,

        @NotBlank
        @Size(max = 20)
        val firstName: String,

        @NotBlank
        @Size(max = 20)
        val lastName: String,

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "USER_ROLES",
                joinColumns = [JoinColumn(name = "USER_ID")],
                inverseJoinColumns = [JoinColumn(name = "ROLE_ID")])
        var roles: MutableSet<AppUserRole> = HashSet()
) : DateAudit()