package content.global.travel

import core.api.playAudio
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
import core.tools.RandomFunction
import org.rs.consts.Sounds

@Initializable
class WildernessObelisk : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(org.rs.consts.Scenery.OBELISK_14829).handlers["option:activate"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.OBELISK_14826).handlers["option:activate"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.OBELISK_14827).handlers["option:activate"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.OBELISK_14828).handlers["option:activate"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.OBELISK_14830).handlers["option:activate"] = this
        SceneryDefinition.forId(org.rs.consts.Scenery.OBELISK_14831).handlers["option:activate"] = this
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val scenery = node as Scenery
        val stationObelisk = Obelisk.forLocation(player.location) ?: return false

        for (i in 0..3) {
            var x = stationObelisk.location.x
            var y = stationObelisk.location.y
            val z = stationObelisk.location.z
            when (i) {
                0 -> {
                    x = x + 2
                    y = y + 2
                    SceneryBuilder.replace(
                        Scenery(scenery.id, Location.create(x, y, z)),
                        Scenery(org.rs.consts.Scenery.OBELISK_14825, Location.create(x, y, 0)),
                        6,
                    )
                }

                1 -> {
                    x = x - 2
                    y = y + 2
                    SceneryBuilder.replace(
                        Scenery(scenery.id, Location.create(x, y, z)),
                        Scenery(org.rs.consts.Scenery.OBELISK_14825, Location.create(x, y, 0)),
                        6,
                    )
                }

                2 -> {
                    x = x - 2
                    y = y - 2
                    SceneryBuilder.replace(
                        Scenery(scenery.id, Location.create(x, y, z)),
                        Scenery(org.rs.consts.Scenery.OBELISK_14825, Location.create(x, y, 0)),
                        6,
                    )
                }

                3 -> {
                    x = x + 2
                    y = y - 2
                    SceneryBuilder.replace(
                        Scenery(scenery.id, Location.create(x, y, z)),
                        Scenery(org.rs.consts.Scenery.OBELISK_14825, Location.create(x, y, 0)),
                        6,
                    )
                }
            }
        }
        playAudio(player, Sounds.WILDERNESS_TP_204)

        Pulser.submit(
            object : Pulse(6, player) {
                override fun pulse(): Boolean {
                    val center = stationObelisk.location
                    if (delay == 1) {
                        for (x in center.x - 1..center.x + 1) {
                            for (y in center.y - 1..center.y + 1) {
                                val l = Location.create(x, y, 0)
                                getRegionChunk(l).flag(GraphicUpdateFlag(Graphics.create(342), l))
                            }
                        }
                        return true
                    }

                    val newObelisks = Obelisk.values()
                    for (i in newObelisks.indices) {
                        if (newObelisks[i] == stationObelisk) {
                            newObelisks[i] = newObelisks[newObelisks.size - 1]
                            break
                        }
                    }
                    val index = RandomFunction.random(0, newObelisks.size - 1)

                    val newObelisk = newObelisks[index]

                    for (player in getLocalPlayersBoundingBox(center, 1, 1)) {
                        player.packetDispatch.sendMessage(
                            "Ancient magic teleports you to a place within the wilderness!",
                        )
                        val xOffset = player.location.x - center.x
                        val yOffset = player.location.y - center.y
                        player.teleporter.send(
                            Location.create(
                                newObelisk.location.x + xOffset,
                                newObelisk.location.y + yOffset,
                                0,
                            ),
                            TeleportType.OBELISK,
                            2,
                        )
                    }
                    super.setDelay(1)
                    return false
                }
            },
        )
        return true
    }

    enum class Obelisk(
        val location: Location,
    ) {
        LEVEL_13(Location(3156, 3620, 0)),

        LEVEL_19(Location(3219, 3656, 0)),

        LEVEL_27(Location(3035, 3732, 0)),

        LEVEL_35(Location(3106, 3794, 0)),

        LEVEL_44(Location(2980, 3866, 0)),

        LEVEL_50(Location(3307, 3916, 0)),
        ;

        companion object {
            @JvmStatic
            fun forLocation(location: Location?): Obelisk? {
                for (obelisk in values()) if (obelisk.location.getDistance(location) <= 20) return obelisk
                return null
            }
        }
    }
}
