package content.region.kandarin.quest.ikov.handlers

import core.api.quest.isQuestComplete
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class GuardianOfArmadylBehavior : NPCBehavior(*guardianOfArmadylIds) {
    companion object {
        private val guardianOfArmadylIds =
            intArrayOf(
                NPCs.GUARDIAN_OF_ARMADYL_274,
                NPCs.GUARDIAN_OF_ARMADYL_275,
            )
    }

    override fun onDropTableRolled(
        self: NPC,
        killer: Entity,
        drops: ArrayList<Item>,
    ) {
        super.onDropTableRolled(self, killer, drops)

        if (killer is Player && isQuestComplete(killer, Quests.TEMPLE_OF_IKOV)) {
            if (RandomFunction.roll(4)) {
                drops.add(Item(Items.ARMADYL_PENDANT_87))
            }
        }
    }
}
