package content.region.kandarin.ardougne.east.plugin

import core.GlobalStatistics
import core.api.registerMapZone
import core.api.sendDialogue
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.Option
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Scenery

/**
 * Handles interaction with a statistic signpost.
 */
@Initializable
class MarketSignpostPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(Scenery.SIGNPOST_31300).handlers["option:read"] = this
        registerActivity()
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        val totalSteals = GlobalStatistics.getDailyStealCakes()
        sendDialogue(player, "The knights and paladins of East Ardougne are keeping a watchful eye on the market stalls today. So far, a total of $totalSteals delicious cakes and loaves of bread have been stolen from the bakers' stalls.")
        // One of East Ardougne's bakers was slightly upset to find one of his delicious wares has been stolen from his stall today. Be vigilant.
        return true
    }

    private fun registerActivity() {
        val zone = object : MapZone("bustling area", true) {
            override fun interact(e: Entity?, target: Node?, option: Option?): Boolean {
                if (option?.name?.lowercase()?.contains("steal-from") == true && target?.id == Scenery.BAKER_S_STALL_34384) {
                    GlobalStatistics.incrementStealCakes()
                }
                return false
            }
        }
        registerMapZone(zone, ZoneBorders(2652, 3295, 2669, 3316))
    }
}
