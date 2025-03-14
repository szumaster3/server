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
class PenguinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val babyPenguin =
        intArrayOf(
            NPCs.BABY_PENGUIN_6908,
            NPCs.BABY_PENGUIN_7313,
            NPCs.BABY_PENGUIN_7316,
        )
    private val adultPenguin =
        intArrayOf(
            NPCs.PENGUIN_6909,
            NPCs.PENGUIN_6910,
            NPCs.PENGUIN_7314,
            NPCs.PENGUIN_7315,
            NPCs.PENGUIN_7317,
            NPCs.PENGUIN_7318,
        )
    private val snowyRegion = intArrayOf(11830, 11318, 11066, 11067, 11068)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((0..3).random()) {
            0 -> npc(FaceAnim.CHILD_NORMAL, "Awk Awk!", "(This is fun!)").also { stage = 0 }
            1 -> npc(FaceAnim.CHILD_NORMAL, "Awkawk awk awk.", "(Dis place is rather dull.)").also { stage = 3 }
            2 -> npc(FaceAnim.CHILD_NORMAL, "Awwk...", "(I'm much too warm here...)").also { stage = 6 }
            else ->
                if (inZone(player, "snow") ||
                    inBorders(player, getRegionBorders(11830)) ||
                    inBorders(
                        player,
                        getRegionBorders(11318),
                    )
                ) {
                    npc(FaceAnim.CHILD_NORMAL, "Awk!", "(Slide!)").also { stage = 10 }
                } else {
                    npc(FaceAnim.CHILD_NORMAL, "Awwwwwwwwk?", "(Are we gonna be here much longer?)").also { stage = 8 }
                }
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "What's fun?").also { stage++ }
            1 -> npc(FaceAnim.CHILD_NORMAL, "Awk!", "(This!)").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "I'm glad someone's enjoying themself...").also { stage = END_DIALOGUE }

            3 -> playerl(FaceAnim.FRIENDLY, "It'll liven up in a while, I'm sure.").also { stage++ }
            4 -> npc(FaceAnim.CHILD_NORMAL, "Awkawk awk awk!", "(I know, let's go visit the zoo!)").also { stage++ }
            5 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Hmm, I dunno, the penguins there are a bit shifty. You shouldn't mix with them.",
                ).also { stage = END_DIALOGUE }

            6 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well, just give me a little while longer and maybe we'll go find some ice for you.",
                ).also { stage++ }

            7 ->
                npc(FaceAnim.CHILD_NORMAL, "Awk! Awkawk awk awk. (Yay! Don't be too long.)").also {
                    stage = END_DIALOGUE
                }

            8 -> playerl(FaceAnim.FRIENDLY, "Why? Is there some place else where you'd rather be?").also { stage++ }
            9 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Awk awk awk!",
                    "(For sure. Somewhere colder! Like the Motherland...)",
                ).also { stage = END_DIALOGUE }

            10 -> playerl(FaceAnim.FRIENDLY, "You want me to slide?").also { stage++ }
            11 -> npc(FaceAnim.CHILD_NORMAL, "Awwwwwwwwwwwwk!", "(Sliiiiiiide!)").also { stage++ }
            12 -> playerl(FaceAnim.FRIENDLY, "I can't; I don't know how. You have fun with it though.").also { stage++ }
            13 -> npc(FaceAnim.CHILD_NORMAL, "Awk!", "(Slide!)").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(*babyPenguin, *adultPenguin)
    }
}
