package content.region.morytania.port_phasmatys.dialogue

import content.region.morytania.port_phasmatys.plugin.EnergyBarrier
import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GhostGuardDialogue(player: Player? = null, ) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val isGhostsAhoyComplete = isQuestComplete(player, Quests.GHOSTS_AHOY)
        val hasGhostspeakAmulet = inEquipment(player, Items.GHOSTSPEAK_AMULET_552)
        val hasBedsheet = inEquipment(player, Items.BEDSHEET_4285)

        when {
            isGhostsAhoyComplete && hasGhostspeakAmulet && hasBedsheet -> {
                sendDialogue(player, "You can't pass the barriers of Port Phasmatys while wearing the bedsheet.")
                return true
            }
            isGhostsAhoyComplete && hasGhostspeakAmulet -> {
                findLocalNPC(player, NPCs.GHOST_GUARD_1706)?.let { face(player, it, 1) }
                npc(FaceAnim.FRIENDLY, "All visitors to Port Phasmatys must pay a toll charge of 2 Ectotokens. However, you have done the ghosts of our town a service that surpasses all value, so you may pass without charge.")
                stage = 3
                return true
            }
            !hasGhostspeakAmulet -> {
                sendDialogue(player, "The ghostly guards wail at you incomprehensibly, and though you cannot understand their exact words, you understand their meaning - you may not pass the barriers of Port Phasmatys.")
                stage = END_DIALOGUE
                return true
            }
            else -> {
                npc(FaceAnim.FRIENDLY, "All visitors to Port Phasmatys must pay a toll charge of", "2 Ectotokens.")
                stage = 1
                return true
            }
        }
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            1 -> if (amountInInventory(player, Items.ECTO_TOKEN_4278) >= 2) {
                options(
                    "I would like to enter Port Phasmatys - here's 2 Ectotokens.",
                    "I'm not paying you Ectotokens just to go through a gate.",
                    "Where can I get Ectotokens?",
                ).also { stage++ }
            } else {
                options("I don't have that many Ectotokens.", "Where can I get Ectotokens?").also { stage = 6 }
            }

            2 -> when (buttonId) {
                1 -> player("I would like to enter Port Phasmatys - here's", "2 Ectotokens.").also { stage = 3 }
                2 -> player("I'm not paying you Ectotockens just to go through a gate.").also { stage = 4 }
                3 -> player("Where can I get Ectotokens?").also { stage = 5 }
            }
            3 -> {
                end()
                EnergyBarrier.passGateByTalk(player, npc)
            }
            4 -> npc("Sorry - it's Town Policy.").also { stage = END_DIALOGUE }
            5 -> npc("You need to go to the Temple and earn some.", "Talk to the disciples - they will tell you how.").also { stage = END_DIALOGUE }
            6 -> when (buttonId) {
                1 -> player("I don't have that many Ectotokens.").also { stage = 4 }
                2 -> player("Where can I get Ectotokens?").also { stage = 5 }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GHOST_GUARD_1706)
}
