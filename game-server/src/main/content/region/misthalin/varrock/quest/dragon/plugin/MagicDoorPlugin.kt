package content.region.misthalin.varrock.quest.dragon.plugin

import content.region.misthalin.varrock.quest.dragon.DragonSlayer
import core.api.getQuestStage
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Scenery

/**
 * A plugin that handles the magic door used in the dragon slayer.
 */
@Initializable
class MagicDoorPlugin : UseWithHandler(*IDS) {

    companion object {
        private val LOC = Location(3049, 9840, 0)
        private val IDS = intArrayOf(
            Items.LOBSTER_POT_301,
            Items.UNFIRED_BOWL_1791,
            Items.SILK_950,
            Items.WIZARDS_MIND_BOMB_1907
        )
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(Scenery.MAGIC_DOOR_25115, OBJECT_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val player = event.player
        if (getQuestStage(player, Quests.DRAGON_SLAYER) < 20) {
            return true
        }
        if (removeItem(player, event.usedItem)) {
            sendMessage(player, "You put ${event.usedItem.name.lowercase()} into the opening in the door.")
            val index = IDS.indexOf(event.usedItem.id).takeIf { it >= 0 } ?: 0
            player.savedData.questData.dragonSlayerItems[index] = true
            DragonSlayer.handleMagicDoor(player, false)
        }
        return true
    }

    override fun getDestination(player: Player, node: Node): Location = LOC
}
