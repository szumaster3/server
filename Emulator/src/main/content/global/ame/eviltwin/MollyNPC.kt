package content.global.ame.eviltwin

import content.global.ame.RandomEventNPC
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.game.system.timer.impl.AntiMacro

private val MOLLY: Int = 3892 + (0 and 0xFF) + (((0 shr 16) and 0xFF) * EvilTwinColors.values().size)

class MollyNPC(
    override var loot: WeightBasedTable? = null,
) : RandomEventNPC(MOLLY) {
    override fun init() {
        super.init()
        EvilTwinUtils.start(player)
        AntiMacro.terminateEventNpc(player)
    }

    override fun talkTo(npc: NPC) {
    }
}
