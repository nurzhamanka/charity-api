package kz.peep.api.dto.user

import org.apache.commons.lang3.ObjectUtils

class UserSummaryResponse (
        val id: Long,
        val username: String,
        val name: String
) {
    private constructor(builder: Builder) : this(builder.id!!, builder.username!!, builder.name!!)

    class Builder {
        var id: Long? = null
            private set
        var username: String? = null
            private set
        var name: String? = null
            private set

        fun id(id: Long) = apply { this.id = id }
        fun username(username: String) = apply { this.username = username }
        fun name(name: String) = apply { this.name = name }

        fun build() = if (ObjectUtils.allNotNull(id, username, name))
            UserSummaryResponse(this)
        else throw IllegalStateException("Properties must not be null.")
    }
}