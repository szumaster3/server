package content.global.skill.hunter.falconry

import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs

enum class FalconCatch(
    val npc: Int,
    val level: Int,
    val experience: Double,
    val item: Item,
) {
    SPOTTED_KEBBIT(NPCs.SPOTTED_KEBBIT_5098, 43, 104.0, Item(Items.SPOTTED_KEBBIT_FUR_10125)),
    DARK_KEBBIT(NPCs.DARK_KEBBIT_5099, 57, 132.0, Item(Items.DARK_KEBBIT_FUR_10115)),
    DASHING_KEBBIT(NPCs.DASHING_KEBBIT_5100, 69, 156.0, Item(Items.DASHING_KEBBIT_FUR_10127)),
    ;

    companion object {
        @JvmStatic
        fun forItem(item: Item): FalconCatch? {
            for (falconCatch in values()) {
                if (item.id == falconCatch.item.id) {
                    return falconCatch
                }
            }
            return null
        }

        @JvmStatic
        fun forNPC(npc: NPC): FalconCatch? {
            for (falconCatch in values()) {
                if (npc.id == falconCatch.npc) {
                    return falconCatch
                }
            }
            return null
        }
    }
}
