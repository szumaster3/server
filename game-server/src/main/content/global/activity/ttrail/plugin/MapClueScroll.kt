package content.global.activity.ttrail.plugin

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScroll
import core.api.inInventory
import core.api.sendItemDialogue
import core.api.sendMessage
import core.game.global.action.DigAction
import core.game.global.action.DigSpadeHandler.register
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Items

/**
 * Represents an map clue scroll.
 * @author Vexia
 */
abstract class MapClueScroll(
    name: String?, clueId: Int, level: ClueLevel?, interfaceId: Int, val location: Location?, val `object`: Int, vararg borders: ZoneBorders?
) : ClueScroll(name, clueId, level, interfaceId, borders.filterNotNull().toTypedArray()) {

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        if (e is Player) {
            val player = e.asPlayer()
            if (target.id == `object` && option.name == "Search") {
                if (!inInventory(player, clueId, 1) || target.location != location) {
                    sendMessage(player, "Nothing interesting happens.")
                    return false
                }
                reward(player)
                return true
            }
        }
        return super.interact(e, target, option)
    }

    override fun configure() {
        if (location != null) {
            register(location, MapDigAction())
        }
        super.configure()
    }

    /**
     * Dig.
     *
     * @param player the player
     */
    open fun dig(player: Player?) {
        reward(player!!)
        sendItemDialogue(player, Items.CASKET_405, "You've found a casket!")
    }

    /**
     * The type Map dig action.
     */
    inner class MapDigAction : DigAction {
        override fun run(player: Player?) {
            if (!hasRequiredItems(player)) {
                sendMessage(player!!, "Nothing interesting happens.")
                return
            }
            dig(player)
        }
    }

    /**
     * Has required items boolean.
     *
     * @param player the player
     * @return the boolean
     */
    fun hasRequiredItems(player: Player?): Boolean {
        return player!!.inventory.contains(clueId, 1)
    }
}
