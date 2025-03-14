package content.minigame.bountyhunter.handlers

import core.api.ContainerListener
import core.api.removeAttribute
import core.api.sendString
import core.api.setAttribute
import core.api.setVarp
import core.game.activity.ActivityManager
import core.game.activity.ActivityPlugin
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.container.Container
import core.game.container.ContainerEvent
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.node.item.GroundItem
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.Point
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.game.world.map.zone.impl.MultiwayCombatZone
import core.game.world.map.zone.impl.WildernessZone
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Animations
import java.util.*

@Initializable
class BountyHunterActivity
    @JvmOverloads
    constructor(
        val type: CraterType = CraterType.LOW_LEVEL,
    ) : ActivityPlugin(
            "BH " + type.name.lowercase(Locale.getDefault()),
            false,
            false,
            false,
            ZoneRestriction.FOLLOWERS,
        ) {
        val players: MutableMap<Player, BountyEntry> = HashMap()
        private val waitingRoom: MutableList<Player> = ArrayList(20)
        private var waitingTime = 166
        private val waitRoomPulse: Pulse =
            object : Pulse(1) {
                override fun pulse(): Boolean {
                    val time = Integer.toString(Math.round(waitingTime-- * 0.6).toInt()) + " Sec"
                    for (player in waitingRoom) {
                        player.packetDispatch.sendString(time, 656, 10)
                    }
                    if (waitingTime == -1) {
                        val it = waitingRoom.iterator()
                        while (it.hasNext()) {
                            enterCrater(it.next())
                            it.remove()
                        }
                        return true
                    }
                    return false
                }

                override fun stop() {
                    super.stop()
                    waitingTime = 166
                }
            }
        private val gamePulse: Pulse =
            object : Pulse(10) {
                override fun pulse(): Boolean {
                    for (player in players.keys) {
                        val entry = players[player]
                        if (entry!!.target == null) {
                            findTarget(player)
                        }
                        entry.updatePenalty(player, false)
                    }
                    return false
                }
            }

        override fun register() {
            waitRoomPulse.stop()
            gamePulse.stop()
            if (type == CraterType.LOW_LEVEL) {
                val check = Location.create(3166, 3679, 0)
                for (border in WildernessZone.getInstance().borders) {
                    if (border.insideBorder(check)) {
                        border.addException(ZoneBorders(3140, 3653, 3149, 3670))
                        border.addException(ZoneBorders(3150, 3656, 3154, 3676))
                        border.addException(ZoneBorders(3155, 3661, 3164, 3686))
                        border.addException(ZoneBorders(3165, 3667, 3173, 3693))
                        border.addException(ZoneBorders(3174, 3673, 3192, 3705))
                        border.addException(ZoneBorders(3193, 3681, 3196, 3709))
                        break
                    }
                }
                definePlugin(
                    object : ComponentPlugin() {
                        @Throws(Throwable::class)
                        override fun newInstance(arg: Any?): Plugin<Any> {
                            ComponentDefinition.put(657, this)
                            return this
                        }

                        override fun handle(
                            player: Player,
                            component: Component?,
                            opcode: Int,
                            button: Int,
                            slot: Int,
                            itemId: Int,
                        ): Boolean {
                            if (button == 18) {
                                player.interfaceManager.close()
                                player.lock(1)
                                val activity =
                                    player.getExtension<BountyHunterActivity>(
                                        BountyHunterActivity::class.java,
                                    )
                                if (activity.players.isEmpty()) {
                                    activity.joinWaitingRoom(player)
                                    return true
                                }
                                activity.enterCrater(player)
                            }
                            return true
                        }
                    },
                )
                BHScoreBoard.init()
                definePlugin(BountyLocateSpell())
                definePlugin(BHOptionHandler())
                ActivityManager.register(BountyHunterActivity(CraterType.MID_LEVEL))
                ActivityManager.register(BountyHunterActivity(CraterType.HIGH_LEVEL))
            }
        }

        override fun start(
            player: Player,
            login: Boolean,
            vararg args: Any,
        ): Boolean {
            if (player.familiarManager.hasFamiliar()) {
                player.packetDispatch.sendMessage("You can't bring a follower into the crater.")
                return false
            }
            if (!type.canEnter(player)) {
                return false
            }
            player.addExtension(BountyHunterActivity::class.java, this)
            if (!login) {
                player.interfaceManager.open(Component(657))
            } else {
                enterCrater(player)
            }
            return true
        }

        override fun enter(e: Entity): Boolean {
            if (e is Player && type.zone.insideBorder(e.getLocation()) || e is Player) {
                if (e.getAttribute<Any?>("viewing_orb") != null) {
                    return super.enter(e)
                }
                val player = e
                for (i in 0 until type.ordinal) {
                    if (!player.musicPlayer.hasUnlockedIndex(517 + i)) {
                        player.musicPlayer.unlock(444 - i, false)
                    }
                }
                player.addExtension(BountyHunterActivity::class.java, this)
                val entry = BountyEntry()
                players[player] = entry
                player.interfaceManager.openOverlay(GAME_OVERLAY)
                var penalty: Int
                if (player.getAttribute("pickup_penalty", 0).also { penalty = it } != 0) {
                    setAttribute(player, "/save:pickup_penalty", ticks + penalty)
                }
                if (player.getAttribute("exit_penalty", 0).also { penalty = it } != 0) {
                    setAttribute(player, "/save:exit_penalty", ticks + penalty)
                    if (player.prayer[PrayerType.PROTECT_ITEMS]) {
                        player.prayer.toggle(PrayerType.PROTECT_ITEMS)
                    }
                }
                findTarget(player)
                entry.updatePenalty(player, true)
                player.interaction.set(Option._P_ATTACK)
                player.interaction.remove(Option._P_ASSIST)
                player.skullManager.isSkullCheckDisabled = true
                player.skullManager.isWilderness = true
                setAttribute(player, "bh_joined", ticks + 10)
                updateSkull(player)
                if (!gamePulse.isRunning) {
                    gamePulse.start()
                    Pulser.submit(gamePulse)
                }
            }
            return super.enter(e)
        }

        override fun leave(
            e: Entity,
            logout: Boolean,
        ): Boolean {
            if (e is Player) {
                val player = e
                player.removeExtension(BountyHunterActivity::class.java)
                if (waitingRoom.contains(player)) {
                    leaveWaitingRoom(player, logout)
                }
                val entry = players[player]
                entry?.let { leaveCrater(player, logout, it) }
            }
            return super.leave(e, logout)
        }

        override fun interact(
            e: Entity,
            target: Node,
            option: Option,
        ): Boolean {
            if (target is GroundItem && option.name == "take") {
                return actionButton(e as Player, 192, 19, -1, -1, 55)
            }
            if (target is Item && option.name == "drop") {
                if (target.value > 1000) {
                    (e as Player).packetDispatch.sendMessage("This item is too valuable to drop in the crater.")
                    return true
                }
            }
            return false
        }

        override fun ignoreMultiBoundaries(
            attacker: Entity,
            victim: Entity,
        ): Boolean {
            if (attacker is Player) {
                val entry = players[attacker]
                return entry != null && entry.target === victim
            }
            return false
        }

        @Suppress("deprecation")
        override fun actionButton(
            player: Player,
            interfaceId: Int,
            buttonId: Int,
            slot: Int,
            itemId: Int,
            opcode: Int,
        ): Boolean {
            if (interfaceId == 192 && buttonId == 19) {
                val entry = players[player]
                if (entry != null && player.getAttribute("pickup_penalty", 0) > ticks) {
                    player.packetDispatch.sendMessage(
                        "You should not be picking up items. Now you must wait before you can leave.",
                    )
                    removeAttribute(player, "pickup_penalty")
                    setAttribute(player, "/save:exit_penalty", ticks + 300)
                    entry.updatePenalty(player, true)
                    if (player.prayer[PrayerType.PROTECT_ITEMS]) {
                        player.prayer.toggle(PrayerType.PROTECT_ITEMS)
                    }
                }
            } else if (interfaceId == 271 && buttonId == 25) {
                if (player.getAttribute("exit_penalty", 0) > ticks) {
                    player.packetDispatch.sendMessage(
                        "You can't use the protect item prayer until your penalty has passed.",
                    )
                    setVarp(player, PrayerType.PROTECT_ITEMS.config, 0)
                    return true
                }
            }
            return false
        }

        override fun continueAttack(
            e: Entity,
            target: Node,
            style: CombatStyle,
            message: Boolean,
        ): Boolean {
            if (e is Player && target is Player) {
                if (target.getAttribute("bh_joined", -1) > ticks) {
                    e.packetDispatch.sendMessage(
                        "This player has only just entered and is temporarily invulnerable to attacks.",
                    )
                    return false
                }
                e.removeAttribute("bh_joined")
            }
            return true
        }

        private fun updateSkull(player: Player) {
            if (player.getAttribute<Any?>("value_listener") == null) {
                val listener: ContainerListener =
                    object : ContainerListener {
                        override fun update(
                            c: Container?,
                            event: ContainerEvent?,
                        ) {
                            refresh(c)
                        }

                        override fun refresh(c: Container?) {
                            updateSkull(player)
                        }
                    }
                setAttribute(player, "value_listener", listener)
                player.inventory.listeners.add(listener)
                player.equipment.listeners.add(listener)
            }
            var value: Long = 0
            for (item in player.inventory.toArray()) {
                if (item != null) {
                    value += item.value
                }
            }
            for (item in player.equipment.toArray()) {
                if (item != null) {
                    value += item.value
                }
            }
            var skull = 2
            if (value >= 0) {
                for (i in 0 until SKULL_VALUES.size - 1) {
                    if (value < SKULL_VALUES.get(i)) {
                        skull = 6 - i
                        break
                    }
                }
            }
            player.skullManager.isSkulled()
            player.appearance.skullIcon = skull
            player.appearance.sync()
        }

        fun enterCrater(player: Player) {
            val offset = RandomFunction.getRandomElement(EXIT_OFFSETS)
            val destination = Location.create(type.zone.southWestX + offset.x, type.zone.southWestY + offset.y, 0)
            player.properties.teleportLocation = destination
            player.animate(Animation.create(Animations.WALK_AND_APPEAR_7377))
        }

        fun leaveCrater(
            player: Player,
            logout: Boolean,
            entry: BountyEntry,
        ) {
            if (entry.hunter != null) {
                val other = players[entry.hunter]
                if (other != null) {
                    entry.hunter!!.packetDispatch.sendMessage(
                        "Your target has " + (if (logout) "logged out" else "left") +
                            ". You shall be found a new target.",
                    )
                    other.target = null
                    findTarget(entry.hunter!!)
                }
            }
            if (entry.target != null) {
                val other = players[entry.target]
                if (other != null) {
                    other.hunter = null
                }
            }
            player.hintIconManager.clear()
            players.remove(player)
            val listener = player.getAttribute<ContainerListener>("value_listener")
            if (listener != null) {
                player.inventory.listeners.remove(listener)
                player.equipment.listeners.remove(listener)
            }
            player.appearance.skullIcon = -1
            player.appearance.sync()
            player.interaction.remove(Option._P_ATTACK)
            player.interaction.set(Option._P_ASSIST)
            player.skullManager.isSkullCheckDisabled = false
            player.skullManager.isWilderness = false
            player.interfaceManager.closeOverlay()
            if (players.isEmpty()) {
                gamePulse.stop()
            }
            var penalty: Int
            if (player.getAttribute("pickup_penalty", 0).also { penalty = it } > ticks) {
                setAttribute(player, "/save:pickup_penalty", penalty - ticks)
            } else {
                removeAttribute(player, "pickup_penalty")
            }
            if (player.getAttribute("exit_penalty", 0).also { penalty = it } > ticks) {
                setAttribute(player, "/save:exit_penalty", penalty - ticks)
            } else {
                removeAttribute(player, "exit_penalty")
            }
            !player.skullManager.isSkulled()
        }

        private fun joinWaitingRoom(player: Player) {
            waitingRoom.add(player)
            player.properties.teleportLocation = type.roomLocation
            player.interfaceManager.openOverlay(WAITING_OVERLAY)
            sendString(player, "Players waiting (need $MINIMUM_PLAYERS):", 656, 6)
            updateWaitingRoomSize()
            if (waitingRoom.size == MINIMUM_PLAYERS) {
                val time = Math.round(waitingTime * 0.6).toInt().toString() + " Sec"
                for (p in waitingRoom) {
                    player.packetDispatch.sendString(time, 656, 10)
                    p.packetDispatch.sendInterfaceConfig(656, 9, false)
                    p.packetDispatch.sendInterfaceConfig(656, 8, false)
                }
                if (!waitRoomPulse.isRunning) {
                    waitRoomPulse.start()
                    Pulser.submit(waitRoomPulse)
                }
            } else if (waitingRoom.size > MINIMUM_PLAYERS) {
                player.packetDispatch.sendString(Math.round(waitingTime * 0.6).toInt().toString() + " Sec", 656, 10)
                player.packetDispatch.sendInterfaceConfig(656, 9, false)
                player.packetDispatch.sendInterfaceConfig(656, 8, false)
            }
        }

        @Suppress("deprecation")
        fun leaveWaitingRoom(
            player: Player,
            logout: Boolean,
        ) {
            waitingRoom.remove(player)
            if (waitingRoom.size < MINIMUM_PLAYERS && waitRoomPulse.isRunning) {
                waitRoomPulse.stop()
                for (p in waitingRoom) {
                    p.packetDispatch.sendInterfaceConfig(656, 9, true)
                    p.packetDispatch.sendInterfaceConfig(656, 8, true)
                }
            }
            updateWaitingRoomSize()
            player.properties.teleportLocation = type.exitLocation
            player.interfaceManager.closeOverlay()
            if (logout) {
                player.location = type.exitLocation
            }
        }

        private fun findTarget(player: Player) {
            var target: Player? = null
            var other: BountyEntry? = null
            var difference = 999
            for (p in players.keys) {
                if (p === player) {
                    continue
                }
                val entry = players[p]
                if (entry!!.hunter == null) {
                    val diff = Math.abs(player.properties.currentCombatLevel - p.properties.currentCombatLevel)
                    if (diff < difference) {
                        difference = diff
                        target = p
                        other = entry
                    }
                }
            }
            if (other != null) {
                other.hunter = player
                HintIconManager.registerHintIcon(player, target)
            } else {
                player.hintIconManager.clear()
            }
            val entry = players[player]
            entry!!.target = target
            entry.update(player)
        }

        private fun updateWaitingRoomSize() {
            val size = Integer.toString(waitingRoom.size)
            for (player in waitingRoom) {
                player.packetDispatch.sendString(size, 656, 7)
            }
        }

        override fun canLogout(player: Player): Boolean {
            if (player.getAttribute("exit_penalty", 0) > ticks) {
                player.packetDispatch.sendMessage("You can't logout until the exit penalty is over.")
                return false
            }
            return true
        }

        override fun death(
            e: Entity,
            killer: Entity,
        ): Boolean {
            if (e is Player) {
                val player = e
                val entry = players[player]
                if (entry != null) {
                    if (entry.hunter !== killer && killer is Player) {
                        handleRogueKill(killer, player, entry)
                    } else if (entry.hunter === killer) {
                        entry.hunter!!.packetDispatch.sendMessage(
                            "You killed " + player.username +
                                ". They were your target, so your Hunter PvP rating increases!",
                        )
                        entry.hunter!!
                            .getSavedData()
                            .activityData
                            .updateBountyHunterRate(1)
                        BHScoreBoard.hunters.check(entry.hunter!!)
                    } else if (entry.hunter != null) {
                        entry.hunter!!.packetDispatch.sendMessage(
                            "Your target has died. You shall be found a new target.",
                        )
                    }
                    if (entry.hunter != null) {
                        val other = players[entry.hunter]
                        if (other != null) {
                            other.target = null
                        }
                        entry.hunter = null
                    }
                    player.hintIconManager.clear()
                    if (player.getAttribute("pickup_penalty", 0) != 0) {
                        setAttribute(player, "pickup_penalty", ticks - 5)
                    }
                    if (player.getAttribute("exit_penalty", 0) != 0) {
                        setAttribute(player, "exit_penalty", ticks - 5)
                    }
                    entry.updatePenalty(e, true)
                }
            }
            return false
        }

        private fun handleRogueKill(
            player: Player,
            victim: Player,
            entry: BountyEntry,
        ) {
            player.packetDispatch.sendMessage(
                "You killed " + victim.username + ". They were not your target, so your Rogue PvP rating",
            )
            player.packetDispatch.sendMessage("increases!")
            player.packetDispatch.sendMessage(
                "This means you get the pick-up penalty: pick anything up and you can't leave!",
            )
            player.getSavedData().activityData.updateBountyRogueRate(1)
            BHScoreBoard.rogues.check(player)
            setAttribute(player, "/save:pickup_penalty", ticks + 300)
            entry.updatePenalty(player, true)
        }

        override fun teleport(
            e: Entity,
            type: Int,
            node: Node,
        ): Boolean {
            if (e is Player && type != -1) {
                e.packetDispatch.sendMessage("A magical force stops you from teleporting.")
                return false
            }
            return true
        }

        @Throws(Throwable::class)
        override fun newInstance(p: Player): ActivityPlugin {
            return this
        }

        override fun getSpawnLocation(): Location {
            return Location.create(3166, 3679, 0)
        }

        override fun configure() {
            registerRegion(6234)
            register(type.zone)
            val x = type.zone.southWestX
            val y = type.zone.southWestY
            MultiwayCombatZone.instance.register(ZoneBorders(x + 56, y + 40, x + 140, y + 140))
        }

        companion object {
            private val SKULL_VALUES =
                intArrayOf(
                    100000,
                    500000,
                    1100000,
                    2500000,
                    -1,
                )

            private val EXIT_OFFSETS =
                arrayOf(
                    Point(19, 58),
                    Point(24, 44),
                    Point(29, 34),
                    Point(36, 22),
                    Point(52, 17),
                    Point(14, 72),
                    Point(15, 85),
                    Point(17, 127),
                    Point(14, 133),
                    Point(19, 151),
                    Point(38, 163),
                    Point(49, 170),
                    Point(64, 20),
                    Point(84, 16),
                    Point(106, 18),
                    Point(69, 176),
                    Point(85, 176),
                    Point(127, 174),
                    Point(130, 21),
                    Point(138, 33),
                    Point(155, 48),
                    Point(163, 53),
                    Point(172, 60),
                    Point(174, 64),
                    Point(176, 106),
                    Point(178, 84),
                    Point(155, 169),
                    Point(162, 162),
                    Point(163, 153),
                    Point(173, 136),
                )

            private const val MINIMUM_PLAYERS = 2

            private val WAITING_OVERLAY = Component(656)

            private val GAME_OVERLAY = Component(653)
        }
    }
