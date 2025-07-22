package content.global.skill.construction

import core.api.getStatLevel
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

enum class HousingStyle(val levelRequirement: Int, val cost: Int, val regionId: Int, val plane: Int, val doorId: Int, val secondDoorId: Int, val wallId: Int, val window: Decoration) {
    BASIC_WOOD(1, 5000, 7503, 0, 13100, 13101, 13098, Decoration.BASIC_WOOD_WINDOW),
    BASIC_STONE(10, 5000, 7503, 1, 13094, 13096, 1902, Decoration.BASIC_STONE_WINDOW),
    WHITEWASHED_STONE(20, 7500, 7503, 2, 13006, 13007, 1415, Decoration.WHITEWASHED_STONE_WINDOW),
    FREMENNIK_STYLE_WOOD(30, 10000, 7503, 3, 13109, 13107, 13111, Decoration.FREMENNIK_WINDOW),
    TROPICAL_WOOD(40, 15000, 7759, 0, 13016, 13015, 13011, Decoration.TROPICAL_WOOD_WINDOW),
    FANCY_STONE(50, 25000, 7759, 1, 13119, 13118, 13116, Decoration.FANCY_STONE_WINDOW),
    ;

    fun hasLevel(player: Player): Boolean = getStatLevel(player, Skills.CONSTRUCTION) >= levelRequirement
}
