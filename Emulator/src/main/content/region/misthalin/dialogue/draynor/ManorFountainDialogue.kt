package content.region.misthalin.dialogue.draynor

import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items

@Initializable
class ManorFountainDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (player.getAttribute("pressure-gauge", false) && player.inventory.containsItem(PRESSURE_GAUGE)) {
            player("It's full of dead fish!")
            stage = 5
            return true
        }
        player("There seems to be a pressure gauge in here...")
        stage = if (player.getAttribute("piranhas-killed", false)) 3 else 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 -> {
                player.impactHandler.manualHit(player, 1, HitsplatType.NORMAL)
                player.sendChat("Ow!")
                player("... and a lot of piranhas!", "I can't get the guage out.")
                stage = 2
            }

            2 -> end()
            3 -> {
                player("... and a lot of dead fish.")
                stage = 4
            }

            4 -> {
                if (!player.inventory.add(PRESSURE_GAUGE)) {
                    GroundItemManager.create(PRESSURE_GAUGE, player)
                }
                player.packetDispatch.sendMessage("You get the pressure gauge from the fountain.")
                setAttribute(player, "/save:pressure-gauge", true)
                end()
            }

            5 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(DIALOGUE_ID)
    }

    companion object {
        const val DIALOGUE_ID = 3954922
        private val PRESSURE_GAUGE = Item(Items.PRESSURE_GAUGE_271)
    }
}
