package content.global.ame.securityguard

import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro
import org.rs.consts.NPCs

class SecurityGuardNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.SECURITY_GUARD_4375) {
    private var timeLeft = 0

    override fun init() {
        super.init()
        sendChat("Stop right there " + player.username + "!")
    }

    override fun talkTo(npc: NPC) {
        openDialogue(player, SecurityGuardDialogueFile(), this.asNpc())
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
    }

    override fun tick() {
        if (!withinDistance(player, this.location, 8)) {
            this.terminate()
        }
        if (ticksLeft <= 10) {
            ticksLeft = 10
            if (timeLeft <= getWorldTicks()) {
                if (inBorders(player, getRegionBorders(7505))) {
                    sendChat(if (player.isMale) "He" else "She" + " got away!")
                } else {
                    sendChat("Well, I best be off!")
                }
            }
            AntiMacro.terminateEventNpc(player)
        }
        super.tick()
        if (!player.viewport.currentPlane.npcs
                .contains(this)
        ) {
            this.clear()
        }
    }
}
