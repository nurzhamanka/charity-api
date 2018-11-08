package kz.peep.api.dto.user

import org.apache.commons.lang3.ObjectUtils

class UserProfileResponse (
        val id: Long,
        val username: String,
        val name: String,
        val phoneNumber: String? = null,
        val avatarStyle: String,
        val avatarColor: String
) {
    private constructor(builder: Builder) : this(builder.id!!, builder.username!!, builder.name!!, builder.phoneNumber, builder.avatarStyle!!, builder.avatarColor!!)

    class Builder {
        var id: Long? = null
            private set
        var username: String? = null
            private set
        var name: String? = null
            private set
        var phoneNumber: String? = null
            private set
        var avatarStyle: String? = null
            private set
        var avatarColor: String? = null
            private set

        fun id(id: Long) = apply { this.id = id }
        fun username(username: String) = apply { this.username = username }
        fun name(name: String) = apply { this.name = name }
        fun phone(phoneNumber: String?) = apply { this.phoneNumber = phoneNumber }
        fun avatarStyle(style: String) = apply { this.avatarStyle = style }
        fun avatarColor(color: String) = apply { this.avatarColor = color }

        fun build() = if (ObjectUtils.allNotNull(id, username, name, avatarStyle, avatarColor))
            UserProfileResponse(this)
        else throw IllegalStateException("Properties must not be null.")
    }
}