package content.region.fremennik.handlers

import core.api.MapArea
import core.api.sendMessage
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.NPCs
import org.rs.consts.Scenery
import java.util.*

class Rellekka : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2602, 3639, 2739, 3741))
    }

    override fun interactBehavior(
        entity: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (entity is Player) {
            val player = entity.asPlayer()
            when (target.id) {
                Scenery.ANVIL_4306,
                Scenery.POTTER_S_WHEEL_4310,
                Scenery.SPINNING_WHEEL_4309,
                Scenery.FURNACE_4304,
                Scenery.POTTERY_OVEN_4308,
                -> {
                    sendMessage(player, "Only Fremenniks may use this ${target.name.lowercase(Locale.getDefault())}.")
                    return true
                }

                Scenery.TRAPDOOR_100 -> {
                    player.dialogueInterpreter.sendDialogue(
                        "You try to open the trapdoor but it won't budge! It looks like the",
                        "trapdoor can only be opened from the other side.",
                    )
                    return true
                }

                2435,
                NPCs.JARVALD_2436,
                NPCs.JARVALD_2437,
                NPCs.JARVALD_2438,
                -> {
                    if (option.name == "Travel") {
                        player.dialogueInterpreter.open(target.id, target, true)
                        return true
                    }
                }
            }
        }

        return false
    }
}
