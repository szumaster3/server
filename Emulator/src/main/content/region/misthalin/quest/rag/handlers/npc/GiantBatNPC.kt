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

class GiantBatNPC : NPCBehavior(*giantBatIds) {
    companion object {
        private val giantBatIds =
            intArrayOf(
                NPCs.GIANT_BAT_78,
                NPCs.GIANT_BAT_1005,
                NPCs.GIANT_BAT_2482,
                NPCs.GIANT_BAT_2483,
                NPCs.GIANT_BAT_2484,
                NPCs.GIANT_BAT_2485,
                NPCs.GIANT_BAT_2486,
                NPCs.GIANT_BAT_2487,
                NPCs.GIANT_BAT_2488,
                NPCs.GIANT_BAT_3711,
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
                drops.add(Item(Items.GIANT_BAT_WING_7827))
            }
        }
    }
}
