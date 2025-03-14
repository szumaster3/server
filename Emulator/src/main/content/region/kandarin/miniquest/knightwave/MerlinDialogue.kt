package content.region.kandarin.miniquest.knightwave

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import org.rs.consts.NPCs

class MerlinDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.MERLIN_213)
        when (stage) {
            0 ->
                if (!getAttribute(player!!, KnightWaveAttributes.KW_COMPLETE, false)) {
                    npc(
                        FaceAnim.HAPPY,
                        "Well done, young adventurer. You truly are a worthy",
                        "knight.",
                    ).also { stage++ }
                } else {
                    sendMessage(player!!, "Nothing interesting happens.")
                    end()
                    player!!.unlock()
                }

            1 -> {
                end()
                player!!.unlock()
                sendDialogueLines(
                    player!!,
                    "You receive access to the prayers Chivalry and Piety and 20,000",
                    "XP in your Hitpoints, Strength, Attack, and Defence skills. You may",
                    "also speak to Merlin to set your respawn point to Camelot.",
                ).also {
                    rewardXP(player!!, Skills.HITPOINTS, 20.000)
                    rewardXP(player!!, Skills.ATTACK, 20.000)
                    rewardXP(player!!, Skills.STRENGTH, 20.000)
                    rewardXP(player!!, Skills.DEFENCE, 20.000)
                    setAttribute(player!!, KnightWaveAttributes.KW_COMPLETE, true)
                }
            }
        }
    }
}
