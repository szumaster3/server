package content.region.kandarin.ardougne.east.quest.biohazard.dialogue

import content.region.kandarin.quest.biohazard.handlers.BiohazardUtils
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.Items
import org.rs.consts.NPCs

class ChancyDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.CHANCY_338)
        when (stage) {
            0 -> player("Hello, I've got a vial for you to take to Varrock.").also { stage++ }
            1 -> npc(FaceAnim.SCARED, "Tssch... that chemist asks for a lot for the wages he", "pays.").also { stage++ }
            2 -> player("Maybe you should ask him for more money.").also { stage++ }
            3 -> npc("Nah... I just use my initiative here and there.").also { stage++ }
            4 ->
                options(
                    "You give him the vial of ethenea...",
                    "You give him the vial of liquid honey...",
                    "You give him the vial of sulphuric broline...",
                ).also { stage++ }

            5 ->
                when (buttonID) {
                    1 -> {
                        if (!removeItem(player!!, Items.ETHENEA_415)) {
                            end()
                            sendMessage(player!!, "You can't give him what you don't have.")
                        } else {
                            sendMessage(player!!, "You give him the vial of ethenea.")
                            stage = 8
                        }
                    }

                    2 -> {
                        if (!removeItem(player!!, Items.LIQUID_HONEY_416)) {
                            end()
                            sendMessage(player!!, "You can't give him what you don't have.")
                        } else {
                            sendMessage(player!!, "You give him the vial of liquid honey.")
                            player("Right. I'll see you later in the Dancing Donkey Inn.")
                            stage++
                        }
                    }

                    3 -> {
                        if (!removeItem(player!!, Items.SULPHURIC_BROLINE_417)) {
                            end()
                            sendMessage(player!!, "You can't give him what you don't have.")
                        } else {
                            sendMessage(player!!, "You give him the vial of sulphuric broline.")
                            stage = 8
                        }
                    }
                }

            6 -> npc("Be Lucky!").also { stage++ }
            7 -> {
                end()
                setAttribute(player!!, BiohazardUtils.FIRST_VIAL_CORRECT, true)
            }

            8 -> {
                end()
            }
        }
    }
}

class ChancyVarrockDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.CHANCY_339)
        when (stage) {
            0 -> player("Hi, thanks for doing that.").also { stage++ }
            1 -> npc("No problem.").also { stage++ }
            2 ->
                npc(
                    "Next time give me something more valuable...",
                    "I couldn't get anything for this on the blackmarket.",
                ).also { stage++ }

            3 -> {
                end()
                player("That was the idea.")
                sendMessage(player!!, "He gives you the vial of liquid honey.")
                addItemOrDrop(player!!, Items.LIQUID_HONEY_416)
                removeAttribute(player!!, BiohazardUtils.FIRST_VIAL_CORRECT)
            }
        }
    }
}
