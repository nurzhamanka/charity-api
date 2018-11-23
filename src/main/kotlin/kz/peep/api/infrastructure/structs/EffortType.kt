package kz.peep.api.infrastructure.structs

import kotlin.reflect.KClass

enum class EffortType {
    MONEY,
    CLOTHES,
    FOOD,
    BLOOD,
    SOUL
}

inline fun <reified T : Enum<T>> KClass<T>.enumValueOfOrNull(name: String): T? {
    return enumValues<T>().find { it.name == name }
}