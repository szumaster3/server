package content.global.skill.construction

import core.api.getStatLevel
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

enum class HousingStyle(
    val levelRequirement: Int,
    val cost: Int,
    val regionId: Int,
    val plane: Int,
    val doorId: Int,
    val secondDoorId: Int,
    val wallId: Int,
    val window: Decoration,
) {
    BASIC_WOOD(
        levelRequirement = 1,
        cost = 5000,
        regionId = 7503,
        plane = 0,
        doorId = 13100,
        secondDoorId = 13101,
        wallId = 13098,
        window = Decoration.BASIC_WOOD_WINDOW,
    ),
    BASIC_STONE(
        levelRequirement = 10,
        cost = 5000,
        regionId = 7503,
        plane = 1,
        doorId = 13094,
        secondDoorId = 13096,
        wallId = 1902,
        window = Decoration.BASIC_STONE_WINDOW,
    ),
    WHITEWASHED_STONE(
        levelRequirement = 20,
        cost = 7500,
        regionId = 7503,
        plane = 2,
        doorId = 13006,
        secondDoorId = 13007,
        wallId = 1415,
        window = Decoration.WHITEWASHED_STONE_WINDOW,
    ),
    FREMENNIK_STYLE_WOOD(
        levelRequirement = 30,
        cost = 10000,
        regionId = 7503,
        plane = 3,
        doorId = 13109,
        secondDoorId = 13107,
        wallId = 13111,
        window = Decoration.FREMENNIK_WINDOW,
    ),
    TROPICAL_WOOD(
        levelRequirement = 40,
        cost = 15000,
        regionId = 7759,
        plane = 0,
        doorId = 13016,
        secondDoorId = 13015,
        wallId = 13011,
        window = Decoration.TROPICAL_WOOD_WINDOW,
    ),
    FANCY_STONE(
        levelRequirement = 50,
        cost = 25000,
        regionId = 7759,
        plane = 1,
        doorId = 13119,
        secondDoorId = 13118,
        wallId = 13116,
        window = Decoration.FANCY_STONE_WINDOW,
    ),
    ;

    fun hasLevel(player: Player): Boolean = getStatLevel(player, Skills.CONSTRUCTION) >= levelRequirement
}
