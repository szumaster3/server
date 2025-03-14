package content.region.misthalin.quest.haunted.handlers

import core.api.sendMessage
import core.api.setVarp
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.DoorActionHandler.getSecondDoor
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import java.awt.Point

@Initializable
class ErnestTheChickenHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(org.rs.consts.Scenery.DOOR_11450).handlers["option:open"] = this
        for (lever in Lever.values()) {
            for (i in lever.objectIds) {
                SceneryDefinition.forId(i).handlers["option:pull"] = this
                SceneryDefinition.forId(i).handlers["option:inspect"] = this
            }
        }
        ZoneBuilder.configure(LeverZone())
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val scenery = (node as Scenery)
        val extension = LeverCacheExtension.extend(player)
        var lever = Lever.forObject(scenery.id)
        when (option) {
            "pull" -> {
                lever = Lever.forObject(scenery.id)
                extension.pull(lever!!, scenery)
            }

            "inspect" -> extension.inspect(lever!!)
            "open" -> {
                extension.walk(scenery)
                return true
            }
        }
        return true
    }

    class LeverCacheExtension(
        private val player: Player,
    ) {
        private val levers = BooleanArray(Lever.values().size)

        fun pull(
            lever: Lever,
            scenery: Scenery,
        ) {
            val isUp = isUp(lever)
            levers[lever.ordinal] = !isUp
            player.animate(if (isUp) DOWN_ANIMATION else UP_ANIMATION)
            Pulser.submit(
                object : Pulse(1) {
                    override fun pulse(): Boolean {
                        updateConfigs()
                        sendMessage(
                            player,
                            "You pull lever ${lever.name.replace("LEVER_", "")} ${if (isUp) "down" else "up"}.",
                        )
                        sendMessage(player, "You hear a clunk.")
                        return true
                    }
                },
            )
        }

        fun inspect(lever: Lever) {
            sendMessage(player, "The lever is ${if (isUp(lever)) "up" else "down"}.")
        }

        fun walk(scenery: Scenery) {
            player.lock(4)
            Pulser.submit(
                object : Pulse(1, player, scenery) {
                    override fun pulse(): Boolean {
                        val p = walkData[0] as Point
                        val rotation = walkData[1] as IntArray
                        val destination = walkData[2] as Location
                        val opened = scenery.transform(scenery.id, rotation[0])
                        opened.charge = 88
                        opened.location = scenery.location.transform(p.getX().toInt(), p.getY().toInt(), 0)
                        val second = getSecondDoor(scenery, player)
                        SceneryBuilder.replace(scenery, opened, 2)
                        if (second != null) {
                            val secondOpened = second.transform(second.id, rotation[1])
                            secondOpened.charge = 88
                            secondOpened.location = second.location.transform(p.getX().toInt(), p.getY().toInt(), 0)
                            SceneryBuilder.replace(second, secondOpened, 2)
                        }
                        content.global.skill.agility.AgilityHandler.walk(
                            player,
                            0,
                            player.location,
                            destination,
                            null,
                            0.0,
                            null,
                        )
                        return true
                    }
                },
            )
        }

        fun updateConfigs() {
            setVarp(player, LEVER_CONFIG, calculateLeverConfig())
            setVarp(player, DOOR_CONFIG, calculateDoorConfig())
            save()
        }

        fun save() {
            levers.forEachIndexed { index, isUp ->
                if (!isUp) player.getSavedData().questData.draynorLevers[index] = false
            }
        }

        fun read() {
            player.getSavedData().questData.draynorLevers.forEachIndexed { index, isUp ->
                levers[index] = isUp
            }
            updateConfigs()
        }

        private fun calculateLeverConfig(): Int {
            return levers.foldIndexed(0) { index, acc, isUp ->
                if (!isUp) acc + (1 shl (index + 1)) else acc
            }
        }

        private fun calculateDoorConfig(): Int {
            val downCount = downCount()
            return when {
                downCount == 0 -> 0
                downCount == 1 && !isUp(Lever.LEVER_B) -> if (isUp(Lever.LEVER_A)) 4 else 328
                downCount == 2 && !isUp(Lever.LEVER_D) -> if (isUp(Lever.LEVER_E)) 290 else 64
                downCount == 3 && !isUp(Lever.LEVER_C) -> if (isUp(Lever.LEVER_A)) 306 else 64
                downCount == 4 && !isUp(Lever.LEVER_D) -> 264
                downCount == 5 && !isUp(Lever.LEVER_A) -> 304
                else -> 0
            }
        }

        val walkData: Array<Any>
            get() {
                val x = player.location.x
                val y = player.location.y
                return when {
                    x == 3108 && y == 9757 -> arrayOf(Point(-1, 0), intArrayOf(2, 0), Location.create(3108, 9759, 0))
                    x == 3108 && y == 9759 -> arrayOf(Point(-1, 0), intArrayOf(2, 0), Location.create(3108, 9757, 0))
                    x == 3106 && y == 9760 -> arrayOf(Point(0, 1), intArrayOf(3, 0), Location.create(3104, 9760, 0))
                    x == 3104 && y == 9760 -> arrayOf(Point(0, 1), intArrayOf(3, 0), Location.create(3106, 9760, 0))
                    x == 3104 && y == 9765 -> arrayOf(Point(0, 1), intArrayOf(3, 0), Location.create(3106, 9765, 0))
                    x == 3106 && y == 9765 -> arrayOf(Point(0, 1), intArrayOf(3, 0), Location.create(3104, 9765, 0))
                    x == 3102 && y == 9764 -> arrayOf(Point(-1, 0), intArrayOf(2, 0), Location.create(3102, 9762, 0))
                    x == 3102 && y == 9762 -> arrayOf(Point(-1, 0), intArrayOf(2, 0), Location.create(3102, 9764, 0))
                    else -> arrayOf(Point(0, 0), intArrayOf(0, 0), player.location)
                }
            }

        fun reset() {
            levers.fill(true)
            player
                .getSavedData()
                .questData.draynorLevers
                .fill(true)
            updateConfigs()
        }

        fun downCount() = levers.count { !it }

        fun isUp(lever: Lever) = levers[lever.ordinal]

        companion object {
            private const val LEVER_CONFIG = 33
            private const val DOOR_CONFIG = 668

            fun extend(player: Player): LeverCacheExtension {
                return player.getExtension(LeverCacheExtension::class.java) ?: LeverCacheExtension(player).apply {
                    player.addExtension(LeverCacheExtension::class.java, this)
                }
            }
        }
    }

    inner class LeverZone : MapZone("Draynor lever zone", true) {
        override fun configure() {
            registerRegion(12440)
        }

        override fun enter(entity: Entity): Boolean {
            if (entity is Player) {
                LeverCacheExtension.extend(entity).read()
            }
            return super.enter(entity)
        }

        override fun leave(
            entity: Entity,
            logout: Boolean,
        ): Boolean {
            if (entity is Player) {
                LeverCacheExtension.extend(entity).save()
            }
            return super.leave(entity, logout)
        }
    }

    enum class Lever(
        vararg val objectIds: Int,
    ) {
        LEVER_A(org.rs.consts.Scenery.LEVER_A_11451, org.rs.consts.Scenery.LEVER_A_11452),
        LEVER_B(org.rs.consts.Scenery.LEVER_B_11453, org.rs.consts.Scenery.LEVER_B_11454),
        LEVER_C(org.rs.consts.Scenery.LEVER_C_11455, org.rs.consts.Scenery.LEVER_C_11456),
        LEVER_D(org.rs.consts.Scenery.LEVER_D_11457, org.rs.consts.Scenery.LEVER_D_11458),
        LEVER_E(org.rs.consts.Scenery.LEVER_E_11459, org.rs.consts.Scenery.LEVER_E_11460),
        LEVER_F(org.rs.consts.Scenery.LEVER_F_11461, org.rs.consts.Scenery.LEVER_F_11462),
        ;

        companion object {
            fun forObject(objectId: Int): Lever? {
                return values().firstOrNull { lever -> lever.objectIds.contains(objectId) }
            }
        }
    }

    override fun getDestination(
        node: Node,
        n: Node,
    ): Location? {
        if (n is Scenery) {
            val player = node as? Player ?: return null
            return when (n.id) {
                11450 -> Location.create(3109, 3353, 0)
                11451, 11452, 11453, 11454, 11455, 11456, 11457, 11458, 11459, 11460, 11461, 11462 ->
                    LeverCacheExtension.extend(
                        player,
                    ).walkData[2] as Location

                else -> null
            }
        }
        return null
    }

    companion object {
        private val UP_ANIMATION = Animation(Animations.PULL_DOWN_LEVER_2140)
        private val DOWN_ANIMATION = Animation(Animations.PULL_LEVER_2141)
    }
}
