package content.global.skill.hunter

import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.game.node.item.Item

open class TrapNode(
    val npcIds: IntArray,
    @JvmField val level: Int,
    val experience: Double,
    val objectIds: IntArray,
    @JvmField val rewards: Array<Item>,
) {
    open fun canCatch(
        wrapper: TrapWrapper,
        npc: NPC,
    ): Boolean {
        val player = wrapper.player

        if (wrapper.isCaught || wrapper.isBusy || wrapper.isFailed) {
            return false
        }

        return player.skills.getStaticLevel(Skills.HUNTER) >= level && !npc.isInvisible
    }

    val transformId: Int
        get() = objectIds[0]

    val finalId: Int
        get() = objectIds[1]
}
