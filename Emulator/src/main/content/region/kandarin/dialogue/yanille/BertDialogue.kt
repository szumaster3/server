package content.region.kandarin.dialogue.yanille

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BertDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Bert! Good news!").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Arrr...Good news be always handy.").also { stage++ }
            2 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "They arrested Sandy for the murder of a wizard and the sand pit now refills itself!",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "ME JOB! I'VE LOSTED ME JOB! 'ow c'n yer say tha' be good news?? Me wife'll tear me limb fr'm limb!",
                ).also {
                    stage++
                }
            4 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Don't worry, the Wizards are going to pay you a large pension so that you can retire...",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Bu' wha'll I be doin' wit' me day now! I be lovin' tha sand.",
                ).also { stage++ }
            6 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "What will you do with your day? Well....You could build sand castles with your own two hands!",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I din't think so... bu' iffen yer ever need someone ta haul buckets o'sand 'round, ye be lettin' me know ${player!!.username}, I's can help yer!",
                ).also {
                    stage++
                }
            8 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Wow! That would be great, buckets of sand direct to bank everday you say? That's great!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BertDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BERT_3108)
    }
}
