package content.region.misthalin.quest.rag.handlers.npc

import core.api.quest.isQuestInProgress
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class BearNPC : NPCBehavior(*bearIds) {
    companion object {
        private val bearIds =
            intArrayOf(
                NPCs.GRIZZLY_BEAR_105,
                NPCs.BLACK_BEAR_106,
                NPCs.GRIZZLY_BEAR_1195,
                NPCs.GRIZZLY_BEAR_CUB_1196,
                NPCs.GRIZZLY_BEAR_CUB_1197,
                NPCs.BEAR_CUB_1326,
                NPCs.BEAR_CUB_1327,
            )
    }

    override fun onDropTableRolled(
        self: NPC,
        killer: Entity,
        drops: ArrayList<Item>,
    ) {
        super.onDropTableRolled(self, killer, drops)

        if (killer is Player && isQuestInProgress(killer, Quests.RAG_AND_BONE_MAN, 1, 99)) {
            if (RandomFunction.roll(4)) {
                drops.add(Item(Items.BEAR_RIBS_7815))
            }
        }
    }
}
