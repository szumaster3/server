package content.region.asgarnia.quest.slug2.dialogue

import content.region.kandarin.witchaven.plugin.FishingPlatformBoat
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class FishingPlatformJebDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hey, Jeb.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc("Business here is complete. Do you wish to return to", "the dry land?").also { stage++ }
            1 -> options("No, I'm going to stay a while.", "Okay, let's go back.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player("No, I'm going to stay a while.").also { stage++ }
                    2 -> player("Okay, let's go back.").also { stage = 4 }
                }

            3 -> npc("Then stay if that is your wish.").also { stage = END_DIALOGUE }
            4 -> npc("Then board the rowing boat.").also { stage++ }
            5 -> {
                end()
                FishingPlatformBoat.sail(player, FishingPlatformBoat.Travel.FISHING_PLATFORM_TO_WITCHAVEN)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.JEB_4896)
}
