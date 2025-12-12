package content.region.asgarnia.falador.dialogue

import core.api.teleport
import core.game.dialogue.*
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.tools.END_DIALOGUE

class ZandarHorfyreDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player(FaceAnim.HALF_THINKING, "Who are you?").also { stage++ }
            1 -> npcl(FaceAnim.NEUTRAL, "My name is Zandar Horfyre, and you ${player?.name} are trespassing in my tower, not to mention attacking my students! I thank you to leave immediately!").also { stage++ }
            2 -> showTopics(
                Topic("Ok, I was going anyway.", 3),
                Topic("No, I think I'll stay for a bit.", 4)
            )
            3 -> npcl(FaceAnim.NEUTRAL, "Good! And don't forget to close the door behind you!").also { stage = END_DIALOGUE }
            4 -> npcl(FaceAnim.ANNOYED, "Actually, that wasn't an invitation. I've tried being polite, now we'll do it the hard way!").also {
                teleport(player!!, Location(3217, 3177, 0), TeleportManager.TeleportType.INSTANT)
                stage = 5
            }
            5 -> player(FaceAnim.ANGRY, "Zamorak curse that mage!").also { stage++ }
            6 -> player(FaceAnim.LAUGH, "Actually, I guess he already has!").also { stage = END_DIALOGUE }
        }
    }
}