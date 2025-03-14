package content.region.misthalin.dialogue.dorgeshuun

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class AmbassadorAlvijarDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (RandomFunction.random(0, 5)) {
            0 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I can't stand these goblins sometimes! I come up with suggestions to make their city more efficient, and they just say it would spoil their precious way of life!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            1 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I miss Keldagrim. This city is all very well, but no one makes an underground city like the dwarves!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            2 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "It's funny... whenever we dwarves have ventured onto the surface, we've found ourselves at war with the goblins there. But now we meet these goblins underground, and they're no threat at all.",
                ).also {
                    stage =
                        6
                }
            3 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Trade with the goblins is going well at the moment. It's a pity they don't seem as interested in our beer as in your food, though!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            4 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Thank you again for helping us complete the train link. Further delays would have cost the Consortium a lot of money.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            5 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Oh, and I hear you saved someone's life as well. Well done on that count too.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            6 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Maybe this will herald a change in our relations with the surface goblins as well.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return AmbassadorAlvijarDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AMBASSADOR_ALVIJAR_5863)
    }
}
