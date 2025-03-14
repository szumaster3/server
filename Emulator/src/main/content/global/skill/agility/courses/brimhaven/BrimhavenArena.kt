package content.global.skill.agility.courses.brimhaven

import core.api.removeAttribute
import core.api.setAttribute
import core.api.setVarp
import core.game.component.Component
import core.game.global.action.ClimbActionHandler
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.MovementHook
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.RegionManager.forId
import core.game.world.map.RegionManager.getObject
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction

@Initializable
class BrimhavenArena :
    MapZone("Brimhaven agility arena", true),
    Plugin<Any?> {
    private fun setDispenser(player: Player) {
        if (!player.getAttribute("brim-tagged", false)) {
            setAttribute(player, "brim-tagcount", 0)
        }
        setAttribute(player, "brim-tagged", false)
        setVarp(player, 309, 0)
        var index = RandomFunction.randomize(DISPENSERS.size)
        if (index == player.getAttribute("brim-tag", -1)) {
            index = (index + 1) % DISPENSERS.size
        }
        val iconSlot = player.getAttribute("brim-icon", -1)
        if (iconSlot > -1) {
            HintIconManager.removeHintIcon(player, iconSlot)
        }
        setAttribute(player, "brim-tag", index)
        setAttribute(
            player,
            "brim-icon",
            HintIconManager.registerHeightHintIcon(
                player,
                DISPENSERS[index],
                50,
            ),
        )
    }

    override fun enter(entity: Entity): Boolean {
        if (entity is Player) {
            val player = entity
            player.interfaceManager.openOverlay(OVERLAY)
            setDispenser(player)
        }
        return super.enter(entity)
    }

    override fun leave(
        entity: Entity,
        logout: Boolean,
    ): Boolean {
        if (entity is Player && !logout) {
            val player = entity
            player.interfaceManager.closeOverlay()
            removeAttribute(player, "brim-tag")
            removeAttribute(player, "brim-tagcount")
            val iconSlot = player.getAttribute("brim-icon", -1)
            if (iconSlot > -1) {
                HintIconManager.removeHintIcon(player, iconSlot)
                removeAttribute(player, "brim-icon")
            }
        }
        return super.enter(entity)
    }

    override fun interact(
        entity: Entity,
        node: Node,
        option: Option,
    ): Boolean {
        if (node is Scenery) {
            val `object` = node
            if (`object`.id == 3610) {
                ClimbActionHandler.climb(entity as Player, Animation.create(828), `object`.location.transform(0, 0, 3))
                return true
            }
            if (`object`.id == 3608 || `object`.id == 3581) {
                val player = entity as Player
                if (`object`.location != DISPENSERS[player.getAttribute("brim-tag", 0)]) {
                    player.packetDispatch.sendMessage(
                        "Tag the pillar indicated by the yellow arrow to get an Agility ticket.",
                    )
                    return true
                }
                if (player.getAttribute("brim-tagged", false)) {
                    player.packetDispatch.sendMessage("You have already tagged this pillar.")
                    return true
                }
                setAttribute(player, "brim-tagged", true)
                val tags = player.getAttribute("brim-tagcount", 0) + 1
                setAttribute(player, "brim-tagcount", tags)
                setVarp(player, 309, 4)
                if (tags > 1) {
                    val amount = 1
                    if (!player.inventory.add(Item(TICKET.id, amount))) {
                        GroundItemManager.create(GroundItem(Item(TICKET.id, amount), player.location, player))
                    }
                    player.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 1, 0)
                    player.dialogueInterpreter.sendItemMessage(TICKET.id, "You have received an Agility Arena Ticket!")
                    player.packetDispatch.sendMessage("You have received an Agility Arena Ticket!")
                    return true
                }
                player.packetDispatch.sendMessage(
                    "You get tickets by tagging more than one pillar in a row, tag the next pillar!",
                )
                player.dialogueInterpreter.sendDialogue(
                    "You get tickets by tagging more than one pillar in a row. <col=C04000>Tag the",
                    "<col=C04000>next pillar for a ticket!</col>",
                )
                return true
            }
        }
        return false
    }

    override fun move(
        e: Entity,
        loc: Location,
        dest: Location,
    ): Boolean {
        if (!e.locks.isMovementLocked && e is Player) {
            val hook = LOCATION_TRAPS[loc]
            if (hook != null) {
                e.setDirection(Direction.getLogicalDirection(loc, dest))
                return hook.handle(e, loc)
            }
        }
        return super.move(e, loc, dest)
    }

    override fun locationUpdate(
        e: Entity,
        last: Location,
    ) {
        if (!e.locks.isMovementLocked && e is Player) {
            val hook = LOCATION_TRAPS[e.getLocation()]
            if (hook != null) {
                if (!hook.handle(e, e.getLocation())) {
                    if (e.getPulseManager().isMovingPulse) {
                        e.getPulseManager().clear()
                    }
                    e.getWalkingQueue().reset()
                }
            }
        }
    }

    override fun configure() {
        registerRegion(11157)
        val pad = PressurePad()
        LOCATION_TRAPS[Location.create(2800, 9579, 3)] = pad
        LOCATION_TRAPS[Location.create(2799, 9579, 3)] = pad
        LOCATION_TRAPS[Location.create(2800, 9557, 3)] = pad
        LOCATION_TRAPS[Location.create(2799, 9557, 3)] = pad
        LOCATION_TRAPS[Location.create(2772, 9585, 3)] = pad
        LOCATION_TRAPS[Location.create(2772, 9584, 3)] = pad
        val spikes = FloorSpikes()
        LOCATION_TRAPS[Location.create(2800, 9568, 3)] = spikes
        LOCATION_TRAPS[Location.create(2799, 9568, 3)] = spikes
        LOCATION_TRAPS[Location.create(2772, 9552, 3)] = spikes
        LOCATION_TRAPS[Location.create(2772, 9551, 3)] = spikes
        LOCATION_TRAPS[Location.create(2761, 9573, 3)] = spikes
        LOCATION_TRAPS[Location.create(2761, 9574, 3)] = spikes
        val dart = DartTrap()
        LOCATION_TRAPS[Location.create(2788, 9557, 3)] = dart
        LOCATION_TRAPS[Location.create(2789, 9557, 3)] = dart
        LOCATION_TRAPS[Location.create(2794, 9573, 3)] = dart
        LOCATION_TRAPS[Location.create(2794, 9574, 3)] = dart
        LOCATION_TRAPS[Location.create(2777, 9568, 3)] = dart
        LOCATION_TRAPS[Location.create(2778, 9568, 3)] = dart
        val blade = SpinningBlades()
        LOCATION_TRAPS[Location.create(2778, 9579, 3)] = blade
        LOCATION_TRAPS[Location.create(2783, 9574, 3)] = blade
        LOCATION_TRAPS[Location.create(2778, 9557, 3)] = blade
        val trap = BladeTrap()
        LOCATION_TRAPS[Location.create(2788, 9579, 3)] = trap
        LOCATION_TRAPS[Location.create(2789, 9579, 3)] = trap
        LOCATION_TRAPS[Location.create(2783, 9551, 3)] = trap
        LOCATION_TRAPS[Location.create(2783, 9552, 3)] = trap
        LOCATION_TRAPS[Location.create(2761, 9584, 3)] = trap
        LOCATION_TRAPS[Location.create(2761, 9585, 3)] = trap
        var index = 0
        var x = 2761
        while (x < 2815) {
            var y = 9546
            while (y < 9600) {
                if (x == 2805 && y == 9590) {
                    y += 11
                    continue
                }
                DISPENSERS[index++] = Location.create(x, y, 3)
                y += 11
            }
            x += 11
        }
        Pulser.submit(
            object : Pulse(1) {
                override fun pulse(): Boolean {
                    val r = forId(11157)
                    if (!r.isActive) {
                        return false
                    }
                    if (ticks % 100 == 0) {
                        for (plane in r.planes) {
                            for (player in plane.players) {
                                setDispenser(player)
                            }
                        }
                        handlePlankSwitching()
                    }
                    val ticks = 3
                    if (GameWorld.ticks % ticks == 0) {
                        sawBladeActive = !sawBladeActive
                        if (sawBladeActive) {
                            var `object` = getObject(3, 2788, 9579)
                            SceneryBuilder.replace(`object`, `object`!!.transform(3567, `object`.rotation, 10), ticks)
                            `object` = getObject(3, 2789, 9579)
                            SceneryBuilder.replace(`object`, `object`!!.transform(0), ticks)

                            `object` = getObject(3, 2783, 9551)
                            SceneryBuilder.replace(`object`, `object`!!.transform(3567, `object`.rotation, 10), ticks)
                            `object` = getObject(3, 2783, 9552)
                            SceneryBuilder.replace(`object`, `object`!!.transform(0), ticks)

                            `object` = getObject(3, 2761, 9584)
                            SceneryBuilder.replace(`object`, `object`!!.transform(3567, `object`.rotation, 10), ticks)
                            `object` = getObject(3, 2761, 9585)
                            SceneryBuilder.replace(`object`, `object`!!.transform(0), ticks)
                        }
                    }
                    return false
                }
            },
        )
    }

    private enum class PlankSet(
        var entrance: Array<Location>,
        var exit: Array<Location>,
    ) {
        FIRST(
            arrayOf(Location.create(2797, 9591, 3), Location.create(2797, 9590, 3), Location.create(2797, 9589, 3)),
            arrayOf(
                Location.create(2802, 9591, 3),
                Location.create(2802, 9590, 3),
                Location.create(2802, 9589, 3),
            ),
        ),

        SECOND(
            arrayOf(Location.create(2764, 9558, 3), Location.create(2764, 9557, 3), Location.create(2764, 9556, 3)),
            arrayOf(
                Location.create(2769, 9558, 3),
                Location.create(2769, 9557, 3),
                Location.create(2769, 9556, 3),
            ),
        ),
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?>? {
        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    companion object {
        private val LOCATION_TRAPS: MutableMap<Location, MovementHook> = HashMap()

        private val OVERLAY = Component(5)

        private val DISPENSERS = arrayOfNulls<Location>(24)

        private val TICKET = Item(2996)

        @JvmField
        var sawBladeActive: Boolean = false

        private fun handlePlankSwitching() {
            val available = BooleanArray(3)
            for (i in 0 until 1 + RandomFunction.randomize(2)) {
                available[RandomFunction.randomize(3)] = true
            }
            for (i in 0..2) {
                val avail = available[i]
                for (set in PlankSet.values()) {
                    val l = set.entrance[i]
                    for (x in 1 until set.exit[i].x - l.x) {
                        val `object` = getObject(l.transform(x, 0, 0))
                        if (`object` != null) {
                            SceneryBuilder.replace(
                                `object`,
                                `object`.transform(if (avail) 3573 else 3576),
                            )
                        }
                    }
                    getObject(set.entrance[i])!!.charge = if (avail) 1000 else 500
                    getObject(set.exit[i])!!.charge = if (avail) 1000 else 500
                }
            }
        }
    }
}
