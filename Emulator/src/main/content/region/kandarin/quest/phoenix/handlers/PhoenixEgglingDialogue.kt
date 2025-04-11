package content.region.kandarin.quest.phoenix.handlers

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class PhoenixEgglingDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.PHOENIX_EGGLING_8550)
        when(stage) {
            // TODO: Camera movement.
            0 -> npc(FaceAnim.NEW_HAPPY, "Cheep cheep-chirp chirp?").also { stage++ }
            1 -> player(FaceAnim.EXTREMELY_SHOCKED, "It's trying to climb into my backpack!").also { stage++ }
            2 -> player(FaceAnim.THINKING,"Hmmm. Should I take it with me?").also { stage++ }
            3 -> options("Hop in the bag, you!", "I have enough mouths to feed.").also { stage++ }
            4 -> when(buttonID) {
                1 -> {
                    lock(player!!, 1)
                    queueScript(player!!, 1, QueueStrength.SOFT) {
                        findLocalNPC(player!!, npc!!.id)!!.clear()
                        addItemOrBank(player!!, Items.PHOENIX_EGGLING_14626)
                        npcl(FaceAnim.NEW_HAPPY, "Cheeeeeeep! Chir, cheepy cheep chirp?")
                        sendMessage(player!!, "The phoenix eggling is now yours! It can be summoned from the pet interface.")
                        sendNews("${player!!.username} has found a Phoenix eggling!")
                        // setAttribute(player!!, GameAttributes.PHOENIX_LAIR_EGGLING_0, true)
                        return@queueScript stopExecuting(player!!)
                    }
                }
                2 -> npc(FaceAnim.NEW_HAPPY, "Chiiiirp...").also { stage = END_DIALOGUE }
            }
        }
    }
}