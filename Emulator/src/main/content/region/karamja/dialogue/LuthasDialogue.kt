package content.region.karamja.dialogue

import core.api.getAttribute
import core.api.removeAttribute
import core.api.sendMessage
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LuthasDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (player.getSavedData().globalData.isLuthasTask()) {
            val current = player.getSavedData().globalData.getKaramjaBananas()
            if (current >= 10) {
                player(FaceAnim.FRIENDLY, "I've filled a crate with bananas.")
                stage = 20
                return true
            }
            npc(FaceAnim.HALF_ASKING, "Have you completed your task yet?")
            stage = 18
            return true
        }
        npc(FaceAnim.HAPPY, "Hello I'm Luthas, I run the banana plantation here.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Could you offer me employment on your plantation?",
                    "That customs officer is annoying isn't he?",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 ->
                        player(
                            FaceAnim.ASKING,
                            "Could you offer me employment on your plantation?",
                        ).also { stage = 10 }
                    2 -> player(FaceAnim.SUSPICIOUS, "That customs officer is annoying isn't she?").also { stage = 14 }
                }

            10 ->
                npc(
                    FaceAnim.HAPPY,
                    "Yes, I can sort something out. There's a crate ready to",
                    "be loaded onto the ship.",
                ).also { stage++ }

            11 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "You wouldn't believe the demand for bananas from",
                    "Wydin's shop over in Port Sarim. I think this is the",
                    "third crate I've shipped him this month..",
                ).also { stage++ }

            12 ->
                npc(
                    FaceAnim.HAPPY,
                    "If you could go fill it up with bananas, I'll pay you 30",
                    "gold.",
                ).also { stage++ }

            13 -> {
                end()
                player.getSavedData().globalData.setLuthasTask(true)
            }

            14 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Well I know her pretty well. She doesn't cause me any",
                    "trouble any more",
                ).also { stage++ }

            15 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "She doesn't even search my export crates anymore.",
                    "She knows they only contain bananas.",
                ).also { stage++ }

            16 ->
                player(
                    FaceAnim.SUSPICIOUS,
                    "Really? How interesting. Whereabouts do you send",
                    "those to?",
                ).also { stage++ }

            17 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "There is a little shop over in Port Sarim that buys",
                    "them up by the crate. I believe it is run by a man",
                    "called Wydin.",
                ).also { stage = END_DIALOGUE }

            18 -> {
                val amt = player.getSavedData().globalData.getKaramjaBananas()
                if (amt < 30) {
                    player(FaceAnim.HALF_GUILTY, "No, the crate isn't full yet.").also { stage++ }
                } else {
                    end()
                }
            }

            19 -> npc(FaceAnim.NEUTRAL, "Well come back when it is.").also { stage = 15 }
            20 -> npc(FaceAnim.HAPPY, "Well done, here's your payment.").also { stage++ }
            21 -> {
                end()
                sendMessage(player, "Luthas hands you 30 coins.")
                player.getSavedData().globalData.setKaramjaBannanas(0)
                player.getSavedData().globalData.setLuthasTask(false)
                if (getAttribute(player, "stashed-rum", false)) {
                    removeAttribute(player, "stashed-rum")
                    setAttribute(player, "/save:wydin-rum", true)
                }
                if (!player.inventory.add(Item(995, 30))) {
                    GroundItemManager.create(GroundItem(Item(995, 30), player.location, player))
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LUTHAS_379)
    }
}
