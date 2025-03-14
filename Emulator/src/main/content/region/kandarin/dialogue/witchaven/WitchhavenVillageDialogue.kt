package content.region.kandarin.dialogue.witchaven

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class WitchhavenVillageDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello there.").also { stage = conversations.random() }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> sendDialogue("Their eyes are staring vacantly into space.").also { stage++ }
            1 -> npc(FaceAnim.NEUTRAL, "Ye mariners all, as ye pass by,").also { stage++ }
            2 -> npc(FaceAnim.NEUTRAL, "Come in and drink if you are dry,").also { stage++ }
            3 -> npc(FaceAnim.NEUTRAL, "Come spend, me lads, your money brisk,").also { stage++ }
            4 -> npc(FaceAnim.NEUTRAL, "And pop your nose in a jug of this.").also { stage++ }
            5 -> player(FaceAnim.NEUTRAL, "You're not fooling anyone you know.").also { stage++ }
            6 -> npc(FaceAnim.NEUTRAL, "We fooled you easily enough.").also { stage = END_DIALOGUE }

            7 -> sendDialogue("Their eyes are staring vacantly into space.").also { stage++ }
            8 -> npc(FaceAnim.NEUTRAL, "Free. She is free...").also { stage++ }
            9 -> player(FaceAnim.NEUTRAL, "What?").also { stage++ }
            10 -> npc(FaceAnim.NEUTRAL, "The mother is free.").also { stage = END_DIALOGUE }

            11 -> sendDialogue("Their eyes are staring vacantly into space.").also { stage++ }
            12 -> npc(FaceAnim.NEUTRAL, "You! You did it!").also { stage++ }
            13 -> player(FaceAnim.NEUTRAL, "I didn't mean to!").also { stage++ }
            14 -> npc(FaceAnim.NEUTRAL, "You killed him!").also { stage++ }
            15 -> player(FaceAnim.NEUTRAL, "It was an accide... Killed who?").also { stage++ }
            16 -> npc(FaceAnim.NEUTRAL, "Our Prince, you killed our Prince.").also { stage++ }
            17 -> player(FaceAnim.NEUTRAL, "Oh that, yes I did.").also { stage++ }
            18 -> npc(FaceAnim.NEUTRAL, "Leave us alone.").also { stage = 99 }

            19 -> sendDialogue("Their eyes are staring vacantly into space.").also { stage++ }
            20 -> npc(FaceAnim.NEUTRAL, "Soon now... So soon...").also { stage++ }
            21 -> npc(FaceAnim.NEUTRAL, "The stars are almost right.").also { stage++ }
            22 -> player(FaceAnim.NEUTRAL, "For what?").also { stage++ }
            23 -> npc(FaceAnim.NEUTRAL, ". . .").also { stage = END_DIALOGUE }

            24 -> sendDialogue("Their eyes are staring vacantly into space.").also { stage++ }
            25 -> npc(FaceAnim.NEUTRAL, "Ahh, our saviour.").also { stage++ }
            26 -> player(FaceAnim.NEUTRAL, "Please don't remind me.").also { stage++ }
            27 -> npc(FaceAnim.NEUTRAL, "Do not worry, soon your regret will be gone.").also { stage++ }
            28 -> player(FaceAnim.NEUTRAL, "If you think you will get to me...").also { stage++ }
            29 -> npc(FaceAnim.NEUTRAL, "All in good time.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return WitchhavenVillageDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.WITCHAVEN_VILLAGER_4883,
            NPCs.WITCHAVEN_VILLAGER_4884,
            NPCs.WITCHAVEN_VILLAGER_4885,
            NPCs.WITCHAVEN_VILLAGER_4886,
            NPCs.WITCHAVEN_VILLAGER_4887,
            NPCs.WITCHAVEN_VILLAGER_4888,
        )
    }

    companion object {
        private val conversations = arrayOf(0, 7, 11, 19, 24)
    }
}
