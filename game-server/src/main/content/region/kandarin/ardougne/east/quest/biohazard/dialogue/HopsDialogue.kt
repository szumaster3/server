package content.region.kandarin.ardougne.east.quest.biohazard.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Guidors Wife dialogue.
 *
 * # Relations
 * - [Biohazard][content.region.kandarin.ardougne.east.quest.biohazard.Biohazard]
 */
class HopsDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.HOPS_340)
        when (stage) {
            0 -> player("Hi, I've got something for you to take to Varrock.").also { stage++ }
            1 -> npc("Sounds like pretty thirsty work.").also { stage++ }
            2 -> player("Well, there's an Inn in Varrock if you're desperate.").also { stage++ }
            3 -> npc("Don't worry, I'm a pretty resourceful fellow you know.").also { stage++ }
            4 -> options("You give him the vial of ethenea...", "You give him the vial of liquid honey...", "You give him the vial of sulphuric broline...").also { stage++ }
            5 -> when (buttonID) {
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
                        stage = 8
                    }
                }

                3 -> {
                    if (!removeItem(player!!, Items.SULPHURIC_BROLINE_417)) {
                        end()
                        sendMessage(player!!, "You can't give him what you don't have.")
                    } else {
                        sendMessage(player!!, "You give him the vial of sulphuric broline.")
                        player("Ok, I'll see you in Varrock.")
                        stage++
                    }
                }
            }

            6 -> npc(FaceAnim.HAPPY, "Sure, I'm a regular at the Dancing Donkey Inn as it", "happens.").also { stage++ }

            7 -> {
                end()
                setAttribute(player!!, GameAttributes.THIRD_VIAL_CORRECT, true)
            }

            8 -> {
                end()
            }
        }
    }
}