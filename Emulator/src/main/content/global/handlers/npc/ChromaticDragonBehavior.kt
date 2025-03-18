package content.global.handlers.npc

import core.api.asItem
import core.api.getStatLevel
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

class ChromaticDragonBehavior : NPCBehavior(*greenDragons, *blueDragons, *redDragons, *blackDragons) {
    override fun onDropTableRolled(
        self: NPC,
        killer: Entity,
        drops: ArrayList<Item>,
    ) {
        val removeList = hashSetOf<Int>()
        for (item in drops) {
            when (item) {
                Items.BLACK_DRAGON_EGG_12480.asItem(),
                Items.RED_DRAGON_EGG_12477.asItem(),
                Items.BLUE_DRAGON_EGG_12478.asItem(),
                Items.GREEN_DRAGON_EGG_12479.asItem(),
                -> removeList.add(item.id)
            }
        }

        drops.removeAll { item -> removeList.contains(item.id) }
        if (getStatLevel(killer, Skills.SUMMONING) >= 99 && RandomFunction.roll(EGG_RATE)) {
            drops.add(
                when (self.id) {
                    in greenDragons -> Items.GREEN_DRAGON_EGG_12479.asItem()
                    in blueDragons -> Items.BLUE_DRAGON_EGG_12478.asItem()
                    in redDragons -> Items.RED_DRAGON_EGG_12477.asItem()
                    in blackDragons -> Items.BLACK_DRAGON_EGG_12480.asItem()
                    else -> Items.DRAGON_BONES_536.asItem()
                },
            )
        }
    }

    companion object {
        // Green dragons.
        val greenDragons =
            intArrayOf(
                NPCs.GREEN_DRAGON_941,
                NPCs.GREEN_DRAGON_4677,
                NPCs.GREEN_DRAGON_4678,
                NPCs.GREEN_DRAGON_4679,
                NPCs.GREEN_DRAGON_4680,
            )

        // Blue dragons.
        val blueDragons =
            intArrayOf(
                NPCs.BLUE_DRAGON_55,
                NPCs.BLUE_DRAGON_4681,
                NPCs.BLUE_DRAGON_4682,
                NPCs.BLUE_DRAGON_4683,
                NPCs.BLUE_DRAGON_4684,
            )

        // Red dragons.
        val redDragons =
            intArrayOf(
                NPCs.RED_DRAGON_53,
                NPCs.RED_DRAGON_4669,
                NPCs.RED_DRAGON_4670,
                NPCs.RED_DRAGON_4671,
                NPCs.RED_DRAGON_4672,
            )

        // Black dragons.
        val blackDragons =
            intArrayOf(
                NPCs.BLACK_DRAGON_54,
                NPCs.BLACK_DRAGON_4673,
                NPCs.BLACK_DRAGON_4674,
                NPCs.BLACK_DRAGON_4675,
                NPCs.BLACK_DRAGON_4676,
            )

        // Egg drop rate.
        var EGG_RATE = 1000
    }
}
