package content.region.fremennik.jatizso.dialogue

import content.region.fremennik.plugin.FremennikBoat
import content.region.fremennik.plugin.Travel
import core.api.requireQuest
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Mord Gunnars dialogue.
 */
@Initializable
class MordGunnarsDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (npc.id == NPCs.MORD_GUNNARS_5481) {
            npcl(FaceAnim.FRIENDLY, "Would you like to sail to Jatizso?")
        } else {
            npcl(FaceAnim.FRIENDLY, "Would you like to sail back to Rellekka?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes, please.", "No, thanks.").also { stage++ }
            1 -> when (buttonId) {
                1 -> playerl(FaceAnim.FRIENDLY, "Yes, please!").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "No, thank you.").also { stage = END_DIALOGUE }
            }

            2 -> {
                end()
                if (!requireQuest(player, Quests.THE_FREMENNIK_TRIALS, "")) {
                    return true
                } else {
                    FremennikBoat.sail(
                        player,
                        if (npc.id == NPCs.MORD_GUNNARS_5481) {
                            Travel.RELLEKKA_TO_JATIZSO
                        } else {
                            Travel.JATIZSO_TO_RELLEKKA
                        },
                    )
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = MordGunnarsDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.MORD_GUNNARS_5482, NPCs.MORD_GUNNARS_5481)
}
