package content.region.kandarin.dialogue.ardougne

import core.api.inEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class ArdougneMonkeyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.MSPEAK_AMULET_4021, 1)) {
            npc(FaceAnim.OLD_LAUGH1, "Eeekeek ookeek!").also { stage = END_DIALOGUE }
        } else {
            var a = 1..5
            when (a.random()) {
                1 -> npc(FaceAnim.OLD_LAUGH1, "Arr!").also { stage = 10 }
                2 ->
                    npcl(FaceAnim.OLD_LAUGH1, "Let me go, can't ye hear them? Howlin' in the dark...").also {
                        stage = 20
                    }
                3 ->
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "I'm not goin' back in that brewery, not fer all the Bitternuts I can carry!",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                4 -> npc(FaceAnim.OLD_DEFAULT, "Are ye here for...the stuff?").also { stage = 30 }
                5 -> npc(FaceAnim.OLD_DISTRESSED, "Arr! Yer messin with me monkey plunder!").also { stage = 40 }
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            10 -> player(FaceAnim.JOLLY, "Arr!").also { stage++ }
            11 -> npc(FaceAnim.OLD_LAUGH1, "Arr!").also { stage++ }
            12 -> player(FaceAnim.JOLLY, "Arr!").also { stage++ }
            13 -> npc(FaceAnim.OLD_LAUGH1, "Arr!").also { stage++ }
            14 -> player(FaceAnim.JOLLY, "Arr!").also { stage++ }
            15 -> npc(FaceAnim.OLD_LAUGH1, "Arr!").also { stage++ }
            16 -> player(FaceAnim.JOLLY, "Arr!").also { stage++ }
            17 -> npc(FaceAnim.OLD_LAUGH1, "Bored now...").also { stage = END_DIALOGUE }

            20 -> player(FaceAnim.ASKING, "What do you mean?").also { stage++ }
            21 -> npc(FaceAnim.OLD_DISTRESSED, "I'm not hangin' around te be killed!").also { stage++ }
            22 -> npc(FaceAnim.OLD_DISTRESSED, "The Horrors, the Horrors!").also { stage = END_DIALOGUE }

            30 -> player(FaceAnim.ASKING, "What?").also { stage++ }
            31 -> npc(FaceAnim.OLD_DEFAULT, "You know...the 'special' bananas?").also { stage++ }
            32 -> player(FaceAnim.ASKING, "No... why do you ask?").also { stage++ }
            33 -> npc(FaceAnim.OLD_SAD, "No reason. Have a nice day.").also { stage = END_DIALOGUE }

            40 -> player(FaceAnim.ASKING, "What?").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ArdougneMonkeyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MONKEY_4363)
    }
}
