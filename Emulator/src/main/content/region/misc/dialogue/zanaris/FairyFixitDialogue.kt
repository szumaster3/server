package content.region.misc.dialogue.zanaris

import core.api.interaction.openNpcShop
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class FairyFixitDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.FAIRYTALE_II_CURE_A_QUEEN)) {
            npc(FaceAnim.OLD_CALM_TALK1, "Pssst! Human! I've got something for you.").also { stage = 20 }
        } else {
            npc(FaceAnim.OLD_DISTRESSED, "What is it, human? Busy busy busy!")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Why are you carrying that toolbox?", "I'm okay, thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "Why are you carrying that toolbox?").also { stage = 10 }
                    2 -> player(FaceAnim.FRIENDLY, "I'm okay, thanks.").also { stage = END_DIALOGUE }
                }
            10 -> npc(FaceAnim.OLD_DEFAULT, "It's the fizgog! It's picking up cable again!").also { stage++ }
            11 -> playerl(FaceAnim.ASKING, "Uh, right. So is it safe to use the fairy rings then?").also { stage++ }
            12 ->
                npcl(
                    FaceAnim.OLD_CALM_TALK1,
                    "Sure, as long as you have been given permission to use them.",
                ).also { stage++ }
            13 ->
                npcl(
                    FaceAnim.OLD_CALM_TALK2,
                    "You should just be aware that using the fairy rings sometimes has strange results - the locations that you have been to may affect the locations you are trying to reach.",
                ).also {
                    stage++
                }
            14 ->
                npc(
                    FaceAnim.OLD_CALM_TALK1,
                    "I could fix it by replacing the fizgog and the whosprangit; I've put in a request for some new parts, but they're pretty hard to get hold of it seems.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            20 ->
                options(
                    "What have you got for me?",
                    "Why are you carrying that toolbox?",
                    "Not interested, thanks.",
                ).also {
                    stage++
                }
            21 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "What have you got for me?").also { stage = 30 }
                    2 -> player(FaceAnim.ASKING, "Why are you carrying that toolbox?").also { stage = 10 }
                    3 -> player(FaceAnim.NEUTRAL, "Not interested, thanks.").also { stage = END_DIALOGUE }
                }
            30 ->
                npcl(
                    FaceAnim.OLD_CALM_TALK1,
                    "They said you'd helped cure our Queen. I haven't got a lot of rewards to offer, but my enchantment scrolls might help if you're working with fairy rings in your home.",
                ).also {
                    stage++
                }
            31 -> {
                end()
                openNpcShop(player, NPCs.FAIRY_FIXIT_4455)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return FairyFixitDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FAIRY_FIXIT_4455)
    }
}
