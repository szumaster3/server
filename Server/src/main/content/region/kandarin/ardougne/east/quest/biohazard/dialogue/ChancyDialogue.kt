package content.region.kandarin.ardougne.east.quest.biohazard.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import shared.consts.Items
import shared.consts.NPCs
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Quests

/**
 * Represents the Chancy dialogue.
 *
 * # Relations
 * - [Biohazard][content.region.kandarin.ardougne.east.quest.biohazard.Biohazard]
 */
@Initializable
class ChancyDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        val FIRST_VIAL = getAttribute(player, GameAttributes.FIRST_VIAL_CORRECT, false)
        val WRONG_VIAL = getAttribute(player, GameAttributes.FIRST_VIAL_WRONG, false)
        val QUEST_STAGE = getQuestStage(player, Quests.BIOHAZARD)

        when {
            npc.id == NPCs.CHANCY_338 && FIRST_VIAL || WRONG_VIAL -> {
                npcl(FaceAnim.NEUTRAL, "Look, I've got your vial but I'm not taking two. I always like to play the percentages.").also { stage = END_DIALOGUE }
            }

            !GameWorld.settings!!.isMembers &&  npc.id == NPCs.CHANCY_338 -> {
                player(FaceAnim.HALF_ASKING, "Hello! Playing Solitaire?!")
            }

            !GameWorld.settings!!.isMembers &&  npc.id == NPCs.CHANCY_339 -> {
                player(FaceAnim.HALF_ASKING, "Good morning.")
                stage = 2
            }

            npc.id == NPCs.CHANCY_339 && FIRST_VIAL || WRONG_VIAL -> {
                player("Hi, thanks for doing that.")
                stage = 12
            }

            QUEST_STAGE == 0 || QUEST_STAGE == 100 -> {
                sendMessage(player, "Chancy doesn't feel like talking.").also { stage = END_DIALOGUE }
            }

            QUEST_STAGE >= 10 -> {
                player("Hello, I've got a vial for you to take to Varrock.")
                stage = 4
            }

            else -> {
                sendMessage(player, "Chancy doesn't feel like talking.")
            }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.FRIENDLY, "Hush - I'm trying to perfect the art of dealing off the bottom of the deck.").also { stage++ }
            1 -> npcl(FaceAnim.NEUTRAL, "Whatever you want, come back later on a members' world and I'll speak to you then.").also { stage = END_DIALOGUE }

            2 -> npcl(FaceAnim.FRIENDLY, "Leave me alone - I'm trying to find my gambling buddies!").also { stage++ }
            3 -> npcl(FaceAnim.NEUTRAL, "Whenever you want, come back on a members' world and I'll speak to you then.").also { stage = END_DIALOGUE }

            4 -> npc(FaceAnim.SCARED, "Tssch... that chemist asks for a lot for the wages he", "pays.").also { stage++ }
            5 -> player("Maybe you should ask him for more money.").also { stage++ }
            6 -> npc("Nah... I just use my initiative here and there.").also { stage++ }
            7 -> options("You give him the vial of ethenea...", "You give him the vial of liquid honey...", "You give him the vial of sulphuric broline...").also { stage++ }
            8 -> when (buttonId) {
                1 -> {
                    if (!removeItem(player!!, Items.ETHENEA_415)) {
                        end()
                        sendMessage(player!!, "You can't give him what you don't have.")
                    } else {
                        sendMessage(player!!, "You give him the vial of ethenea.")
                        stage = 11
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
                        stage = 11
                    }
                }
            }

            9 -> npc("Be Lucky!").also { stage++ }
            10 -> {
                end()
                setAttribute(player!!, GameAttributes.FIRST_VIAL_CORRECT, true)
            }

            11 -> {
                end()
                setAttribute(player!!, GameAttributes.FIRST_VIAL_WRONG, true)
            }

            12 -> {
                if(getAttribute(player, GameAttributes.FIRST_VIAL_CORRECT, false)) {
                    npc("No problem.")
                    stage++
                } else {
                    npc("Next time give me something more valuable...", "I couldn't get anything for this on the blackmarket.")
                    stage = 14
                }
            }
            13 -> {
                end()
                player("That was the idea.")
                sendMessage(player!!, "He gives you the vial of liquid honey.")
                addItemOrDrop(player!!, Items.LIQUID_HONEY_416, 1)
                removeAttribute(player!!, GameAttributes.FIRST_VIAL_CORRECT)
            }
            14 -> {
                npcl(FaceAnim.FRIENDLY, "No problem. I've got some money for you actually.")
                removeAttribute(player!!, GameAttributes.FIRST_VIAL_WRONG)
                stage++
            }
            15 -> playerl(FaceAnim.FRIENDLY, "What do you mean?").also { stage++ }
            16 -> npcl(FaceAnim.FRIENDLY, "Well, it turns out that potion you gave me, was quite valuable...").also { stage++ }
            17 -> playerl(FaceAnim.FRIENDLY, "What?").also { stage++ }
            18 -> npcl(FaceAnim.FRIENDLY, "I know that I probably shouldn't have sold it... but some friends and I were having a little wager, the odds were just too good!").also { stage++ }
            19 -> playerl(FaceAnim.FRIENDLY, "You sold my vial and gambled with the money?!").also { stage++ }
            20 -> npcl(FaceAnim.FRIENDLY, "Actually yes... but praise be to Saradoming because I won! So all's well that ends well right?").also { stage++ }
            21 -> options("No! Nothing could be further from the truth!", "You have no idea what you have just done!").also { stage++ }
            22 -> when(buttonId) {
                1 -> playerl(FaceAnim.FRIENDLY, "No! Nothing could be further from the truth!").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "You have no idea what you have just done!").also { stage = 24 }
            }
            23 -> npcl(FaceAnim.HALF_THINKING, "Well, there's no pleasing some people.").also { stage = END_DIALOGUE }
            24 -> npcl(FaceAnim.HALF_THINKING, "Ignorance is bliss I'm afraid.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ChancyDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.CHANCY_338, NPCs.CHANCY_339)
}