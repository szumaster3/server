package content.region.asgarnia.dialogue

import core.api.teleport
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class ZandarHorfyreDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_THINKING, "Who are you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "My name is Zandar Horfyre, and you ${player.name} are trespassing in my tower, not to mention attacking my students! I thank you to leave immediately!",
                ).also {
                    stage++
                }
            1 -> options("Ok, I was going anyway.", "No, I think I'll stay for a bit.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player("Ok, I was going anyway.").also { stage++ }
                    2 -> player("No, I think I'll stay for a bit.").also { stage = 4 }
                }
            3 -> npcl(FaceAnim.NEUTRAL, "Good! And don't forget to close the door behind you!").also { stage = 7 }
            4 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "Actually, that wasn't an invitation. I've tried being polite, now we'll do it the hard way!",
                ).also {
                    teleport(player, Location.create(3217, 3177, 0), TeleportManager.TeleportType.INSTANT)
                }.also { stage++ }
            5 -> player(FaceAnim.ANGRY, "Zamorak curse that mage!").also { stage++ }
            6 -> player(FaceAnim.LAUGH, "Actually, I guess he already has!").also { stage++ }
            7 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ZandarHorfyreDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ZANDAR_HORFYRE_3308)
    }
}
