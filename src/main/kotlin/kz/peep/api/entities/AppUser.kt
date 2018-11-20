package kz.peep.api.entities

import kz.peep.api.entities.audit.DateAudit
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
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
        var name: String,

        @Pattern(regexp="^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")
        var phoneNumber: String? = null,

        @Embedded
        @AttributeOverrides(
                AttributeOverride(name = "style", column = Column(name = "avatarStyle")),
                AttributeOverride(name = "color", column = Column(name = "avatarColor"))
        )
        var avatar: UserAvatar = UserAvatar.generateAvatar(),

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "USER_ROLES",
                joinColumns = [JoinColumn(name = "USER_ID")],
                inverseJoinColumns = [JoinColumn(name = "ROLE_ID")])
        var roles: MutableSet<AppUserRole> = HashSet()
) : DateAudit()