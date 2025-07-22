package content.region.kandarin.camelot.quest.grail.npc

import content.data.GameAttributes
import content.region.kandarin.camelot.quest.grail.dialogue.BlackKnightTitanDialogue
import core.api.openDialogue
import core.api.sendMessage
import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Handles the BlackKnightTitanNPC.
 *
 * # Relations
 * - [Holy Grail][content.region.kandarin.camelot.quest.grail.HolyGrail]
 */
@Initializable
class BlackKnightTitanNPC :
    MapZone("BlackKnightTitanZone", true),
    Plugin<Any?> {
    override fun newInstance(arg: Any?): BlackKnightTitanNPC {
        ZoneBuilder.configure(this)
        return this
    }

    override fun configure() {
        super.register(ZoneBorders(Location.create(2792, 4721, 0), Location.create(2790, 4723, 0)))
    }

    override fun fireEvent(
        identifier: String?,
        vararg args: Any?,
    ): Any = Unit

    override fun startDeath(
        e: Entity?,
        killer: Entity?,
    ): Boolean {
        if (e != null && e is NPC && e.asNpc().id == NPCs.BLACK_KNIGHT_TITAN_221) {
            if (killer != null && killer.isPlayer && killer.asPlayer().equipment.contains(Items.EXCALIBUR_35, 1)) {
                sendMessage(killer.asPlayer(), "Well done! You have defeated the Black Knight Titan!")
                return true
            }
        }

        setAttribute(killer!!.asPlayer(), GameAttributes.BLACK_KNIGHT_TITAN, true)
        openDialogue(killer.asPlayer(), BlackKnightTitanDialogue(true), NPCs.BLACK_KNIGHT_TITAN_221)
        return false
    }
}
