package content.global.skill.slayer.dungeon

import content.global.skill.slayer.SlayerEquipmentFlags
import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Components

@Initializable
class SmokeDungeon :
    MapZone("smoke dungeon", true),
    Plugin<Any?> {
    companion object {
        private val CHATS = arrayOf("*choke*", "*cough*")
        private val playersInDungeon = mutableSetOf<Player>()
        private const val DELAY = 20

        private val pulse =
            object : Pulse(3) {
                override fun pulse(): Boolean {
                    playersInDungeon.removeIf { player ->
                        if (isPlayerAffected(player)) {
                            applyEffect(player)
                        }
                        !player.isActive
                    }
                    return playersInDungeon.isEmpty()
                }
            }

        private fun isPlayerAffected(player: Player): Boolean {
            return !player.interfaceManager.isOpened &&
                !player.interfaceManager.hasChatbox() &&
                !player.locks.isMovementLocked &&
                getDelay(player) < GameWorld.ticks &&
                !isProtected(player)
        }

        private fun applyEffect(player: Player) {
            setDelay(player)
            if (RandomFunction.random(2) == 1) {
                sendChat(player, CHATS.random())
                sendMessage(player, "The stagnant, smoky air chokes you.")
            }
            impact(player, 2, HitsplatType.NORMAL)
        }

        private fun setDelay(player: Player) {
            setAttribute(player, "smoke-delay", GameWorld.ticks + DELAY)
        }

        private fun getDelay(player: Player): Int {
            return player.getAttribute("smoke-delay", 0)
        }

        private fun isProtected(player: Player): Boolean {
            return SlayerEquipmentFlags.hasFaceMask(player)
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        ClassScanner.definePlugin(SmokeDungeonOptionHandler())
        ZoneBuilder.configure(this)
        return this
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            val player = e.asPlayer()
            setDelay(player)
            playersInDungeon.add(player)
            if (!pulse.isRunning) {
                pulse.restart()
                submitWorldPulse(pulse)
            }
            openOverlay(player, Components.ENAKH_SMOKE_OVERLAY_118)
        }
        return true
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (e is Player) {
            val player = e.asPlayer()
            closeOverlay(player)
            playersInDungeon.remove(player)
            removeAttribute(player, "smoke-delay")
        }
        return super.leave(e, logout)
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any?,
    ): Any? = this

    override fun configure() {
        register(ZoneBorders(3196, 9337, 3344, 9407))
        pulse.stop()
    }

    private class SmokeDungeonOptionHandler : OptionHandler() {
        override fun newInstance(arg: Any?): Plugin<Any?> {
            SceneryDefinition.forId(36002).handlers["option:climb-down"] = this
            SceneryDefinition.forId(6439).handlers["option:climb-up"] = this
            return this
        }

        override fun handle(
            player: Player,
            node: Node,
            option: String,
        ): Boolean {
            when (node.id) {
                36002 -> {
                    teleport(player, Location.create(3206, 9379, 0))
                    sendMessage(player, "You climb down the well.")
                }

                6439 -> {
                    teleport(player, Location.create(3310, 2963, 0))
                    sendMessage(player, "You climb up the rope.")
                }
            }
            return true
        }
    }
}
