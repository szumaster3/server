package content.region.island.miscellania.dialogue

import content.region.island.waterbirth.plugin.TravelDestination
import content.region.island.waterbirth.plugin.WaterbirthUtils.sail
import core.api.inBorders
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SailorDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        playerl(FaceAnim.ASKING, "Hello. Can I get a ride on your ship?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Hello brother ${player!!.username}. If you are ready to jump aboard, we're all ready to set sail with the tide!",
                ).also {
                    stage++
                }
            1 -> options("Let's go!", "Actually no.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> {
                        end()
                        if (inBorders(player, 2580, 3844, 2584, 3847)) {
                            sail(player!!, TravelDestination.MISCELLANIA_TO_RELLEKKA)
                        } else {
                            sail(player!!, TravelDestination.RELLEKA_TO_MISCELLANIA)
                        }
                    }
                    2 -> npc("Okay. Suit yourself.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue =
        content.region.island.miscellania.dialogue.SailorDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SAILOR_1304)
}
