package content.region.wilderness.plugin

import core.api.playAudio
import core.api.sendMessage
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayersBoundingBox
import core.game.world.map.RegionManager.getRegionChunk
import core.game.world.update.flag.chunk.GraphicUpdateFlag
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Sounds
import shared.consts.Scenery as Objects

@Initializable
class WildernessObeliskPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(Objects.OBELISK_14829).handlers["option:activate"] = this
        SceneryDefinition.forId(Objects.OBELISK_14826).handlers["option:activate"] = this
        SceneryDefinition.forId(Objects.OBELISK_14827).handlers["option:activate"] = this
        SceneryDefinition.forId(Objects.OBELISK_14828).handlers["option:activate"] = this
        SceneryDefinition.forId(Objects.OBELISK_14830).handlers["option:activate"] = this
        SceneryDefinition.forId(Objects.OBELISK_14831).handlers["option:activate"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val scenery = node as Scenery
        val stationObelisk = Obelisk.forLocation(player.location) ?: return false
        val base = stationObelisk.location

        val offsets = listOf(2 to 2, -2 to 2, -2 to -2, 2 to -2)

        offsets.forEach { (dx, dy) ->
            val origin = stationObelisk.location
            val from = Location.create(origin.x + dx, origin.y + dy, origin.z)
            val to   = Location.create(origin.x + dx, origin.y + dy, 0)

            SceneryBuilder.replace(
                Scenery(scenery.id, from),
                Scenery(Objects.OBELISK_14825, to),
                6
            )
        }

        playAudio(player, Sounds.WILDERNESS_TP_204)

        Pulser.submit(object : Pulse(6, player) {
            override fun pulse(): Boolean {
                if (delay == 1) {
                    for (x in base.x - 1..base.x + 1) {
                        for (y in base.y - 1..base.y + 1) {
                            val loc = Location.create(x, y, 0)
                            getRegionChunk(loc).flag(GraphicUpdateFlag(Graphics.create(342), loc))
                        }
                    }
                    return true
                }

                val possible = Obelisk.values().filter { it != stationObelisk }
                val newObelisk = possible.random()

                getLocalPlayersBoundingBox(base, 1, 1).forEach { p ->
                    sendMessage(p, "Ancient magic teleports you to a place within the wilderness!")

                    val offsetX = p.location.x - base.x
                    val offsetY = p.location.y - base.y

                    p.teleporter.send(
                        Location.create(
                            newObelisk.location.x + offsetX,
                            newObelisk.location.y + offsetY,
                            0
                        ),
                        TeleportType.OBELISK
                    )
                }

                delay = 1
                return false
            }
        })

        return true
    }

    /**
     * Enum representing various Obelisks in the game world.
     *
     * @property location The coordinates of the obelisk in the game world.
     */
    enum class Obelisk(val location: Location) {
        LEVEL_13(Location(3156, 3620, 0)),
        LEVEL_19(Location(3219, 3656, 0)),
        LEVEL_27(Location(3035, 3732, 0)),
        LEVEL_35(Location(3106, 3794, 0)),
        LEVEL_44(Location(2980, 3866, 0)),
        LEVEL_50(Location(3307, 3916, 0)),
        ;

        companion object {
            /**
             * Finds an Obelisk near the given location.
             *
             * @param location The location to check proximity against.
             * @return The Obelisk within 20 units distance of the location, or null if none found.
             */
            @JvmStatic
            fun forLocation(location: Location?): Obelisk? {
                for (obelisk in values()) {
                    if (obelisk.location.getDistance(location!!) <= 20) return obelisk
                }
                return null
            }
        }
    }
}
