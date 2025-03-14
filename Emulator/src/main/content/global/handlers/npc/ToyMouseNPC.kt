package content.global.handlers.npc

import content.data.GameAttributes
import core.api.findLocalNPC
import core.api.sendMessage
import core.api.setAttribute
import core.game.interaction.InteractPlugin
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.NPCs

/*
 * TODO:
 *  Cats can chase these clockwork mice.
 *  If they catch them they will eat them and the clockwork mouse will be lost.
 *  In addition clockwork cats can chase clockwork mice, just like regular cats chase rats.
 */
@Initializable
class ToyMouseNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var ownerUID: Int = -1
    var clearTime = 0

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return ToyMouseNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TOY_MOUSE_3597)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (clearTime++ > 200) this.clear()
    }

    companion object {
        val mouseMap = mutableMapOf<Int, ToyMouseNPC>()

        @JvmStatic
        fun spawnToyMouse(player: Player) {
            val mouse = ToyMouseNPC(NPCs.TOY_MOUSE_3597)
            mouse.location = Location.getRandomLocation(player.location, 1, true)
            mouse.isRespawn = false
            mouse.isWalks = true
            mouse.isActive = false
            mouse.walkRadius = 3

            if (mouse.asNpc() != null && mouse.isActive) {
                mouse.properties.teleportLocation = mouse.properties.spawnLocation
                mouse.ownerUID = player.details.uid
                mouseMap[player.details.uid] = mouse
            }

            mouse.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(0, mouse) {
                    override fun pulse(): Boolean {
                        mouse.init()
                        sendMessage(player, "You wind up the mouse.")
                        setAttribute(player, GameAttributes.ITEM_TOY_MOUSE_RELEASE, player.details.uid)
                        return true
                    }
                },
            )
        }

        @JvmStatic
        fun removeMouse(
            player: Player,
            mouse: NPC,
        ) {
            val mouseInstance = mouseMap[player.details.uid]
            val m = findLocalNPC(player, NPCs.TOY_MOUSE_3597)
            mouse.clear()
            mouseMap.remove(player.details.uid)
            return
        }
    }

    override fun checkImpact(state: BattleState?) {
        super.checkImpact(state)
    }

    override fun getInteraction(): InteractPlugin {
        return super.getInteraction()
    }
}
