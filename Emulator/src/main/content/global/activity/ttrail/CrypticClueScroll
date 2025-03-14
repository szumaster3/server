package content.global.activity.ttrail

import core.api.inInventory
import core.api.sendItemDialogue
import core.api.sendString
import core.game.global.action.DigAction
import core.game.global.action.DigSpadeHandler
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.Items

abstract class CrypticClueScroll(
    name: String?,
    clueId: Int,
    level: ClueLevel?,
    private val interfaceId: Int?,
    private val location: Location?,
    val clue: String?,
    private val objectId: Int?
) : ClueScrollPlugin(name, clueId, level, -1) {

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        if (e is Player) {
            val player = e
            if (target.id == objectId && option.name == "Search") {
                if (!inInventory(player, clueId, 1) || target.location != location) {
                    return false
                }
                reward(player)
                return true
            }
        }
        return super.interact(e, target, option)
    }

    override fun read(player: Player) {
        for (i in 1..8) {
            sendString(player, "", interfaceId, i)
        }
        val riddleText = clue ?: ""

        super.read(player)
        sendString(player, "<br><br><br><br><br>$riddleText", interfaceId, 1)
    }

    override fun configure() {
        if (location != null) {
            DigSpadeHandler.register(location, CrypticClueDigAction(this))
        }
        super.configure()
    }

    fun dig(player: Player) {
        reward(player)
        sendItemDialogue(player, Items.CASKET_405, "You've found a casket!")
    }

    class CrypticClueDigAction(private val clueScroll: CrypticClueScroll) : DigAction {
        override fun run(player: Player?) {
            if (player == null) return
            if (!inInventory(player, clueScroll.clueId, 1)) {
                return
            }

            clueScroll.dig(player)
        }
    }

    fun getLocation(): Location? = location
    fun getClue(): String? = clue
    fun getObjectId(): Int? = objectId
}
