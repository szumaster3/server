package content.global.ame.prisonpete

import content.global.ame.RandomEventNPC
import core.api.lock
import core.api.lockInteractions
import core.api.sendMessage
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs

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

    override fun talkTo(npc: NPC) {}
}
