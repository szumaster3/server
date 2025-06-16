package content.region.kandarin.witchaven.dialogue

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FishermanDialogue(player: Player? = null, ) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("Hello there.")
        return when (npc.id) {
            NPCs.FISHERMAN_703 -> stage == 10
            NPCs.FISHERMAN_704 -> stage == 20
            else -> stage == 0
        }
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> sendDialogue(player!!, "His eyes are staring vacantly into space.").also { stage++ }
            1 -> npcl(FaceAnim.DRUNK, "Lost to us... She is Lost to us...").also { stage++ }
            2 -> player("Who is lost?").also { stage++ }
            3 -> npcl(FaceAnim.DRUNK, "Trapped by the light... Lost and trapped...").also { stage++ }
            4 -> player(FaceAnim.THINKING, "Ermm... So you don't want to tell me then?").also { stage++ }
            5 -> npcl(FaceAnim.DRUNK, "Trapped... In stone and darkness...").also { stage = END_DIALOGUE }

            10 -> sendDialogue(player!!, "His eyes are staring vacantly into space.").also { stage++ }
            11 -> npcl(FaceAnim.DRUNK, "Must find family...").also { stage++ }
            12 -> player("What?").also { stage++ }
            13 -> npcl(FaceAnim.DRUNK, "Soon we will all be together...").also { stage++ }
            14 -> player(FaceAnim.WORRIED, "Are you ok?").also { stage++ }
            15 -> npcl(FaceAnim.DRUNK, "Must find family... They are all under the blue... Deep deep under the blue...").also { stage++ }
            16 -> playerl(FaceAnim.HALF_ROLLING_EYES, "Ermm... I'll leave you to it then.").also { stage = END_DIALOGUE }

            20 -> sendDialogue(player!!, "His eyes are staring vacantly into space.").also { stage++ }
            21 -> npcl(FaceAnim.DRUNK, "Free of the deep blue we are... We must find...").also { stage++ }
            22 -> player("Yes?").also { stage++ }
            23 -> npcl(FaceAnim.DRUNK, "a new home... We must leave this place...").also { stage++ }
            24 -> player(FaceAnim.ASKING, "Where will you go?").also { stage++ }
            25 -> npcl(FaceAnim.DRUNK, "Away... Away to her...").also { stage++ }
            26 -> playerl(FaceAnim.AFRAID, "Riiiight.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = FishermanDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.FISHERMAN_702)
}
