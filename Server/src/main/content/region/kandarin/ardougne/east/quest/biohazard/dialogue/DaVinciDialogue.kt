package content.region.kandarin.ardougne.east.quest.biohazard.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Da Vinci dialogue.
 *
 * # Relations
 * - [Biohazard][content.region.kandarin.ardougne.east.quest.biohazard.Biohazard]
 */
@Initializable
class DaVinciDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        val SECOND_VIAL = getAttribute(player, GameAttributes.SECOND_VIAL_CORRECT, false)
        val WRONG_VIAL = getAttribute(player, GameAttributes.SECOND_VIAL_WRONG, false)
        val QUEST_STAGE = getQuestStage(player, Quests.BIOHAZARD)

        when {
            npc.id == NPCs.DA_VINCI_336 && SECOND_VIAL || WRONG_VIAL -> {
                npcl(FaceAnim.NEUTRAL, "Oh, it's you again. Please don't distract me now, I'm contemplating the sublime.").also { stage = 7 }
            }

            !GameWorld.settings!!.isMembers -> {
                npcl(FaceAnim.ANNOYED, "Bah! A great artist such as myself should not have to suffer the HUMILIATION of spending time on these dreadful worlds where non-members wander everywhere!").also { stage = 7 }
            }

            npc.id == NPCs.DA_VINCI_337 && SECOND_VIAL || WRONG_VIAL -> {
                npc("Hello again. I hope your journey was as pleasant as", "mine.")
                stage = 8
            }

            QUEST_STAGE == 0 || QUEST_STAGE == 100 -> sendMessage(player, "Da Vinci does not feel sufficiently moved to talk.").also { stage = 7 }

            QUEST_STAGE >= 10 -> player("Hello, I hear you're an errand boy for the chemist.")

            else -> {
                sendMessage(player, "Da Vinci does not feel sufficiently moved to talk.")
                stage = 7
            }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc("Well that's my job yes. But I don't necessarily define", "my identity in such black and white terms.").also { stage++ }
            1 -> player("Good for you. Now can you take a vial to Varrock for", "me?").also { stage++ }
            2 -> npc("Go on then.").also { stage++ }
            3 -> options("You give him the vial of ethenea...", "You give him the vial of liquid honey...", "You give him the vial of sulphuric broline...").also { stage++ }
            4 -> when (buttonId) {
                1 -> {
                    if (!removeItem(player!!, Items.ETHENEA_415)) {
                        end()
                        sendMessage(player!!, "You can't give him what you don't have.")
                    } else {
                        sendMessage(player!!, "You give him the vial of ethenea.")
                        player("Right. I'll see you later in the Dancing Donkey Inn.")
                        stage++
                    }
                }

                2 -> {
                    if (!removeItem(player!!, Items.LIQUID_HONEY_416)) {
                        end()
                        sendMessage(player!!, "You can't give him what you don't have.")
                    } else {
                        sendMessage(player!!, "You give him the vial of liquid honey.")
                        stage = 7
                    }
                }

                3 -> {
                    if (!removeItem(player!!, Items.SULPHURIC_BROLINE_417)) {
                        end()
                        sendMessage(player!!, "You can't give him what you don't have.")
                    } else {
                        sendMessage(player!!, "You give him the vial of sulphuric broline.")
                        stage = 7
                    }
                }
            }

            5 -> npc("That's right.").also { stage++ }
            6 -> {
                end()
                setAttribute(player!!, GameAttributes.SECOND_VIAL_CORRECT, true)
            }
            7 -> {
                end()
                setAttribute(player!!, GameAttributes.SECOND_VIAL_WRONG, true)
            }
            8 -> {
                if(getAttribute(player, GameAttributes.FIRST_VIAL_CORRECT, false)) {
                    playerl(FaceAnim.FRIENDLY, "Well, as they say, it's always sunny in Gielinor.")
                    stage = 9
                } else {
                    playerl(FaceAnim.FRIENDLY, "Yep. Anyway, I'll take the package off you now.")
                    stage = 11
                }
            }
            9 -> npc("Ok, here it is.").also { stage++ }
            10 -> {
                end()
                sendMessage(player!!, "He gives you the vial of ethenea.")
                player("Thanks, you've been a big help.")
                addItemOrDrop(player!!, Items.ETHENEA_415, 1)
                removeAttribute(player!!, GameAttributes.SECOND_VIAL_CORRECT)
            }
            11 -> npcl(FaceAnim.HALF_THINKING, "Package? That's a funny way to describe a liquid of such exquisite beauty!").also { stage++ }
            12 -> options("I'm getting a bad feeling about this.", "Just give me the stuff now please.").also { stage++ }
            13 -> when(buttonId) {
                1 -> player("I'm getting a bad feeling about this.").also { stage++ }
                2 -> player("Just give me the stuff now please.").also { stage++ }
            }
            14 -> player("You do still have it don't you?").also { stage++ }
            15 -> npcl(FaceAnim.FRIENDLY, "Absolutely. It's just not stored in a vial anymore.").also { stage++ }
            16 -> player(FaceAnim.ASKING, "What?").also { stage++ }
            17 -> npcl(FaceAnim.FRIENDLY, "Instead it has been liberated. It now gleams from the canvas of my latest epic: The Majesty of Varrock!").also { stage++ }
            18 -> playerl(FaceAnim.FRIENDLY, "That's great. Thanks to you I'll have to walk back to East Ardougne to get another vial.").also { stage++ }
            19 -> npcl(FaceAnim.FRIENDLY, "Well you can't put a price on art.").also { stage++ }
            20 -> {
                end()
                removeAttribute(player!!, GameAttributes.SECOND_VIAL_WRONG)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = DaVinciDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.DA_VINCI_336, NPCs.DA_VINCI_337)
}