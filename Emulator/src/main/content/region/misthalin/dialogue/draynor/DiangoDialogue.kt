package content.region.misthalin.dialogue.draynor

import content.global.handlers.iface.DiangoReclaimInterface
import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class DiangoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.HAPPY,
            "Howdy there, partner! Want to see my spinning plates?",
            "Or did ya want a holiday item back?",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Spinning plates?",
                    "I'd like to check holiday items please!",
                    "I'd like to claim purchased cosmetics.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> player("Spinning plates?").also { stage = 10 }
                    2 -> player("I'd like to check holiday items please?").also { stage = 20 }
                    3 -> player("I'd like to claim purchased cosmetics.").also { stage = 30 }
                }

            10 ->
                npc(
                    FaceAnim.LAUGH,
                    "That's right. There's a funny story behind them, their",
                    "shipment was held up by thieves",
                ).also { stage++ }

            11 ->
                npc(
                    FaceAnim.LAUGH,
                    "The crate was marked 'Dragon Plates'.",
                    "Apparently they thought it was some kind of armour,",
                    "when really it's just a plate!",
                ).also { stage++ }

            12 -> {
                end()
                openNpcShop(player, NPCs.DIANGO_970)
            }

            20 -> npc("Sure thing, let me just see what you're missing.").also { stage++ }
            21 -> {
                DiangoReclaimInterface.open(player)
                end()
            }

            30 -> {
                end()
                DiangoReclaimInterface.open(player)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return DiangoDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DIANGO_970)
    }
}
