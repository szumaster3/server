package content.global.ame.lostpirate

import content.global.ame.RandomEventNPC
import core.api.getWorldTicks
import core.api.openDialogue
import core.api.utils.WeightBasedTable
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro
import org.rs.consts.NPCs

class CapnHandNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.CAPN_HAND_2539) {
    private var attackDelay = 0
    private val forceChat = arrayOf("I got treasure here, ${player.username}!", "Heave to, missus ${player.name}!")

    private var timeLeft = 0

    override fun init() {
        super.init()
        sendChat(forceChat.random())
    }

    override fun talkTo(npc: NPC) {
        openDialogue(player, CapnHandDialogue(0), this.asNpc())
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
    }

    override fun tick() {
        if (!player.location.withinDistance(this.location, 8)) {
            this.terminate()
        }
        if (ticksLeft <= 10) {
            ticksLeft = 10
            if (timeLeft <= getWorldTicks()) {
                AntiMacro.terminateEventNpc(player)
            }
        }
        super.tick()
        if (!player.viewport.currentPlane.npcs
                .contains(this)
        ) {
            this.clear()
        }
    }
}
