package content.region.morytania.port_phasmatys.dialogue

import content.region.morytania.port_phasmatys.quest.ahoy.dialogue.NecrovarusDialogueFile
import core.api.inEquipment
import core.api.openDialogue
import core.api.quest.hasRequirement
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class NecrovarusDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            core.api.sendDialogue(player, "You cannot understand the ghost.").also { stage = END_DIALOGUE }
            return true
        }
        if (hasRequirement(player, Quests.GHOSTS_AHOY) && !isQuestComplete(player, Quests.GHOSTS_AHOY)) {
            end()
            openDialogue(player, NecrovarusDialogueFile())
            return true
        }
        if (isQuestComplete(player, Quests.GHOSTS_AHOY)) {
            player(
                "Told you I'd defeat you, Necrovarus. My advice to",
                "you is to pass over to the next world yourself with",
                "everybody else.",
            )
            stage = 4
        } else {
            options("What is this place?", "What happened to everyone here?", "How do I get into the town?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 -> player("What is this place?").also { stage = 1 }
                    2 -> player("What happened to everyone here?").also { stage = 2 }
                    3 -> player("How do I get into the town?").also { stage = 3 }
                }

            1 ->
                npc(FaceAnim.ANNOYED, "Speak to me again and I will rend the soul from your", "flesh.").also {
                    stage = END_DIALOGUE
                }

            2 ->
                npc(FaceAnim.ANNOYED, "You dare to speak to me??? Have you lost your", "wits????").also {
                    stage = END_DIALOGUE
                }

            3 -> npc(FaceAnim.ANNOYED, "I do not answer questions, mortal fool!").also { stage = END_DIALOGUE }
            4 -> npc("I should fry you for what you have done...").also { stage++ }
            5 ->
                player(
                    "Quiet, evil priest!! If you try anything I will command you",
                    "again, but this time it will be to throw yourself into the",
                    "Endless Void for the rest of eternity.",
                ).also { stage++ }

            6 -> npc(FaceAnim.SCARED, "Please no! I will do whatever you say!!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.NECROVARUS_1684)
}
