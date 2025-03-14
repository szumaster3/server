package content.region.asgarnia.handlers.guilds.warriorsguild

import core.api.removeAttribute
import core.api.setAttribute
import core.api.setVarp
import core.cache.def.impl.SceneryDefinition
import core.game.container.impl.EquipmentContainer
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.lock.Lock
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction

@Initializable
class BarrelRoom :
    MapZone("wg barrel", true),
    Plugin<Any> {
    override fun configure() {
        super.register(ZoneBorders(2861, 3536, 2876, 3543))
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        pulse.stop()
        definePlugin(
            object : OptionHandler() {
                override fun newInstance(arg: Any?): Plugin<Any> {
                    SceneryDefinition.forId(15668).handlers["option:pick-up"] = this
                    return this
                }

                override fun handle(
                    player: Player,
                    node: Node,
                    option: String,
                ): Boolean {
                    if (player.settings.runEnergy < 5) {
                        player.dialogueInterpreter.sendDialogue("You're too exhausted to continue. Take a break.")
                        return true
                    }
                    val helmId = player.equipment.getNew(EquipmentContainer.SLOT_HAT).id
                    val currentBarrel = player.getAttribute("barrel_count", 0)
                    if (player.equipment[EquipmentContainer.SLOT_WEAPON] != null ||
                        player.equipment[EquipmentContainer.SLOT_SHIELD] != null ||
                        player.equipment[EquipmentContainer.SLOT_HANDS] != null ||
                        helmId != currentBarrel
                    ) {
                        player.dialogueInterpreter.sendDialogue(
                            "To balance kegs you will need your head and hands free!",
                        )
                        return true
                    }
                    var id = currentBarrel + 1
                    if (id < 8860) {
                        id = 8860
                    } else if (id > 8864) {
                        id = 8864
                    }
                    val barrelId = id
                    player.lock(5)
                    player.animate(Animation.create(4180))
                    val lock = Lock("You're too busy balancing barrels to do that!")
                    lock.lock()
                    player.locks.equipmentLock = lock
                    player.packetDispatch.sendMessage("You pick up the keg and balance it on your head carefully.")
                    Pulser.submit(
                        object : Pulse(3, player) {
                            override fun pulse(): Boolean {
                                player.equipment.replace(Item(barrelId), EquipmentContainer.SLOT_HAT)
                                player.appearance.setAnimations(Animation.create(4178))
                                player.appearance.standAnimation = 4179
                                player.appearance.sync()
                                setAttribute(player, "barrel_count", barrelId)
                                (node as Scenery).setChildIndex(player, 1)
                                if (!players.contains(player)) {
                                    player.walkingQueue.isRunDisabled = true
                                    players.add(player)
                                    if (!pulse.isRunning) {
                                        pulse.restart()
                                        pulse.start()
                                        Pulser.submit(pulse)
                                    }
                                }
                                return true
                            }
                        },
                    )
                    return true
                }
            },
        )
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (e is Player && e.getAttribute("barrel_count", 0) > 0) {
            players.remove(e)
            removeBarrels(e)
        }
        return super.leave(e, logout)
    }

    companion object {
        private val players: MutableList<Player> = ArrayList(20)

        private val pulse: Pulse =
            object : Pulse(5) {
                override fun pulse(): Boolean {
                    if (players.isEmpty()) {
                        return true
                    }
                    val it = players.iterator()
                    while (it.hasNext()) {
                        val player = it.next()
                        player.settings.updateRunEnergy(5.0)
                        if (player.locks.isMovementLocked) {
                            continue
                        }
                        val barrels = (player.getAttribute("barrel_count", 8860) - 8859)
                        val chance = (player.settings.runEnergy - (5 * barrels)).toInt()
                        if (RandomFunction.randomize(100) > chance) {
                            removeBarrels(player)
                            player.sendChat("Ouch!")
                            player.packetDispatch.sendMessage("Some of the barrels hit you on their way to the floor.")
                            player.impactHandler.manualHit(player, 1, HitsplatType.NORMAL)
                            player.visualize(Animation.create(4177), Graphics.create(689 - barrels))
                            it.remove()
                            continue
                        }
                        player.getSavedData().activityData.updateWarriorTokens(barrels)
                    }
                    return false
                }
            }

        private fun removeBarrels(player: Player) {
            if (player.locks.equipmentLock != null) {
                player.locks.equipmentLock.unlock()
            }
            removeAttribute(player, "barrel_count")
            player.walkingQueue.isRunDisabled = false
            player.equipment.replace(null, EquipmentContainer.SLOT_HAT)
            player.appearance.setAnimations()
            player.appearance.sync()
            setVarp(player, 793, 0)
        }
    }
}
