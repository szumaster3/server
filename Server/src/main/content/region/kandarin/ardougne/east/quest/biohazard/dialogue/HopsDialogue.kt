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
 * Represents the Hops dialogue.
 *
 * # Relations
 * - [Biohazard][content.region.kandarin.ardougne.east.quest.biohazard.Biohazard]
 */
@Initializable
class HopsDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        val THIRD_VIAL = getAttribute(player, GameAttributes.THIRD_VIAL_CORRECT, false)
        val WRONG_VIAL = getAttribute(player, GameAttributes.THIRD_VIAL_WRONG, false)
        val QUEST_STAGE = getQuestStage(player, Quests.BIOHAZARD)

        when {
            npc.id == NPCs.HOPS_340 && THIRD_VIAL || WRONG_VIAL -> {
                npcl(FaceAnim.NEUTRAL, "I suppose I'd better get going. I'll meet you at the Dancing Donkey Inn.")
                stage = END_DIALOGUE
            }

            !GameWorld.settings!!.isMembers && npc.id == NPCs.HOPS_340 -> {
                sendDialogue(player, "Hops don't wanna talk now. Try different world, HIC!")
                stage = END_DIALOGUE
            }

            !GameWorld.settings!!.isMembers && npc.id == NPCs.HOPS_340 -> {
                sendMessage(player, "He isn't in a fit state to talk.")
                stage = END_DIALOGUE
            }

            npc.id == NPCs.HOPS_341 && THIRD_VIAL || WRONG_VIAL -> {
                player("Hello, how was your journey?")
                stage = 8
            }

            QUEST_STAGE == 0 || QUEST_STAGE == 100 -> {
                sendMessage(player, "Hops doesn't feel like talking.")
                stage = END_DIALOGUE
            }

            QUEST_STAGE >= 10 -> {
                player("Hi, I've got something for you to take to Varrock.")
            }

            else -> {
                when(npc.id){
                    NPCs.HOPS_340 -> sendMessage(player, "He isn't in a fit state to talk.")
                    else -> sendMessage(player, "Hops doesn't feel like talking.")
                }
            }
        }

        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        npc = NPC(NPCs.HOPS_340)
        when (stage) {
            0 -> npc("Sounds like pretty thirsty work.").also { stage++ }
            1 -> player("Well, there's an Inn in Varrock if you're desperate.").also { stage++ }
            2 -> npc("Don't worry, I'm a pretty resourceful fellow you know.").also { stage++ }
            3 -> options("You give him the vial of ethenea...", "You give him the vial of liquid honey...", "You give him the vial of sulphuric broline...").also { stage++ }
            4 -> when (buttonID) {
                1 -> {
                    end()
                    if (!removeItem(player!!, Items.ETHENEA_415)) {
                        sendMessage(player!!, "You can't give him what you don't have.")
                    } else {
                        sendMessage(player!!, "You give him the vial of ethenea.")
                        stage = 7
                    }
                }

                2 -> {
                    end()
                    if (!removeItem(player!!, Items.LIQUID_HONEY_416)) {
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
                        player("Ok, I'll see you in Varrock.")
                        stage++
                    }
                }
            }
            5 -> npc(FaceAnim.HAPPY, "Sure, I'm a regular at the Dancing Donkey Inn as it", "happens.").also { stage++ }
            6 -> {
                end()
                setAttribute(player!!, GameAttributes.THIRD_VIAL_CORRECT, true)
            }
            7 -> {
                end()
                setAttribute(player!!, GameAttributes.THIRD_VIAL_WRONG, true)
            }
            8 -> npc("Pretty thirst-inducing actually...").also { stage++ }
            9 -> player("Please tell me that you haven't drunk the contents...").also { stage++ }
            10 -> if(getAttribute(player, GameAttributes.THIRD_VIAL_CORRECT, false)) {
                npc(FaceAnim.SCARED, "Oh the gods no! What do you take me for?")
                stage++
            } else {
                npc(FaceAnim.NEUTRAL, "Of course I can tell you that I haven't drunk the contents...")
                stage = 13
            }
            11 -> npc("Here's your vial anyway.").also { stage++ }
            12 -> {
                end()
                sendMessage(player!!, "He gives you the vial of ethenea.")
                player("Thanks, I'll let you get your drink now.")
                addItemOrDrop(player!!, Items.SULPHURIC_BROLINE_417, 1)
                removeAttribute(player!!, GameAttributes.THIRD_VIAL_CORRECT)
            }
            13 -> npcl(FaceAnim.HALF_THINKING, "But I'd be lying. Sorry about that me old mucker, can I get you a drink?").also { stage++ }
            14 -> playerl(FaceAnim.NEUTRAL, "No, I think you've done enough for now.").also { stage++ }
            15 -> {
                end()
                removeAttribute(player, GameAttributes.THIRD_VIAL_WRONG)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = HopsDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.HOPS_340, NPCs.HOPS_341)
}