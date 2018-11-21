package kz.peep.api.infrastructure.structs

enum class ClothingType(val points: Int) {
    HEADGEAR(3),
    FOOTWEAR(5),
    OUTERWEAR(10),
    TOPWEAR(5),
    UNDERWEAR(2)
}