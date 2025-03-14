package content.minigame.fishingtrawler

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Plugin
import org.rs.consts.Components
import kotlin.math.ceil

class FishingTrawlerRewardInterface : ComponentPlugin() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.TRAWLER_REWARD_367, this)
        return this
    }

    override fun open(
        player: Player?,
        component: Component?,
    ) {
        super.open(player, component)
        val session: FishingTrawlerSession? = player?.getAttribute("ft-session", null)
        session ?: return

        val numRolls = ceil(session.fishAmount / session.players.size.toDouble()).toInt()
        player?.removeAttribute("ft-session")

        val loot = ArrayList<Item>()
        for (i in 0 until numRolls) {
        }
    }

    override fun handle(
        player: Player?,
        component: Component?,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        TODO("Not yet implemented")
    }
}
