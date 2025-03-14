package content.region.fremennik.dialogue.rellekka

import core.api.getAttribute
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
class BjornAndEldgrimDialogues(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            npc(FaceAnim.DRUNK, "Hey! Itsh you again! Whatshyerfashe!").also { stage = 10 }
        } else {
            player(FaceAnim.FRIENDLY, "Hello there.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.DRUNK,
                    "Hey! scheck it out! Itsh an outerlandub! Yer shud go shpeak to the chieftain!",
                ).also {
                    stage++
                }
            1 -> player(FaceAnim.ASKING, "The who?").also { stage++ }
            2 ->
                npcl(FaceAnim.DRUNK, "That guy over there by that stuff! (hic) Yeh, abshoultely! He's da bosh!").also {
                    stage =
                        END_DIALOGUE
                }
            10 -> player(FaceAnim.ASKING, "${player.name}?").also { stage++ }
            11 -> npcl(FaceAnim.DRUNK, "Nah nah nah, not them, the other one, whatshyerfashe!").also { stage++ }
            12 -> player(FaceAnim.ASKING, "${getAttribute(player, "fremennikname", "fremmyname")}?").also { stage++ }
            13 -> npc(FaceAnim.DRUNK, "Thatsh what I said diddle I?").also { stage++ }
            14 -> player(FaceAnim.ASKING, "Um.... okay. I'll leave you to your drinking.").also { stage++ }
            15 -> npc(FaceAnim.DRUNK, "Thanksh pal! You're alright!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BjornAndEldgrimDialogues(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BJORN_1284, NPCs.ELDGRIM_1285)
    }
}
