package content.region.desert.uzer.dialogue

import core.api.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class FatherBaddenDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getQuestStage(player, Quests.DESERT_TREASURE)) {
            in 1..15 -> {
                npcl(FaceAnim.FRIENDLY, "What brings you to this godsforsaken desert?")
            }

            else -> {
                npcl(FaceAnim.FRIENDLY, "How is it going?")
                stage = 100
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Reen sent me.", "Just passing through.").also { stage = 1 }
            1 -> when (buttonId) {
                1 -> {
                    playerl(FaceAnim.FRIENDLY, "Reen sent me.")
                    stage = 2
                }

                2 -> {
                    playerl(FaceAnim.FRIENDLY, "Just passing through.")
                    stage = 10
                }
            }

            2 -> npcl(FaceAnim.FRIENDLY, "Saradomin be praised! And you have Silverlight! You're willing to help us, then?").also { stage = 3 }
            3 -> options("Tell me more about Agrith Naar.", "Tell me more about Denath.", "So what do you want me to do?").also { stage = 4 }
            4 -> when (buttonId) {
                1 -> {
                    playerl(FaceAnim.FRIENDLY, "Tell me more about Agrith Naar.")
                    stage = 5
                }

                2 -> {
                    playerl(FaceAnim.FRIENDLY, "Tell me more about Denath.")
                    stage = 7
                }

                3 -> {
                    playerl(FaceAnim.FRIENDLY, "So what do you want me to do?")
                    stage = 12
                }
            }
            5 -> npcl(FaceAnim.FRIENDLY, "Agrith Naar is not so much a demon as a force of nature...").also { stage = END_DIALOGUE }
            7 -> npcl(FaceAnim.FRIENDLY, "We've been keeping an eye on Denath for some years now...").also { stage = 8 }
            8 -> options("Tell me more about Agrith Naar.", "Why has he come to Uzer?", "So what do you want me to do?").also { stage = 9 }
            9 -> when (buttonId) {
                1 -> {
                    playerl(FaceAnim.FRIENDLY, "Tell me more about Agrith Naar.")
                    stage = 5
                }

                2 -> {
                    playerl(FaceAnim.FRIENDLY, "Why has he come to Uzer?")
                    stage = 14
                }

                3 -> {
                    playerl(FaceAnim.FRIENDLY, "So what do you want me to do?")
                    stage = 12
                }
            }

            10 -> npcl(FaceAnim.FRIENDLY, "Oh dear. I was hoping you would arrive.").also { stage = END_DIALOGUE }
            12 -> npcl(FaceAnim.FRIENDLY, "You have to be in position to kill Agrith Naar as soon as he is summoned...").also { stage = 13 }

            13 -> options("How can I do that?", "I'd never take part in a demonic ritual!").also { stage = 14 }
            14 -> when(buttonId) {
                1 -> npcl(FaceAnim.FRIENDLY, "I don't know. You'll have to find some way to convince them you're one of them.").also { stage = END_DIALOGUE }
                2 -> npcl(FaceAnim.FRIENDLY, "Oh, don't be so simple-minded!").also { stage++ }
            }
            15 -> npcl(FaceAnim.FRIENDLY, "You'll be summoning a demon in order to kill it, to rid the world of great evil! You won't really be joining the group, just infiltrating it.").also { stage = END_DIALOGUE }
            16 -> options("Tell me more about Agrith Naar.", "Foul construct? Do you mean the golem?", "So what do you want me to do?").also { stage = 17 }
            17 -> when (buttonId) {
                1 -> {
                    playerl(FaceAnim.FRIENDLY, "Tell me more about Agrith Naar.")
                    stage = 5
                }

                2 -> {
                    playerl(FaceAnim.FRIENDLY, "Foul construct? Do you mean the golem?")
                    stage = 18
                }

                3 -> {
                    playerl(FaceAnim.FRIENDLY, "So what do you want me to do?")
                    stage = 12
                }
            }

            18 -> npcl(FaceAnim.FRIENDLY, "The golem, yes! An abomination in the eyes of holy Saradomin!...").also { stage = 19 }

            19 -> options("Tell me more about Agrith Naar.", "So what do you want me to do?").also { stage = 20 }

            20 -> when (buttonId) {
                1 -> {
                    playerl(FaceAnim.FRIENDLY, "Tell me more about Agrith Naar.")
                    stage = 5
                }

                2 -> {
                    playerl(FaceAnim.FRIENDLY, "So what do you want me to do?")
                    stage = 12
                }
            }

            100 -> options("I haven't managed to infiltrate the group yet.", "Tell me more about Agrith Naar.", "Tell me more about Denath.", "I'd never take part in a demonic ritual!").also { stage = 101 }
            101 -> when (buttonId) {
                1 -> {
                    playerl(FaceAnim.FRIENDLY, "I haven't managed to infiltrate the group yet.")
                    stage = 102
                }

                2 -> {
                    playerl(FaceAnim.FRIENDLY, "Tell me more about Agrith Naar.")
                    stage = 5
                }

                3 -> {
                    playerl(FaceAnim.FRIENDLY, "Tell me more about Denath.")
                    stage = 7
                }

                4 -> {
                    playerl(FaceAnim.FRIENDLY, "I'd never take part in a demonic ritual!")
                    stage = 22
                }
            }

            102 -> npcl(FaceAnim.FRIENDLY, "Oh dear. Do try again. You must be in place when they summon the demon!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = FatherBaddenDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.FATHER_BADDEN_2902)
}
