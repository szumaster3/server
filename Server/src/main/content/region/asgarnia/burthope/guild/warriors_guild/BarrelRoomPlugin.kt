package content.region.asgarnia.burthope.guild.warriors_guild

import core.api.playAudio
import core.api.sendChat
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
import core.game.world.GameWorld
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.repository.Repository
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import shared.consts.NPCs
import shared.consts.Sounds

/**
 * Handles the Barrel room.
 * @author Emperor
 */
@Initializable
class BarrelRoomPlugin : MapZone("wg barrel", true), Plugin<Any> {

    override fun configure() {
        super.register(ZoneBorders(2861, 3536, 2876, 3543))
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        pulse.stop()

        ClassScanner.definePlugin(object : OptionHandler() {
            @Throws(Throwable::class)
            override fun newInstance(arg: Any?): Plugin<Any> {
                SceneryDefinition.forId(15668).handlers["option:pick-up"] = this
                return this
            }

            override fun handle(player: Player, node: Node, option: String): Boolean {
                if (player.settings.runEnergy < 5) {
                    player.dialogueInterpreter.sendDialogue("You're too exhausted to continue. Take a break.")
                    return true
                }

                val helmId = player.equipment.getNew(EquipmentContainer.SLOT_HAT).id
                val currentBarrel = player.getAttribute("barrel_count", 0)

                if (player.equipment.get(EquipmentContainer.SLOT_WEAPON) != null ||
                    player.equipment.get(EquipmentContainer.SLOT_SHIELD) != null ||
                    player.equipment.get(EquipmentContainer.SLOT_HANDS) != null ||
                    helmId != currentBarrel
                ) {
                    player.dialogueInterpreter.sendDialogue("To balance kegs you will need your head and hands free!")
                    return true
                }

                var id = currentBarrel + 1
                id = when {
                    id < 8860 -> 8860
                    id > 8864 -> 8864
                    else -> id
                }

                val barrelId = id
                player.lock(5)
                player.animate(Animation.create(4180))
                val lock = Lock("You're too busy balancing barrels to do that!")
                player.locks.equipmentLock = lock
                player.packetDispatch.sendMessage("You pick up the keg and balance it on your head carefully.")
                val jimmy = Repository.findNPC(NPCs.JIMMY_4298)
                val barrels = player.getAttribute("barrel_count", -1)
                when {
                    barrels in 1..3 -> sendChat(jimmy!!.asNpc(), "Ya got no chance o' beatin' me ${player.name}!")
                    else -> sendChat(jimmy!!.asNpc(), "No one's stronger than me, 'speshly not ${player.name}!")
                }
                GameWorld.Pulser.submit(object : Pulse(3, player) {
                    override fun pulse(): Boolean {
                        player.equipment.replace(Item(barrelId), EquipmentContainer.SLOT_HAT)
                        player.appearance.setAnimations(Animation(4178))
                        player.appearance.standAnimation = 4179
                        player.appearance.walkAnimation = 4178
                        player.appearance.runAnimation = 4178
                        player.appearance.sync()

                        player.setAttribute("barrel_count", barrelId)
                        (node as Scenery).setChildIndex(player, 1)

                        if (!players.contains(player)) {
                            player.walkingQueue.isRunDisabled = true
                            players.add(player)

                            if (!pulse.isRunning) {
                                pulse.restart()
                                pulse.start()
                                GameWorld.Pulser.submit(pulse)
                            }
                        }
                        return true
                    }
                })
                return true
            }
        })
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any?): Any? = null

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (e is Player && e.getAttribute("barrel_count", 0) > 0) {
            players.remove(e)
            removeBarrels(e)
        }
        return super.leave(e, logout)
    }

    companion object {
        /**
         * Barrel sound effects.
         */
        private fun getBarrelAudio(barrels: Int): Int {
            return when (barrels) {
                0,1 -> Sounds.WARGUILD_DROP_BARRELS_1920
                2 -> Sounds.WARGUILD_DROP_BARRELS_1_1921
                3 -> Sounds.WARGUILD_DROP_BARRELS_2_1922
                else -> Sounds.WARGUILD_DROP_BARRELS_1920
            }
        }

        /**
         * The players list.
         */
        private val players: MutableList<Player> = mutableListOf()

        /**
         * The pulse.
         */
        private val pulse: Pulse = object : Pulse(5) {
            override fun pulse(): Boolean {
                if (players.isEmpty()) {
                    return true
                }
                val it = players.iterator()
                while (it.hasNext()) {
                    val player = it.next()
                    player.settings.updateRunEnergy(5.0)

                    if (player.locks.isMovementLocked()) continue

                    val barrels = player.getAttribute("barrel_count", 8860) - 8859
                    val chance = (player.settings.runEnergy - (5 * barrels)).toInt()

                    if (RandomFunction.randomize(100) > chance) {
                        removeBarrels(player)
                        player.sendChat("Ouch!")
                        player.packetDispatch.sendMessage("Some of the barrels hit you on their way to the floor.")
                        player.impactHandler.manualHit(player, 1, HitsplatType.NORMAL)
                        player.visualize(Animation.create(4177), Graphics.create(689 - barrels))
                        playAudio(player, getBarrelAudio(barrels))
                        val jimmy = Repository.findNPC(NPCs.JIMMY_4298)
                        sendChat(jimmy!!.asNpc(), "Wow! That'sh bery impr....imp...impresh.... good ${player.name}! Equalsh my record!")
                        it.remove()
                        continue
                    }
                    player.savedData.activityData.updateWarriorTokens(barrels)
                }
                return false
            }
        }

        /**
         * Removes the barrels from the player's head.
         * @param player The player.
         */
        private fun removeBarrels(player: Player) {
            player.locks.equipmentLock?.unlock()
            player.removeAttribute("barrel_count")
            player.walkingQueue.isRunDisabled = false
            player.equipment.replace(null, EquipmentContainer.SLOT_HAT)

            val def = player.appearance
            player.appearance.standAnimation = def.standAnimation
            player.appearance.setAnimations(null)
            player.appearance.sync()
            setVarp(player, 793, 0)
        }
    }
}
