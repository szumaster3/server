package content.minigame.puropuro.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FairyAerykaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DEFAULT, "It's still here.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player("Pardon?").also { stage++ }
            1 -> npc(FaceAnim.OLD_DEFAULT, "It's still here. The crop circle is still here.").also { stage++ }
            2 -> player("Oh yes, thanks Aery. It didn't go anywhere in the", "meantime, then?").also { stage++ }
            3 -> npc(FaceAnim.OLD_DEFAULT, "Nope. It just sat there.").also { stage++ }
            4 ->
                player(
                    "Jolly good. I can come back and visit Puro-Puro",
                    "whenever I want then. Brilliant!",
                ).also { stage++ }
            5 ->
                options(
                    "What's in Puro-Puro?",
                    "So what are these implings then?",
                    "I've heard I may find dragon equipment in Puro-Puro?",
                    "No, bye!",
                ).also {
                    stage++
                }
            6 ->
                when (buttonId) {
                    1 -> player("What's in Puro-Puro?").also { stage = 10 }
                    2 -> player("So what are these implings then?").also { stage = 20 }
                    3 -> player("I've heard I may find dragon equipment in Puro-Puro?").also { stage = 30 }
                    4 -> player("No, bye!").also { stage = 40 }
                }
            10 -> npc(FaceAnim.OLD_DEFAULT, "Implings...and wheat.").also { stage = END_DIALOGUE }
            20 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Well, no-one know for sure. The mischievous little",
                    "creatures are probably related to imps. And they fly as",
                    "well.",
                ).also {
                    stage++
                }
            21 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Also, like imps, they love collecting things. I'm not sure",
                    "why, though. They also seem to like being chased.",
                ).also {
                    stage++
                }
            22 -> player("So how would I get hold of what they are carrying,", "then?").also { stage++ }
            23 ->
                npc(FaceAnim.OLD_DEFAULT, "Catch them, I suppose I don't know really. Why would", "you want to?").also {
                    stage =
                        END_DIALOGUE
                }
            30 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Really? You humans like that stuff a lot, don't you? I",
                    "don't like really old stuff myself.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            40 -> npc(FaceAnim.OLD_DEFAULT, "See you around!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FAIRY_AERYKA_6072)
    }
}
