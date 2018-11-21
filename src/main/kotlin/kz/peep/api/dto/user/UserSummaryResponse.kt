package kz.peep.api.dto.user

import kz.peep.api.entities.AppUserRole
import kz.peep.api.infrastructure.structs.UserRole
import org.apache.commons.lang3.ObjectUtils

class UserSummaryResponse (
        val id: Long,
        val username: String,
        val name: String,
        val roles: List<UserRole>,
        val points: Int
) {
    private constructor(builder: Builder) : this(builder.id!!, builder.username!!, builder.name!!, builder.roles!!, builder.points!!)

    class Builder {
        var id: Long? = null
            private set
        var username: String? = null
            private set
        var name: String? = null
            private set
        var roles: List<UserRole>? = null
            private set
        var points: Int? = null
            private set

        fun id(id: Long) = apply { this.id = id }
        fun username(username: String) = apply { this.username = username }
        fun name(name: String) = apply { this.name = name }
        fun roles(roles: MutableSet<AppUserRole>) = apply { this.roles = roles.map{it.name}}
        fun points(points: Int) = apply { this.points = points }

        fun build() = if (ObjectUtils.allNotNull(id, username, name, roles, points) && !roles!!.isEmpty())
            UserSummaryResponse(this)
        else throw IllegalStateException("Properties must not be null.")
    }
}