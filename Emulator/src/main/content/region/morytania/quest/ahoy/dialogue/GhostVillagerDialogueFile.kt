package content.region.morytania.quest.ahoy.dialogue

import content.region.morytania.quest.ahoy.handlers.GhostsAhoyUtils.collectSignature
import core.api.inEquipment
import core.api.openDialogue
import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class GhostVillagerDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.GHOST_VILLAGER_1697)
        when (stage) {
            0 -> player("Would you sign this petition form, please?").also { stage++ }
            1 -> {
                if (!inEquipment(player!!, Items.BEDSHEET_4285)) {
                    npc(
                        "I'm sorry, but it's hard to believe that a mortal",
                        "could be interested in helping us.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                } else if (inEquipment(player!!, Items.BEDSHEET_4284)) {
                    npc(
                        "Why are you wearing that bedsheet? If you're",
                        "trying to pretend to be one of us, you're not fooling",
                        "anybody - you're not even green!",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    when ((0..6).random()) {
                        0 -> npc("Most certainly, I will.").also { stage = 10 }
                        1 -> npc("I'll do anything that annoys Necrovarus.").also { stage = 10 }
                        2 -> npc("Yes! It's about time somebody did something about", "Necrovarus.").also { stage = 10 }
                        3 -> npc("Yes, of course.").also { stage = 10 }
                        4 -> npc("I will if you make it worth my while...").also { stage = 11 }
                        5 -> npc("You scratch my back and I'll scratch yours...").also { stage = 11 }
                        6 -> npc("It'll cost you...").also { stage = 11 }
                    }
                }
            }
            10 -> {
                end()
                collectSignature(player!!)
            }
            11 -> {
                end()
                openDialogue(player!!, PaidVillagerDialogueFile())
            }
        }
    }
}
