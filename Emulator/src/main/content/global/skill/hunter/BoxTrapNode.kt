package content.global.skill.hunter

import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.game.node.item.Item

open class BoxTrapNode(
    npcIds: IntArray,
    level: Int,
    experience: Double,
    rewards: Array<Item>,
    private val summoningLevel: Int,
) : TrapNode(npcIds, level, experience, intArrayOf(19188, 19189), rewards) {
    override fun canCatch(
        wrapper: TrapWrapper,
        npc: NPC,
    ): Boolean {
        if (wrapper.player.getSkills().getStaticLevel(Skills.SUMMONING) < summoningLevel) {
            return false
        }
        return super.canCatch(wrapper, npc)
    }
}
