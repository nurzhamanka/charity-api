package kz.peep.api.entities

import javax.persistence.Embeddable

@Embeddable
class UserAvatar (
        var style: String,
        var color: String
) {
    companion object {

        fun generateAvatar(): UserAvatar {
            val randStyle = arrayListOf("SQUARE", "ISOGRID", "SPACEINV", "HEXAGRID", "HEXAROT").shuffled()[0]
            val randColor = arrayListOf("PINK", "ORANGE", "GREEN", "TEAL", "YELLOW", "BLUE", "PURPLE", "RED", "BLACK").shuffled()[0]
            return UserAvatar(randStyle, randColor)
        }
    }
}