package content.global.random.event.prison_pete

import content.global.random.RandomEventNPC
import core.api.lock
import core.api.lockInteractions
import core.api.sendMessage
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import shared.consts.NPCs
/**
 * Represents the Prison Pete random event NPC.
 * @author szu
 */
class PrisonPeteNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.EVIL_BOB_2478) {
    override fun init() {
        super.init()
        lock(player, 6)
        lockInteractions(player, 6)
        PrisonPeteUtils.teleport(player)
        sendMessage(player, "Welcome to ScapeRune.")
    }

    override fun talkTo(npc: NPC) {
        // Empty
    }
}
