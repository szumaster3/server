package content.minigame.bountyhunter.handlers

import core.ServerConstants
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.ClassScanner.definePlugin
import core.plugin.Plugin
import org.rs.consts.Components
import org.rs.consts.Scenery

class OrbViewingInterface : ComponentPlugin() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.TZHAAR_CAMERA_374, this)
        ComponentDefinition.put(Components.CLANWARS_VIEWING_ORB_649, this)
        definePlugin(
            object : OptionHandler() {
                override fun newInstance(arg: Any?): Plugin<Any> {
                    SceneryDefinition.forId(Scenery.VIEWING_ORB_9391).handlers["option:look-into"] = this
                    SceneryDefinition.forId(Scenery.VIEWING_ORB_28194).handlers["option:look-into"] = this
                    SceneryDefinition.forId(Scenery.LOW_LEVEL_ORB_28209).handlers["option:view"] = this
                    SceneryDefinition.forId(Scenery.MID_LEVEL_ORB_28210).handlers["option:view"] = this
                    SceneryDefinition.forId(Scenery.HIGH_LEVEL_ORB_28211).handlers["option:view"] = this
                    return this
                }

                override fun handle(
                    player: Player,
                    node: Node,
                    option: String,
                ): Boolean {
                    var interfaceId = Components.CLANWARS_VIEWING_ORB_649
                    when (node.id) {
                        Scenery.VIEWING_ORB_9391 -> {
                            interfaceId = Components.TZHAAR_CAMERA_374
                            setAttribute(player, "viewing_orb", FIGHT_PITS)
                        }

                        Scenery.VIEWING_ORB_28194 -> setAttribute(player, "viewing_orb", CLAN_WARS)
                        Scenery.LOW_LEVEL_ORB_28209,
                        Scenery.MID_LEVEL_ORB_28210,
                        Scenery.HIGH_LEVEL_ORB_28211,
                        ->
                            setAttribute(
                                player,
                                "viewing_orb",
                                BOUNTY_HUNTER[
                                    node.id -
                                        28209,
                                ],
                            )
                    }
                    viewOrb(player, interfaceId)
                    return true
                }

                override fun getDestination(
                    node: Node,
                    n: Node,
                ): Location? {
                    when (n.id) {
                        Scenery.VIEWING_ORB_28194 -> return n.location.transform(1, 0, 0)
                    }
                    return null
                }
            },
        )
        return this
    }

    override fun handle(
        player: Player,
        component: Component,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int,
    ): Boolean {
        val locations = player.getAttribute<Array<Location>>("viewing-orb") ?: return false
        if (button != 5) {
            move(player, locations[15 - button])
        } else {
            stopViewing(player, true)
        }
        return true
    }

    private fun move(
        player: Player,
        location: Location,
    ) {
        lockInteractions(player, 100000000)
        player.properties.teleportLocation = location
        if (player.appearance.npcId == -1) {
            player.appearance.transformNPC(-2)
            player.appearance.sync()
        }
    }

    private fun viewOrb(
        player: Player,
        interfaceId: Int,
    ) {
        val component = Component(interfaceId).setCloseEvent(ViewCloseEvent())
        setAttribute(player, "view-location", player.location)
        player.interfaceManager.openSingleTab(component)
        player.pulseManager.run(
            object : Pulse(1, player) {
                override fun pulse(): Boolean {
                    return false
                }

                override fun stop() {
                    super.stop()
                    stopViewing(player, true)
                }
            },
        )
    }

    class ViewCloseEvent : CloseEvent {
        override fun close(
            player: Player,
            c: Component,
        ): Boolean {
            stopViewing(player, false)
            return true
        }
    }

    companion object {
        private val FIGHT_PITS =
            arrayOf(
                Location.create(2388, 5138, 0),
                Location.create(2411, 5137, 0),
                Location.create(2409, 5158, 0),
                Location.create(2384, 5157, 0),
                Location.create(2398, 5150, 0),
            )

        private val CLAN_WARS =
            arrayOf(
                Location.create(3277, 3725, 0),
                Location.create(3315, 3725, 0),
                Location.create(3316, 3829, 0),
                Location.create(3277, 3827, 0),
                Location.create(3296, 3776, 0),
            )

        private val BOUNTY_HUNTER =
            arrayOf(
                arrayOf(
                    Location.create(2752, 5695, 0),
                    Location.create(2816, 5695, 0),
                    Location.create(2783, 5783, 0),
                    Location.create(2826, 5785, 0),
                    Location.create(2784, 5727, 0),
                ),
                arrayOf(
                    Location.create(3008, 5695, 0),
                    Location.create(3072, 5695, 0),
                    Location.create(3039, 5783, 0),
                    Location.create(3082, 5785, 0),
                    Location.create(3040, 5727, 0),
                ),
                arrayOf(
                    Location.create(3264, 5695, 0),
                    Location.create(3328, 5695, 0),
                    Location.create(3295, 5783, 0),
                    Location.create(3338, 5785, 0),
                    Location.create(3296, 5727, 0),
                ),
            )

        private fun stopViewing(
            player: Player,
            close: Boolean,
        ) {
            if (close) {
                closeTabInterface(player)
            }
            removeAttribute(player, "viewing-orb")
            player.unlock()
            player.appearance.transformNPC(-1)
            player.appearance.sync()
            player.pulseManager.clear()
            player.properties.teleportLocation = getAttribute(player, "view-location", ServerConstants.HOME_LOCATION)
        }
    }
}
