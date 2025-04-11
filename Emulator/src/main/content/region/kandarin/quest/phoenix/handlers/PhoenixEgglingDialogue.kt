package content.region.kandarin.quest.phoenix.handlers

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

class PhoenixEgglingDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.PHOENIX_EGGLING_8550)
        // Check if the player already owns either version of the pet.
        val hasCutePet = getAttribute(player!!, GameAttributes.PHOENIX_LAIR_EGGLING_CUTE, false)
        val hasMeanPet = getAttribute(player!!, GameAttributes.PHOENIX_LAIR_EGGLING_MEAN, false)
        // Roll a random pet version if the player doesn't have any.
        val rollPet = RandomFunction.random(1, 2)

        when (stage) {
            0 -> npc(FaceAnim.NEW_HAPPY, "Cheep cheep-chirp chirp?").also { stage++ }
            1 -> player(FaceAnim.EXTREMELY_SHOCKED, "It's trying to climb into my backpack!").also { stage++ }
            2 -> player(FaceAnim.THINKING, "Hmmm. Should I take it with me?").also { stage++ }
            3 -> options("Hop in the bag, you!", "I have enough mouths to feed.").also { stage++ }
            4 -> when (buttonID) {
                1 -> {
                    val item: Int
                    val attribute: String
                    val dialogue: String

                    if (hasCutePet && !hasMeanPet) {
                        item = Items.PHOENIX_EGGLING_14627
                        attribute = GameAttributes.PHOENIX_LAIR_EGGLING_MEAN
                        dialogue = "Bwark bwaa bwik bwark!"
                    } else if (!hasCutePet && hasMeanPet) {
                        item = Items.PHOENIX_EGGLING_14626
                        attribute = GameAttributes.PHOENIX_LAIR_EGGLING_CUTE
                        dialogue = "Cheeeeeeep! Chir, cheepy cheep chirp?"
                    } else {
                        if (rollPet == 1) {
                            item = Items.PHOENIX_EGGLING_14626
                            attribute = GameAttributes.PHOENIX_LAIR_EGGLING_CUTE
                            dialogue = "Cheeeeeeep! Chir, cheepy cheep chirp?"
                        } else {
                            item = Items.PHOENIX_EGGLING_14627
                            attribute = GameAttributes.PHOENIX_LAIR_EGGLING_MEAN
                            dialogue = "Bwark bwaa bwik bwark!"
                        }
                    }

                    lock(player!!, 2)
                    queueScript(player!!, 1, QueueStrength.SOFT) {
                        findLocalNPC(player!!, npc!!.id)?.clear()
                        addItemOrBank(player!!, item, 1)
                        setAttribute(player!!, attribute, true)
                        npcl(FaceAnim.NEW_HAPPY, dialogue)
                        sendMessage(
                            player!!, "The phoenix eggling is now yours!"
                        )
                        sendNews("${player!!.username} has found a Phoenix eggling!")
                        stage = END_DIALOGUE
                        return@queueScript stopExecuting(player!!)
                    }
                }

                2 -> npc(FaceAnim.NEW_HAPPY, "Chiiiirp...").also { stage = END_DIALOGUE }
            }
        }
    }
}
