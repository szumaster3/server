package content.global.skill.summoning.pet.dialogue

import core.api.getRegionBorders
import core.api.inBorders
import core.api.inZone
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class GeckoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val babyGecko =
        intArrayOf(
            NPCs.BABY_GECKO_6915,
            NPCs.BABY_GECKO_7277,
            NPCs.BABY_GECKO_7278,
            NPCs.BABY_GECKO_7279,
            NPCs.BABY_GECKO_7280,
        )
    private val adultGecko =
        intArrayOf(
            NPCs.GECKO_6916,
            NPCs.GECKO_7281,
            NPCs.GECKO_7282,
            NPCs.GECKO_7283,
            NPCs.GECKO_7284,
        )

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((0..4).random()) {
            0 -> npc(FaceAnim.CHILD_NORMAL, "Moop, Gecko presko!", "(You're nice to me!)").also { stage = END_DIALOGUE }
            1 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Gronk, Gecko gronk?",
                    "(When can we go back to the jungle?)",
                ).also { stage = 0 }

            2 -> npc(FaceAnim.CHILD_NORMAL, "Hiss gronk. Gecko?", "(Can you carry me?)").also { stage = 1 }
            3 -> npc(FaceAnim.CHILD_NORMAL, "Hiss, gecko. Gecko!", "(I want to rest.)").also { stage = 3 }
            else ->
                if (inBorders(player, getRegionBorders(12598)) || inZone(player, "Desert Zone")) {
                    npc(FaceAnim.CHILD_NORMAL, "Hss! Hsss! Gecko! Hsss!", "(Too hot! Let's go home!)").also {
                        stage = 5
                    }
                } else {
                    npc(FaceAnim.CHILD_NORMAL, "Meep, gecko! Gecko!", "(Happy happy!)").also { stage = 4 }
                }
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "When I have a reason to go there.").also { stage = END_DIALOGUE }
            1 -> playerl(FaceAnim.FRIENDLY, "Well if you are tired I suppose I could...").also { stage++ }
            2 ->
                npc(FaceAnim.CHILD_NORMAL, "Gecko! Gronk gronk.", "(It's hard to keep up with you!)").also {
                    stage = END_DIALOGUE
                }

            3 -> playerl(FaceAnim.FRIENDLY, "I think I could do with a short rest, too.").also { stage = END_DIALOGUE }
            4 -> playerl(FaceAnim.FRIENDLY, "Hehe, you seem full of energy today.").also { stage = END_DIALOGUE }
            5 -> playerl(FaceAnim.FRIENDLY, "Don't worry, we'll be out of the sun soon.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(*babyGecko, *adultGecko)
    }
}
