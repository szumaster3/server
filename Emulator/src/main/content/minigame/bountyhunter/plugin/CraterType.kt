package content.minigame.bountyhunter.plugin

import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders

enum class CraterType(
    val level: Int,
    val roomLocation: Location,
    val craterLocation: Location,
    val exitLocation: Location,
    val zone: ZoneBorders,
) {
    LOW_LEVEL(
        level = 3,
        roomLocation = Location.create(1548, 5804, 0),
        craterLocation = Location.create(1548, 5804, 0),
        exitLocation = Location.create(3152, 3672, 0),
        zone = ZoneBorders(2688, 5632, 2879, 5823),
    ),
    MID_LEVEL(
        level = 50,
        roomLocation = Location.create(1558, 5785, 0),
        craterLocation = Location.create(1548, 5804, 0),
        exitLocation = Location.create(3158, 3680, 0),
        zone = ZoneBorders(2944, 5632, 3135, 5823),
    ),
    HIGH_LEVEL(
        level = 95,
        roomLocation = Location.create(1570, 5804, 0),
        craterLocation = Location.create(1548, 5804, 0),
        exitLocation = Location.create(3164, 3685, 0),
        zone = ZoneBorders(3200, 5632, 3391, 5823),
    ),
    ;

    fun canEnter(player: Player): Boolean {
        val combatLevel = player.properties.currentCombatLevel
        if (player.ironmanManager.checkRestriction()) {
            return false
        }
        if (ordinal < CraterType.values().size - 1) {
            if (combatLevel > CraterType.values()[ordinal + 1].level + 5) {
                sendMessage(
                    player,
                    "Your combat level has to be below " + (CraterType.values()[ordinal + 1].level + 5) +
                        " to enter this crater.",
                )
                return false
            }
        }
        if (combatLevel < level) {
            sendMessage(player, "You need a combat level of $level to enter this crater.")
            return false
        }
        return true
    }
}
