package content.region.kandarin.dialogue.stronghold

import content.global.travel.EssenceTeleport
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
class BrimstailDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.OLD_NORMAL, "Hello adventurer, what can I do for you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                    options(
                        "Hello, just wanted a chat.",
                        "Nothing for now, thanks!",
                        "I need to mine some rune essence.",
                    ).also { stage++ }
                } else {
                    options("Hello, just wanted a chat.", "Nothing for now, thanks!").also { stage++ }
                }

            1 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Hello, just wanted a chat.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Nothing for now, thanks!").also { stage = 7 }
                    3 -> playerl(FaceAnim.NEUTRAL, "Can you teleport me to the Rune Essence?").also { stage = 5 }
                }
            2 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "It's always a pleasure to catch up with another mage! Especially one who has helped me so much! How are your researches going?",
                ).also {
                    stage++
                }
            3 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Oh, not so bad. I'll let you know if I find anything I think you'll find interesting...",
                ).also {
                    stage++
                }
            4 -> npcl(FaceAnim.OLD_NORMAL, "Thanks! I'd appreciate that.").also { stage = END_DIALOGUE }
            5 -> npcl(FaceAnim.OLD_NORMAL, "Okay. Hold onto your hat!").also { stage++ }
            6 -> {
                end()
                EssenceTeleport.teleport(npc, player)
            }
            7 ->
                npcl(FaceAnim.OLD_NORMAL, "Ok. Just remember that a friend of a wizard is a friend of mine!").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BrimstailDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BRIMSTAIL_171)
    }
}
