package content.global.ame.rickturpentine

import content.global.ame.RandomEventNPC
import core.api.addItemOrDrop
import core.api.getWorldTicks
import core.api.sendNPCDialogue
import core.api.utils.WeightBasedTable
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro
import org.rs.consts.NPCs

/**
 * Represents the Rick Turpentine NPC.
 * @author Zerken
 */
class RickTurpentineNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(NPCs.RICK_TURPENTINE_2476) {
    private var attackDelay = 0

    override fun init() {
        super.init()
        sendChat(
            "Good day to you, " + (if (player.isMale) "milord " else "milady ") +
                player.username.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } +
                ".",
        )
    }

    override fun tick() {
        if (ticksLeft <= 10) {
            ticksLeft = 10
            if (attackDelay <= getWorldTicks()) {
                this.attack(player)
            }
        }
        super.tick()
    }

    override fun talkTo(npc: NPC) {
        attackDelay = getWorldTicks() + 10
        this.pulseManager.clear()
        sendNPCDialogue(player, NPCs.RICK_TURPENTINE_2476,
            "Today is your lucky day, " + (if (player.isMale) "sirrah!" else "madam!") +
                    " I am donating to the victims of crime to atone for my past actions!", FaceAnim.NEUTRAL
        )
        AntiMacro.rollEventLoot(player).forEach { addItemOrDrop(player, it.id, it.amount) }
        AntiMacro.terminateEventNpc(player)
    }
}
