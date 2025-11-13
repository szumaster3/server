package content.region.kandarin.gnome_stronghold.quest.itgronigen.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import shared.consts.NPCs

class GoblinsNPC : NPCBehavior(NPCs.GREASYCHEEKS_6127, NPCs.SMELLYTOES_6128, NPCs.CREAKYKNEES_6129) {

    companion object {
        private val overheadDialogues = mapOf(
            NPCs.GREASYCHEEKS_6127 to arrayOf(
                "This is gonna taste sooo good", "Cook, cook, cook!", "I'm so hungry!"
            ),
            NPCs.SMELLYTOES_6128 to arrayOf(
                "La la la. Do di dum dii!", "Doh ray meeee laa doh faaa!"
            ),
            NPCs.CREAKYKNEES_6129 to arrayOf(
                "Come on! Please light!", "Was that a spark?", "I'm so hungry!"
            )
        )
    }

    override fun onCreation(self: NPC) {
        self.isNeverWalks = false
        self.isWalks = false
    }

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.roll(25)) {
            overheadDialogues[self.id]?.let { dialogues ->
                sendChat(self, dialogues.random())
            }
        }
        return true
    }
}

