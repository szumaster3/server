package content.minigame.duelarena.handlers

import content.global.handlers.iface.warning.WarningManager
import content.global.handlers.iface.warning.Warnings
import content.global.skill.summoning.familiar.Familiar
import core.api.inBorders
import core.api.setAttribute
import core.cache.def.impl.SceneryDefinition
import core.game.container.Container
import core.game.container.impl.EquipmentContainer
import core.game.interaction.Option
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.impl.PulseManager
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.map.RegionManager.isTeleportPermitted
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.plugin.Plugin
import core.tools.RandomFunction
import core.tools.StringUtils
import java.util.*

class DuelArea
    @JvmOverloads
    constructor(
        index: Int = -1,
        val border: ZoneBorders? = null,
        val isObstacles: Boolean = false,
        val center: Location? = null,
    ) : MapZone(
            "Duel Area - $index",
            true,
            ZoneRestriction.FIRES,
            ZoneRestriction.CANNON,
            ZoneRestriction.RANDOM_EVENTS,
            ZoneRestriction.FOLLOWERS,
        ) {
        fun duel(session: DuelSession) {
            val locations = getStartLocations(session)
            session.player!!.teleport(locations[0])
            session.other!!.teleport(locations[1])
            session.player.interfaceManager.close()
            session.player.interfaceManager.restoreTabs()
            session.other.interfaceManager.close()
            session.other.interfaceManager.restoreTabs()
            session.player.setAttribute("duel:icon", HintIconManager.registerHintIcon(session.player, session.other))
            session.other.setAttribute("duel:icon", HintIconManager.registerHintIcon(session.other, session.player))
            session.player.setAttribute("duel:ammo", ArrayList<GroundItem>(100))
            session.other.setAttribute("duel:ammo", ArrayList<GroundItem>(100))
            session.player.setAttribute("vengeance", false)
            session.other.setAttribute("vengeance", false)
            Pulser.submit(
                object : Pulse(4, session.player, session.other) {
                    var count = 0

                    override fun pulse(): Boolean {
                        val chat = if (count < 3) (3 - count).toString() else "FIGHT!"
                        session.player.sendChat(chat)
                        session.other.sendChat(chat)
                        return count++ >= 3
                    }

                    override fun stop() {
                        super.stop()
                        if (session.player.isActive && session.other.isActive && session.fightState != 2) {
                            session.fightState = 1
                            session.player.skullManager.isSkullCheckDisabled = true
                            session.player.skullManager.isWilderness = true
                            session.other.skullManager.isSkullCheckDisabled = true
                            session.other.skullManager.isWilderness = true
                            session.player.properties.isMultiZone = true
                            session.other.properties.isMultiZone = true
                        }
                    }
                },
            )
        }

        override fun interact(
            e: Entity,
            target: Node,
            option: Option,
        ): Boolean {
            if (!e.isPlayer) {
                return true
            }
            val p = e.asPlayer()
            val session: DuelSession = getSession(p) ?: return true
            if (option.name.equals("eat", ignoreCase = true) && session.hasRule(DuelRule.NO_FOOD)) {
                p.sendMessage("You can't eat in this fight.")
                return true
            }
            if (option.name.equals("drink", ignoreCase = true) && session.hasRule(DuelRule.NO_DRINKS)) {
                p.sendMessage("You can't drink in this fight.")
                return true
            }
            if (option.name.equals("wield", ignoreCase = true) ||
                option.name.equals(
                    "wear",
                    ignoreCase = true,
                ) &&
                target is Item
            ) {
                if (session.isRestrictedEquipment(target.asItem())) {
                    p.sendMessage("You can't equip that during this duel.")
                    return true
                }
            }
            if (option.name.equals("Summon", ignoreCase = true) && !session.hasRule(DuelRule.ENABLE_SUMMONING)) {
                p.sendMessage("You cannot summon familiars whilst in a duel.")
                return true
            }
            if (option.name.equals("drop", ignoreCase = true) && target is Item) {
                p.sendMessage("You cannot drop items whilst in a duel.")
                return true
            }
            when (target.id) {
                3203 -> {
                    handleForfeit(p)
                    return true
                }
            }
            return super.interact(e, target, option)
        }

        override fun move(
            entity: Entity,
            from: Location,
            to: Location,
        ): Boolean {
            if (entity.isPlayer) {
                val p = entity.asPlayer()
                val session: DuelSession = getSession(p) ?: return false
                return !session.hasRule(DuelRule.NO_MOVEMENT)
            }
            return super.move(entity, from, to)
        }

        override fun actionButton(
            player: Player,
            interfaceId: Int,
            buttonId: Int,
            slot: Int,
            itemId: Int,
            opcode: Int,
        ): Boolean {
            val session: DuelSession = getSession(player) ?: return true
            val inter = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
            if (inter != null && interfaceId == inter.id) {
                when (buttonId) {
                    8, 10 ->
                        if (session.hasRule(DuelRule.NO_SPECIAL_ATTACKS)) {
                            player.sendMessage("You can't use special attacks during a duel.")
                            return true
                        }
                }
            }
            when (interfaceId) {
                182 -> {
                    player.sendMessage("You can't logout during a duel.")
                    return true
                }

                271 ->
                    if (session.hasRule(DuelRule.NO_PRAYER)) {
                        player.prayer.toggle(PrayerType.get(buttonId))
                        player.prayer.toggle(PrayerType.get(buttonId))
                        player.sendMessage("Use of prayer has been turned off for this duel.")
                        return true
                    }

                430 ->
                    if (session.hasRule(DuelRule.NO_MAGIC)) {
                        player.sendMessage("Use of prayer has been turned off for this duel.")
                        return true
                    }
            }
            return super.actionButton(player, interfaceId, buttonId, slot, itemId, opcode)
        }

        override fun continueAttack(
            e: Entity,
            target: Node,
            style: CombatStyle,
            message: Boolean,
        ): Boolean {
            if (e.isPlayer && target is Player && !checkAttack(e.asPlayer(), target)) {
                return false
            }
            if (e is Familiar && target is Player) {
                val o = e.owner
                if (o != null && target.asPlayer() !== getSession(o)!!.getOpposite(o)) {
                    return false
                }
            }
            if (e is Familiar && target is Familiar) {
                val f = e
                if (getSession(f.owner)!!.getOpposite(f.owner) !== target.owner) {
                    return false
                }
            }
            if (e.isPlayer) {
                val p = e.asPlayer()
                val session: DuelSession = getSession(p) ?: return false
                var canAttack = true
                canAttack =
                    when (style) {
                        CombatStyle.MAGIC -> !session.hasRule(DuelRule.NO_MAGIC)
                        CombatStyle.MELEE -> !session.hasRule(DuelRule.NO_MELEE)
                        CombatStyle.RANGE -> !session.hasRule(DuelRule.NO_RANGE)
                    }
                if (!canAttack) {
                    p.sendMessage(
                        StringUtils.formatDisplayName(style.name.lowercase(Locale.getDefault())) +
                            " has been turned off for this duel.",
                    )
                    return false
                }
                if (session.hasRule(DuelRule.FUN_WEAPONS) &&
                    (
                        p.equipment[EquipmentContainer.SLOT_WEAPON] == null ||
                            !p.equipment[EquipmentContainer.SLOT_WEAPON].definition.getConfiguration(
                                "fun_weapon",
                                false,
                            )
                    )
                ) {
                    p.sendMessages(
                        "This is a 'fun weapon' duel. You can only use flowers, basket of eggs, or a",
                        "rubber chicken.",
                    )
                    return false
                }
                if (target is content.global.skill.summoning.familiar.Familiar) {
                    val f = target
                    if (f.owner != null && getSession(p)!!.getOpposite(p) !== f.owner) {
                        p.sendMessage("You can't attack that familiar.")
                        return false
                    }
                }
            }
            return true
        }

        override fun teleport(
            e: Entity,
            type: Int,
            node: Node,
        ): Boolean {
            if (e.isPlayer) {
                e.asPlayer().dialogueInterpreter.sendDialogue("Coward! You can't teleport from a duel.")
            }
            return false
        }

        override fun enter(e: Entity): Boolean {
            if (e is Player) {
                val p = e.asPlayer()
                if (!Warnings.DUEL_ARENA.isDisabled && inBorders(p, 3312, 3233, 3313, 3236)) {
                    WarningManager.openWarning(
                        p,
                        Warnings.DUEL_ARENA,
                    )
                }
            }
            if (e.isPlayer) {
                getSession(e.asPlayer())
                e.properties.isSafeZone = true
                e.properties.spawnLocation = RandomFunction.getRandomElement<Location>(RESPAWN_LOCATIONS)
                e.asPlayer().interaction.remove(DuelArenaActivity.CHALLENGE_OPTION)
                e.asPlayer().interaction.set(FIGHT_OPTION)
            } else if (e is Familiar) {
                val f = e
                val o = f.owner
                if (o != null && !o.familiarManager.hasPet()) {
                    val s: DuelSession = getSession(f.owner)!!
                    if (s != null && s.hasRule(DuelRule.ENABLE_SUMMONING)) {
                        f.transform()
                    }
                }
                f.properties.isMultiZone = true
            }
            return super.enter(e)
        }

        @Suppress("deprecation")
        override fun leave(
            entity: Entity,
            logout: Boolean,
        ): Boolean {
            entity.properties.isSafeZone = false
            if (entity is Player) {
                val p = entity.asPlayer()
                if (logout) {
                    p.location = RandomFunction.getRandomElement(RESPAWN_LOCATIONS)
                }
                p.lock(1)
                leave(p)
                val session: DuelSession = getSession(p) ?: return true
                session.leave(
                    p,
                    if (logout) {
                        1
                    } else if (p.getAttribute("duel:forfeit", false)) {
                        0
                    } else {
                        2
                    },
                )
                remove(session.getOpposite(p))
                leave(session.getOpposite(p))
            } else if (entity is Familiar) {
                val familiar = entity
                if (familiar.isCombatFamiliar) {
                    familiar.reTransform()
                }
                familiar.properties.isMultiZone = false
            }
            return super.leave(entity, logout)
        }

        override fun configure() {
            register(border)
        }

        private fun leave(p: Player) {
            p.properties.isSafeZone = false
            if (p.getAttribute<Any?>("duel:ammo", null) != null) {
                val ammo = p.getAttribute<List<GroundItem>>("duel:ammo")
                val c = Container(40)
                for (item in ammo) {
                    if (item == null) {
                        continue
                    }
                    if (item.isActive && GroundItemManager.getItems().contains(item) && item.droppedBy(p)) {
                        GroundItemManager.destroy(item)
                        c.add(item)
                    }
                }
                p.inventory.addAll(c)
            }
            p.interaction.remove(FIGHT_OPTION)
            p.skullManager.isSkullCheckDisabled = false
            p.skullManager.isWilderness = false
            p.properties.spawnLocation = RandomFunction.getRandomElement(RESPAWN_LOCATIONS)
            p.properties.isMultiZone = false
            p.interaction.set(DuelArenaActivity.CHALLENGE_OPTION)
        }

        fun getStartLocations(session: DuelSession): Array<Location?> {
            var start: Location? = null
            val locations = arrayOfNulls<Location>(2)
            while (start == null) {
                start =
                    center!!.transform(
                        RandomFunction.random(10),
                        RandomFunction.random(if (session.hasRule(DuelRule.NO_MOVEMENT)) 6 else 10),
                        0,
                    )
                if (isValidLocation(start)) {
                    locations[if (locations[0] == null) 0 else 1] = start
                    if (session.hasRule(DuelRule.NO_MOVEMENT)) {
                        var l: Location? = null
                        if (!isValidLocation(start.transform(1, 0, 0).also { l = it })) {
                            if (!isValidLocation(start.transform(-1, 0, 0).also { l = it })) {
                                if (!isValidLocation(start.transform(1, 0, 0).also { l = it })) {
                                    if (!isValidLocation(start.transform(-1, 0, 0).also { l = it })) {
                                        locations[1] = start
                                        break
                                    }
                                }
                            }
                        }
                        start = l
                        locations[1] = l
                        break
                    }
                    start = if (locations[1] == null) null else start
                } else {
                    start = null
                }
            }
            return locations
        }

        override fun startDeath(
            entity: Entity,
            killer: Entity,
        ): Boolean {
            if (entity is Player) {
                val k =
                    if (killer is Player) {
                        killer.asPlayer()
                    } else if (killer is content.global.skill.summoning.familiar.Familiar) {
                        killer.owner
                    } else {
                        null
                    }
                if (k != null) {
                    k.impactHandler.disabledTicks = 10
                    k.getSkills().heal(100)
                    k.lock(5)
                }
            }
            return true
        }

        override fun death(
            e: Entity,
            killer: Entity,
        ): Boolean {
            if (e.isPlayer && (killer.isPlayer || killer is Familiar)) {
                val k = if (killer is Familiar) killer.owner else killer.asPlayer()
                if (k != null) {
                    k.getSkills().heal(100)
                    PulseManager.cancelDeathTask(e)
                }
            }
            return super.death(e, killer)
        }

        fun isValidLocation(location: Location?): Boolean {
            return isTeleportPermitted(location!!) && getObject(location) == null
        }

        fun checkAttack(
            player: Player,
            target: Player,
        ): Boolean {
            val session: DuelSession = getSession(player) ?: return false
            if (session.getOpposite(player) !== target) {
                player.sendMessage("You can only attack your opponent!")
                return false
            }
            if (session.fightState != 1) {
                player.sendMessage("You can't attack yet!")
                return false
            }
            return true
        }

        class ForfeitTrapdoorPlugin : OptionHandler() {
            @Throws(Throwable::class)
            override fun newInstance(arg: Any?): Plugin<Any> {
                SceneryDefinition.forId(3203).handlers["option:forfeit"] = this
                return this
            }

            override fun handle(
                player: Player,
                node: Node,
                option: String,
            ): Boolean {
                handleForfeit(player)
                return true
            }

            override fun getDestination(
                node: Node,
                n: Node,
            ): Location {
                var loc: Location? = null
                if (node is Player) {
                    val session: DuelSession = getSession(node.asPlayer())!!
                    if (session != null && session.hasRule(DuelRule.NO_MOVEMENT)) {
                        loc = node.getLocation()
                    }
                }
                return loc!!
            }
        }

        companion object {
            val RESPAWN_LOCATIONS =
                arrayOf(
                    Location.create(3371, 3275, 0),
                    Location.create(3365, 3276, 0),
                    Location.create(3366, 3274, 0),
                    Location.create(3369, 3274, 0),
                    Location.create(3372, 3275, 0),
                    Location.create(3372, 3266, 0),
                    Location.create(3371, 3269, 0),
                    Location.create(3376, 3270, 0),
                    Location.create(3376, 3273, 0),
                    Location.create(3377, 3275, 0),
                )

            private val FIGHT_OPTION =
                Option("Fight", 0).setHandler(
                    object : OptionHandler() {
                        override fun handle(
                            player: Player,
                            node: Node,
                            option: String,
                        ): Boolean {
                            player.properties.combatPulse.attack(node)
                            return true
                        }

                        override fun isWalk(): Boolean {
                            return false
                        }

                        @Throws(Throwable::class)
                        override fun newInstance(arg: Any?): Plugin<Any> {
                            return this
                        }

                        override fun isDelayed(player: Player): Boolean {
                            return false
                        }
                    },
                )

            private fun remove(player: Player) {
                HintIconManager.removeHintIcon(player, player.getAttribute("duel:icon", -1))
                if (player.getExtension<Any?>(DuelSession::class.java) != null) {
                    player.teleport(RandomFunction.getRandomElement(RESPAWN_LOCATIONS))
                }
            }

            fun handleForfeit(p: Player) {
                val session: DuelSession = getSession(p) ?: return
                if (session.fightState != 1) {
                    p.sendMessage("The duel has not started yet!")
                    return
                }
                if (session.hasRule(DuelRule.NO_FORFEIT)) {
                    p.dialogueInterpreter.sendDialogue("Forfeit has been turned off for this duel.")
                    return
                }
                p.dialogueInterpreter.sendOptions("Do you wish to forfeit?", "Yes", "No")
                p.dialogueInterpreter.addAction { player, buttonId ->
                    if (buttonId == 2) {
                        remove(player)
                        setAttribute(player!!, "duel:forfeit", true)
                    }
                }
            }

            fun getSession(player: Player): DuelSession? {
                val session = player.getExtension<DuelSession>(DuelSession::class.java)
                if (session == null) {
                    remove(player)
                }
                return session
            }
        }
    }
