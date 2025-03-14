package content.region.fremennik.handlers.neitiznot

import core.api.MapArea
import core.api.getRegionBorders
import core.api.sendMessage
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class Neitiznot : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(getRegionBorders(9275), ZoneBorders(2313, 3786, 2331, 3802))
    }

    override fun interactBehavior(
        entity: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (entity is Player) {
            val player = entity.asPlayer()
            when (target.id) {
                Scenery.BANK_CHEST_21301 -> {
                    player.bank.open()
                    return true
                }

                NPCs.THAKKRAD_SIGMUNDSON_5506 ->
                    if (option.name == "Craft-goods") {
                        player.dialogueInterpreter.open("thakkrad-yak")
                        return true
                    }
            }
        }
        return false
    }

    override fun useWithBehavior(
        player: Player?,
        used: Item?,
        with: Node?,
    ): Boolean {
        if (with is NPC && with.id == NPCs.YAK_5529) {
            sendMessage(player!!, "The cow doesn't want that.")
        }
        return true
    }
}
