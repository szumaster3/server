package content.region.morytania.quest.ahoy.dialogue

import content.region.morytania.quest.ahoy.handlers.GhostsAhoyUtils
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class GravingasDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val score = 0
        val questStage = getQuestStage(player!!, Quests.GHOSTS_AHOY)
        npc = NPC(NPCs.GRAVINGAS_1685)
        when (questStage) {
            1 ->
                when (stage) {
                    0 ->
                        npc(
                            "Will you join with me and protect against the evil ban",
                            "of Nercrovarus and his disciples?",
                        ).also { stage++ }
                    1 ->
                        options(
                            "After hearing Velorina's story I will be happy to help out.",
                            "I'm sorry, I don't really think I should get involved.",
                        ).also {
                            stage++
                        }
                    2 ->
                        when (buttonID) {
                            1 ->
                                player("After hearing Velorina's story I will be happy to help", "out.").also {
                                    stage =
                                        4
                                }
                            2 -> player("I'm sorry, I don't really think I should get involved.").also { stage = 3 }
                        }
                    3 -> npc("Ah, the youth of today - so apathetic to politics.").also { stage = END_DIALOGUE }
                    4 ->
                        npc(
                            "Excellent, excellent!! Here - take this petition form, and",
                            "try and get 10 signatures from the townsfolk.",
                        ).also {
                            end()
                            addItemOrDrop(player!!, Items.PETITION_FORM_4283, 1)
                            setAttribute(player!!, GhostsAhoyUtils.petitionstart, true)
                            setQuestStage(player!!, "Ghost Ahoy", 10)
                        }
                }

            in 2..10 ->
                when (stage) {
                    0 ->
                        if (!inInventory(player!!, Items.PETITION_FORM_4283) &&
                            getAttribute(player!!, GhostsAhoyUtils.petitionstart, false)
                        ) {
                            npc(
                                "Blown away in the sea breeze, hey?",
                                "Oh well, can't be helped. Here's another one, but",
                                "you'll have to start from scratch again.",
                            ).also {
                                addItemOrDrop(player!!, Items.PETITION_FORM_4283, 1)
                                setAttribute(player!!, GhostsAhoyUtils.petitionsigns, 0)
                                stage = END_DIALOGUE
                            }
                        } else if (getAttribute(player!!, GhostsAhoyUtils.petitionsigns, 0) == 0) {
                            npc("Come on - you haven't even started yet! You need 10", "more signatures.").also {
                                stage = END_DIALOGUE
                            }
                        } else if (getAttribute(player!!, GhostsAhoyUtils.petitionsigns, 0) in 4..9) {
                            npc(
                                "Not doing too badly I see! You need ${getAttribute(
                                    player!!,
                                    GhostsAhoyUtils.petitionsigns,
                                    score.toString(),
                                )} more signature's.",
                            ).also {
                                stage++
                            }
                        } else if (getAttribute(player!!, GhostsAhoyUtils.petitionsigns, 0) == 10) {
                            npc("You've got them all! Now go and present it to", "Necrovarus!!").also {
                                removeAttribute(player!!, GhostsAhoyUtils.petitionsigns)
                                stage = END_DIALOGUE
                            }
                        }
                    1 ->
                        options(
                            "After hearing Velorina's story I will be happy to help out.",
                            "I'm sorry, I don't really think I should get involved.",
                        ).also {
                            stage++
                        }
                    2 ->
                        when (buttonID) {
                            1 ->
                                player("After hearing Velorina's story I will be happy to help", "out.").also {
                                    stage =
                                        4
                                }
                            2 -> player("I'm sorry, I don't really think I should get involved.").also { stage = 3 }
                        }
                    3 -> npc("Ah, the youth of today - so apathetic to politics.").also { stage = END_DIALOGUE }
                    4 ->
                        npc(
                            "Excellent, excellent!! Here - take this petition form, and",
                            "try and get 10 signatures from the townsfolk.",
                        ).also {
                            end()
                            addItemOrDrop(player!!, Items.PETITION_FORM_4283, 1)
                            setAttribute(player!!, GhostsAhoyUtils.petitionstart, true)
                        }
                }
        }
    }
}
