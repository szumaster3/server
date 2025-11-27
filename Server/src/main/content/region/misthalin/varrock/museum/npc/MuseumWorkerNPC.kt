package content.region.misthalin.varrock.museum.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import shared.consts.NPCs

private val archelogistsNPCs = intArrayOf(NPCs.BARNABUS_HURMA_5932, NPCs.THIAS_LEACKE_5935, NPCs.MARIUS_GISTE_5933, NPCs.TINSE_TORPE_5937, NPCs.CADEN_AZRO_5934, NPCs.SINCO_DOAR_5936)
/**
 * Handles the Museum Worker NPC.
 */
class MuseumWorkerNPC : NPCBehavior(*archelogistsNPCs) {
    private var delay = 0
    private val forceChat =
        arrayOf(
            "How's it going, officers?",
            "Another lot for ya!",
            "Off we go again!",
            "Alright, thanks!",
        )

    override fun onCreation(self: NPC) {
        self.isWalks = false
    }

    override fun tick(self: NPC): Boolean {
        delay++
        if (delay >= 10) {
            delay = 0
            if (RandomFunction.random(100) < 15) {
                sendChat(self, forceChat.random())
            }
        }
        return true
    }
}
