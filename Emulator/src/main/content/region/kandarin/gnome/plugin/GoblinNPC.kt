package content.region.kandarin.gnome.plugin

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class GoblinNPC : NPCBehavior(NPCs.GREASYCHEEKS_6127, NPCs.SMELLYTOES_6128, NPCs.CREAKYKNEES_6129) {

    companion object {
        private val greasyOverheadDialogues = arrayOf(
            "This is gonna taste sooo good",
            "Cook, cook, cook!",
            "I'm so hungry!"
        )
        private val smellyOverheadDialogues = arrayOf(
            "La la la. Do di dum dii!",
            "Doh ray meeee laa doh faaa!"
        )
        private val creakyOverheadDialogues = arrayOf(
            "Come on! Please light!",
            "Was that a spark?",
            "I'm so hungry!"
        )
    }

    private var nextActionTime = 0L

    override fun onCreation(self: NPC) {
        self.isNeverWalks = true
        self.isWalks = false
    }

    override fun tick(self: NPC): Boolean {
        val now = System.currentTimeMillis()
        if (now < nextActionTime || !self.isPlayerNearby(15)) {
            return true
        }

        if (RandomFunction.roll(25)) {
            val message = when (self.id) {
                NPCs.GREASYCHEEKS_6127 -> greasyOverheadDialogues.random()
                NPCs.SMELLYTOES_6128 -> smellyOverheadDialogues.random()
                NPCs.CREAKYKNEES_6129 -> creakyOverheadDialogues.random()
                else -> null
            }
            message?.let { sendChat(self, it) }
            nextActionTime = now + 10000
        }

        return true
    }
}
