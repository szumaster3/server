package content.region.kandarin.piscatoris.quest.phoenix.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import shared.consts.Items
import shared.consts.NPCs

class PhoenixEgglingDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.PHOENIX_EGGLING_8550)

        //TODO 5766
        val hasCutePet = getAttribute(player!!, GameAttributes.PHOENIX_LAIR_EGGLING_CUTE, false)
        val hasMeanPet = getAttribute(player!!, GameAttributes.PHOENIX_LAIR_EGGLING_MEAN, false)

        when (stage) {
            0 -> npc(FaceAnim.FAMILIAR_HAPPY, "Cheep cheep-chirp chirp?").also { stage++ }
            1 -> player(FaceAnim.EXTREMELY_SHOCKED, "It's trying to climb into my backpack!").also { stage++ }
            2 -> player(FaceAnim.THINKING, "Hmmm. Should I take it with me?").also { stage++ }
            3 -> options("Hop in the bag, you!", "I have enough mouths to feed.").also { stage++ }
            4 -> when (buttonID) {
                1 -> {
                    fun randomRoll(): Pair<Int, String> {
                        return if (RandomFunction.random(1, 2) == 1) {
                            Items.PHOENIX_EGGLING_14626 to GameAttributes.PHOENIX_LAIR_EGGLING_CUTE
                        } else {
                            Items.PHOENIX_EGGLING_14627 to GameAttributes.PHOENIX_LAIR_EGGLING_MEAN
                        }
                    }

                    val (item, attribute, dialogue) = when {
                        hasCutePet && !hasMeanPet -> Triple(
                            Items.PHOENIX_EGGLING_14627,
                            GameAttributes.PHOENIX_LAIR_EGGLING_MEAN,
                            "Bwark bwaa bwik bwark!"
                        )
                        !hasCutePet && hasMeanPet -> Triple(
                            Items.PHOENIX_EGGLING_14626,
                            GameAttributes.PHOENIX_LAIR_EGGLING_CUTE,
                            "Cheeeeeeep! Chir, cheepy cheep chirp?"
                        )
                        else -> {
                            val (randomItem, randomAttr) = randomRoll()
                            val randomDialogue = if (randomAttr == GameAttributes.PHOENIX_LAIR_EGGLING_CUTE)
                                "Cheeeeeeep! Chir, cheepy cheep chirp?"
                            else
                                "Bwark bwaa bwik bwark!"
                            Triple(randomItem, randomAttr, randomDialogue)
                        }
                    }

                    lock(player!!, 2)
                    queueScript(player!!, 1, QueueStrength.SOFT) {
                        findLocalNPC(player!!, npc!!.id)?.clear()
                        addItemOrBank(player!!, item, 1)
                        setAttribute(player!!, attribute, true)
                        npcl(FaceAnim.FAMILIAR_HAPPY, dialogue)
                        sendMessage(player!!, "The phoenix eggling is now yours!")
                        sendNews("${player!!.username} has found a Phoenix eggling!")
                        stage = END_DIALOGUE
                        stopExecuting(player!!)
                    }
                }

                2 -> npc(FaceAnim.FAMILIAR_HAPPY, "Chiiiirp...").also { stage = END_DIALOGUE }
            }
        }
    }
}