package kz.peep.api.entities

import kz.peep.api.infrastructure.structs.UserRole
import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name = "APP_USER_ROLE")
data class AppUserRole (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = -1,

        @Enumerated(EnumType.STRING)
        @NaturalId
        @Column(length = 60)
        val name: UserRole
)