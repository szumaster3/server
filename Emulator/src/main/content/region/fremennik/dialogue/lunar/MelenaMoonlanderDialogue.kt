package content.region.fremennik.dialogue.lunar

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MelenaMoonlanderDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Hi. Welcome to the general store. How might I help you?").also { stage++ }
            1 -> options("What can you sell me?", "I have a question...", "I'm good thanks, bye.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.MELANA_MOONLANDER_4516)
                    }

                    2 -> player(FaceAnim.HALF_ASKING, "I have a question...").also { stage = 11 }
                    3 -> player(FaceAnim.FRIENDLY, "I'm good thanks, bye.").also { stage = END_DIALOGUE }
                }
            11 -> npc(FaceAnim.FRIENDLY, "About magic of course.").also { stage++ }
            12 -> player(FaceAnim.SUSPICIOUS, "Sorry?").also { stage++ }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I said about magic of course. You know, in response to your question.",
                ).also { stage++ }
            14 -> player(FaceAnim.HALF_THINKING, "But I didn't ask anything yet.").also { stage++ }
            15 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes, but you were thinking of asking me how I was floating.",
                ).also { stage++ }
            16 -> player(FaceAnim.AMAZED, "That's true! How could you possibly know that?").also { stage++ }
            17 -> npc(FaceAnim.HALF_THINKING, "Don't you realise we can read your mind.").also { stage++ }
            18 -> player(FaceAnim.SUSPICIOUS, "Oh, of course. How do you manage to do that?").also { stage++ }
            19 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "It's quite simple, everyone has a resonance that is responded to by the moon. This resonance changes depending on what we are thinking. You can tune yourself in to listen to this",
                ).also {
                    stage++
                }
            20 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "resonance with practice - it's a life long quest for the members of the Moon Clan, but its especially easy to read with outsiders like yourself,",
                ).also {
                    stage++
                }
            21 -> npc(FaceAnim.FRIENDLY, "as you are far louder and unguarded.").also { stage++ }
            22 -> player(FaceAnim.SUSPICIOUS, "I see. I best be careful what I think of then.").also { stage++ }
            23 -> player(FaceAnim.WORRIED, "...").also { stage++ }
            24 -> player(FaceAnim.THINKING, "...").also { stage++ }
            25 -> npc(FaceAnim.DISGUSTED_HEAD_SHAKE, "That's disgusting!").also { stage++ }
            26 -> player(FaceAnim.SAD, "Sorry.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return MelenaMoonlanderDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MELANA_MOONLANDER_4516)
    }
}
