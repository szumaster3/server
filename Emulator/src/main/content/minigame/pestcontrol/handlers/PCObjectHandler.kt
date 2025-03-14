package content.minigame.pestcontrol.handlers

import core.cache.def.impl.SceneryDefinition
import core.game.activity.ActivityManager
import core.game.bots.PvMBotsBuilder.Companion.createPestControlTestBot
import core.game.bots.PvMBotsBuilder.Companion.createPestControlTestBot2
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.update.flag.context.Animation
import core.plugin.Plugin
import org.rs.consts.Animations

class PCObjectHandler :
    OptionHandler(),
    InteractionListener {
    var PCnBotsSpawned: Boolean = false

    var PCiBotsSpawned: Boolean = false

    var playersJoined: ArrayList<String> = ArrayList(20)

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(14227).handlers["option:repair"] = this
        SceneryDefinition.forId(14228).handlers["option:repair"] = this
        SceneryDefinition.forId(14229).handlers["option:repair"] = this
        SceneryDefinition.forId(14230).handlers["option:repair"] = this
        SceneryDefinition.forId(14231).handlers["option:repair"] = this
        SceneryDefinition.forId(14232).handlers["option:repair"] = this

        SceneryDefinition.forId(14233).handlers["option:open"] = this
        SceneryDefinition.forId(14234).handlers["option:close"] = this
        SceneryDefinition.forId(14235).handlers["option:open"] = this
        SceneryDefinition.forId(14236).handlers["option:close"] = this
        SceneryDefinition.forId(14237).handlers["option:open"] = this
        SceneryDefinition.forId(14237).handlers["option:repair"] = this
        SceneryDefinition.forId(14238).handlers["option:close"] = this
        SceneryDefinition.forId(14238).handlers["option:repair"] = this
        SceneryDefinition.forId(14239).handlers["option:open"] = this
        SceneryDefinition.forId(14239).handlers["option:repair"] = this
        SceneryDefinition.forId(14240).handlers["option:close"] = this
        SceneryDefinition.forId(14240).handlers["option:repair"] = this
        SceneryDefinition.forId(14241).handlers["option:open"] = this
        SceneryDefinition.forId(14241).handlers["option:repair"] = this
        SceneryDefinition.forId(14242).handlers["option:close"] = this
        SceneryDefinition.forId(14242).handlers["option:repair"] = this
        SceneryDefinition.forId(14243).handlers["option:open"] = this
        SceneryDefinition.forId(14243).handlers["option:repair"] = this
        SceneryDefinition.forId(14244).handlers["option:close"] = this
        SceneryDefinition.forId(14244).handlers["option:repair"] = this
        SceneryDefinition.forId(14245).handlers["option:open"] = this
        SceneryDefinition.forId(14245).handlers["option:repair"] = this
        SceneryDefinition.forId(14246).handlers["option:close"] = this
        SceneryDefinition.forId(14246).handlers["option:repair"] = this
        SceneryDefinition.forId(14247).handlers["option:open"] = this
        SceneryDefinition.forId(14247).handlers["option:repair"] = this
        SceneryDefinition.forId(14248).handlers["option:close"] = this
        SceneryDefinition.forId(14248).handlers["option:repair"] = this

        SceneryDefinition.forId(14296).handlers["option:climb"] = this

        SceneryDefinition.forId(14315).handlers["option:cross"] = this
        SceneryDefinition.forId(25631).handlers["option:cross"] = this
        SceneryDefinition.forId(25632).handlers["option:cross"] = this
        return this
    }

    private fun startActivity(
        p: Player,
        name: String,
        destination: Location,
    ) {
        if (ActivityManager.start(p, name, false)) {
            p.packetDispatch.sendMessage("You board the lander.")
            p.properties.teleportLocation = destination
        }
    }

    override fun defineListeners() {
        on(GATES, IntType.SCENERY, "repair") { player, node ->
            val scenery = node as Scenery
            val session = player.getExtension<PestControlSession>(
                PestControlSession::class.java) ?: return@on true
            if (!scenery.isActive || !session.barricades.contains(scenery)) {
                return@on true
            }
            repair(player, session, node.asScenery(), node.id)
            return@on true
        }

        on(BARRICADES, IntType.SCENERY, "repair") { player, node ->
            val scenery = node as Scenery
            val session = player.getExtension<PestControlSession>(
                PestControlSession::class.java) ?: return@on true
            if (!scenery.isActive || !session.barricades.contains(scenery)) {
                return@on true
            }
            repair(player, session, scenery, scenery.id - (if (scenery.id < 14233) 3 else 4))
            return@on true
        }
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        var pestBotsAmount = 0
        var pestBots2Amount = 0
        val scenery = node as Scenery
        if (option == "cross") {
            if (player.familiarManager.hasFamiliar() && player.rights !== Rights.ADMINISTRATOR) {
                player.packetDispatch.sendMessage("You can't take a follower on the lander.")
                return true
            }
            when (scenery.id) {
                14315 -> {
                    if (!GameWorld.PCnBotsSpawned && !player.isArtificial) {
                        GameWorld.PCnBotsSpawned = true
                        pestBotsAmount = 0
                        while (pestBotsAmount <= 35) {
                            createPestControlTestBot(Location(2657, 2640))
                            pestBotsAmount++
                        }
                    }
                    if (!playersJoined.contains(player.username) && !player.isArtificial) {
                        playersJoined.add(player.username)
                    }

                    startActivity(player, "pest control novice", Location.create(2661, 2639, 0))
                    return true
                }

                25631 -> {
                    if (!GameWorld.PCiBotsSpawned && !player.isArtificial) {
                        GameWorld.PCiBotsSpawned = true
                        pestBots2Amount = 0
                        while (pestBots2Amount <= 50) {
                            createPestControlTestBot2(Location(2644, 2644))
                            pestBots2Amount++
                        }
                    }
                    if (!playersJoined.contains(player.username) && !player.isArtificial) {
                        playersJoined.add(player.username)
                    }

                    startActivity(player, "pest control intermediate", Location.create(2640, 2644, 0))
                    return true
                }

                25632 -> {
                    startActivity(player, "pest control veteran", Location.create(2634, 2653, 0))
                    return true
                }
            }
        }
        val session = player.getExtension<PestControlSession>(
            PestControlSession::class.java) ?: return true
        if (scenery.id == 14296) {
            handleTurretTower(player, session, scenery)
            return true
        }
        if (!scenery.isActive || !session.barricades.contains(scenery)) {
            return true
        }
        if (option == "repair") {
            repair(player, session, scenery, scenery.id - (if (scenery.id < 14233) 3 else 4))
            return true
        }
        if (scenery.id > 14232) {
            handleGates(player, session, scenery)
            return true
        }
        return false
    }

    private fun handleTurretTower(
        player: Player,
        session: PestControlSession,
        scenery: Scenery,
    ) {
        val x = scenery.location.localX
        val y = scenery.location.localY
        if (x == 45 && y == 41) {
            if (player.location.localX < x + 1) {
                player.properties.teleportLocation = session.region.baseLocation.transform(x + 1, 41, 0)
            } else {
                player.properties.teleportLocation = session.region.baseLocation.transform(x - 1, 41, 0)
            }
        } else if ((x == 42 || x == 23) && y == 26) {
            if (player.location.localY > 25) {
                player.properties.teleportLocation = session.region.baseLocation.transform(x, 25, 0)
            } else {
                player.properties.teleportLocation = session.region.baseLocation.transform(x, 27, 0)
            }
        } else if (x == 20 && y == 41) {
            if (player.location.localX > 19) {
                player.properties.teleportLocation = session.region.baseLocation.transform(x - 1, 41, 0)
            } else {
                player.properties.teleportLocation = session.region.baseLocation.transform(x + 1, 41, 0)
            }
        }
    }

    private fun repair(
        player: Player,
        session: PestControlSession,
        scenery: Scenery,
        newId: Int,
    ) {
        if (!player.inventory.contains(2347, 1)) {
            player.packetDispatch.sendMessage("You'll need a hammer to make any repairs!")
            return
        }
        if (!player.inventory.remove(Item(1511, 1))) {
            player.packetDispatch.sendMessage("You'll need some logs to make any repairs!")
            return
        }
        session.addZealGained(player, 5)
        player.animate(Animation.create(Animations.HITTING_WITH_HAMMER_UP_HIGH_AND_DOWN_LOW_3971))

        if (session.barricades.remove(scenery)) {
            val replacement = scenery.transform(newId, scenery.rotation, getObjectType(newId))
            session.barricades.add(replacement)
            SceneryBuilder.replace(scenery, replacement)
        }
    }

    companion object {
        val GATES =
            intArrayOf(
                14233,
                14234,
                14235,
                14236,
                14237,
                14238,
                14239,
                14240,
                14241,
                14242,
                14243,
                14244,
                14245,
                14246,
                14247,
                14248,
            )
        val BARRICADES = intArrayOf(14227, 14228, 14229, 14230, 14231, 14232)

        private fun handleGates(
            player: Player,
            session: PestControlSession,
            scenery: Scenery,
        ) {
            val open = (scenery.id % 2) != 0
            val second = getSecondDoor(scenery) ?: return
            if (scenery.id > 14240 || second.id > 14240) {
                player.packetDispatch.sendMessage("It's too damaged to be moved!")
                return
            }
            val rotation = getRotation(scenery)
            val dir = if (open) scenery.rotation else rotation
            val direction = Direction.get(if (!open) dir else ((3 + dir) % 4))
            if (session.barricades.contains(scenery) && session.barricades.contains(second)) {
                session.barricades.remove(scenery)
                session.barricades.remove(second)

                var l = scenery.location.transform(direction.stepX, direction.stepY, 0)
                var replacement =
                    Scenery(
                        scenery.id + (if (open) 1 else -1),
                        l,
                        0,
                        if (open) rotation else ((direction.toInteger() + 3) % 4),
                    )
                SceneryBuilder.replace(scenery, replacement)
                session.barricades.add(replacement)

                l = second.location.transform(direction.stepX, direction.stepY, 0)
                replacement =
                    Scenery(
                        second.id + (if (open) 1 else -1),
                        l,
                        0,
                        if (open) getRotation(second) else ((direction.toInteger() + 3) % 4),
                    )
                SceneryBuilder.replace(second, replacement)
                session.barricades.add(replacement)
            }
        }

        private fun getRotation(scenery: Scenery): Int {
            var id = scenery.id
            if (id > 14236) {
                id -= 4
            }
            when (id) {
                14233 -> return (scenery.rotation + 1) % 4
                14234 -> return scenery.rotation % 4
                14235 -> return (scenery.rotation + 3) % 4
                14236 -> return (scenery.rotation + 2) % 4
            }
            return 0
        }

        fun getSecondDoor(scenery: Scenery): Scenery? {
            val l = scenery.location
            var o: Scenery? = null
            if ((getObject(l.transform(-1, 0, 0)).also { o = it }) != null && o!!.name == scenery.name) {
                return o
            }
            if ((getObject(l.transform(1, 0, 0)).also { o = it }) != null && o!!.name == scenery.name) {
                return o
            }
            if ((getObject(l.transform(0, -1, 0)).also { o = it }) != null && o!!.name == scenery.name) {
                return o
            }
            if ((getObject(l.transform(0, 1, 0)).also { o = it }) != null && o!!.name == scenery.name) {
                return o
            }
            return null
        }

        private fun getObjectType(objectId: Int): Int {
            if (objectId == 14225 || objectId == 14226 || objectId == 14228 || objectId == 14229) {
                return 9
            }
            return 0
        }
    }
}
