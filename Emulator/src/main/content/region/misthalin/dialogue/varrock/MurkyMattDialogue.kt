package content.region.misthalin.dialogue.varrock

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.ge.GuidePrices
import core.game.ge.GuidePrices.GuideType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MurkyMattDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "A pirate!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "Arrrr, How'd ye be guessing that, me-lad?").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "You're kidding, right?").also { stage++ }
            2 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Nay! Now, what is it that ye be wantin?",
                    "I can tell ye all about the prices of runes, I can.",
                ).also {
                    stage++
                }
            3 ->
                options(
                    "What's a pirate doing here?",
                    "Tell me about the prices of runes.",
                    "I got to go, erm, swab some decks! Yar!",
                ).also {
                    stage++
                }
            4 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "What's a pirate doing here?").also { stage = 5 }
                    2 -> player(FaceAnim.HALF_GUILTY, "Tell me about the prices of runes.").also { stage = 14 }
                    3 ->
                        player(FaceAnim.HALF_GUILTY, "I got to go, erm, swab some decks! Yarr!").also {
                            stage =
                                END_DIALOGUE
                        }
                }
            5 -> npc(FaceAnim.HALF_GUILTY, "By my sea-blistered skin, I could ask the same of you!").also { stage++ }
            6 -> player(FaceAnim.HALF_GUILTY, "But... I'm not a pirate?").also { stage++ }
            7 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "No? Then what's that smell? The smell o'",
                    "someone spent too long at sea without a bath!",
                ).also {
                    stage++
                }
            8 -> player(FaceAnim.HALF_GUILTY, "I think that's probably you.").also { stage++ }
            9 -> npc(FaceAnim.HALF_GUILTY, "Har har har!").also { stage++ }
            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "We've got a stern landlubber 'ere'! Well, let",
                    "me tell ye, I'm here for the Grand Exchange!",
                ).also {
                    stage++
                }
            11 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Don't you just want to sell it in a shop or trade",
                    "it to someone specific?",
                ).also {
                    stage++
                }
            12 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "By my wave-battered bones! Not when I can sell to",
                    "the whole world from this very spot!",
                ).also {
                    stage++
                }
            13 -> player(FaceAnim.HALF_GUILTY, "You pirates are nothing but trouble!").also { stage = END_DIALOGUE }
            14 -> {
                end()
                GuidePrices.open(player, GuideType.RUNES)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return MurkyMattDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MURKY_MATT_RUNES_6525)
    }
}
