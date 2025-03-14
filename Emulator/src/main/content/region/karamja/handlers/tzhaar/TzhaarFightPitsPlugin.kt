package content.region.karamja.handlers.tzhaar

import core.api.setAttribute
import core.api.setVarp
import core.game.activity.ActivityPlugin
import core.game.component.Component
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.FaceAnim
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.forId
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class TzhaarFightPitsPlugin : ActivityPlugin("fight pits", false, true, true, ZoneRestriction.CANNON) {
    override fun continueAttack(
        e: Entity,
        target: Node,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        if (target is Player && !WAR_PLAYERS.contains(target)) {
            return false
        }
        if (minutes < 4 && e is Player) {
            e.packetDispatch.sendMessage("The fight hasn't started yet.")
        }
        return minutes > 3
    }

    fun removeFromBattle(e: Entity?) {
        if (WAR_PLAYERS.remove(e)) {
            if (WAR_PLAYERS.size < 2) {
                if (WAR_PLAYERS.isNotEmpty()) {
                    resetLastVictor()
                    lastVictor = WAR_PLAYERS[0]

                    if (lastVictor != null) {
                        lastVictor!!.achievementDiaryManager.finishTask(lastVictor!!, DiaryType.KARAMJA, 2, 0)
                        addTokkul(lastVictor)
                        lastVictor!!.appearance.skullIcon = SKULL_ID
                        lastVictor!!.updateAppearance()
                        lastVictor!!.packetDispatch.sendString("Current Champion: $championName", INTERFACE_ID, 0)
                        resetDamagePulse(lastVictor)
                    }
                    forId(9552).planes[0].npcs[0].setAttribute("fp_champion", championName)
                }
                minutes = 0
            }
            sendPlayersRemaining(WAR_PLAYERS.size - 1)
        }
        if (e is Player && !LOBBY_PLAYERS.contains(e)) {
            LOBBY_PLAYERS.add(e.also { player = it })
            player.skullManager.isSkullCheckDisabled = false
            player.skullManager.isWilderness = false
            player.interfaceManager.closeOverlay()
            player.interaction.remove(Option._P_ATTACK)
            if (player.appearance.skullIcon == SKULL_ID) {
                player.appearance.skullIcon = -1
                player.updateAppearance()
            }
        }
    }

    private fun resetLastVictor() {
        val npcs: List<NPC> = ArrayList(forId(9552).planes[0].npcs)
        for (n in npcs) {
            if (n.id == 2734 || n.id == 2739) {
                n.clear()
            }
        }
        if (lastVictor == null || !lastVictor!!.isActive) {
            return
        }
        if (lastVictor!!.appearance.skullIcon == SKULL_ID) {
            player.appearance.skullIcon = -1
            player.updateAppearance()
        }
    }

    override fun death(
        e: Entity,
        killer: Entity,
    ): Boolean {
        if (e is Player) {
            removeFromBattle(e)
        }
        return false
    }

    override fun start(
        player: Player,
        login: Boolean,
        vararg args: Any,
    ): Boolean {
        player.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 0, 8)
        if (!login) {
            setAttribute(player, "fight_pits", true)
            ForceMovement.run(player, Location.create(2399, 5177, 0), Location.create(2399, 5175, 0))
            return true
        }
        if (WAR_ZONE.insideBorder(player.location.x, player.location.y)) {
            player.properties.teleportLocation = Location.create(2399, 5177, 0)
        }
        return false
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            if (!e.getAttribute("fight_pits", false)) {
                return false
            }
            LOBBY_PLAYERS.add(e)
        }
        return super.enter(e)
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (WAR_PLAYERS.contains(e)) {
            removeFromBattle(e)
        }
        LOBBY_PLAYERS.remove(e)
        if (logout) {
            e.location = Location.create(2399, 5177, 0)
        }
        if (e is Player && e.also { player = it }.appearance.skullIcon == SKULL_ID) {
            player.appearance.skullIcon = -1
            player.updateAppearance()
        }
        return super.leave(e, logout)
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (target is Scenery) {
            val o = target
            if (o.id == 9369) {
                ForceMovement.run(e, Location.create(2399, 5175, 0), Location.create(2399, 5177, 0))
                return true
            }
            if (o.id == 9368) {
                if (WAR_PLAYERS.contains(e)) {
                    removeFromBattle(e)
                    ForceMovement.run(e, Location.create(2399, 5167, 0), Location.create(2399, 5169, 0))
                    return true
                }
                (e as Player).packetDispatch.sendMessage("This vent is too hot for you to pass!")
                return true
            }
        }
        return false
    }

    override fun getSpawnLocation(): Location {
        val r = RandomFunction.RANDOM
        val x = 2395 + r.nextInt(8)
        var y = 5170 + r.nextInt(5)
        if (x == 2399 && y == 5172) {
            y--
        }
        return Location.create(x, y, 0)
    }

    override fun configure() {
        register(ZoneBorders(2368, 5120, 2420, 5176))
        PULSE.start()
        Pulser.submit(PULSE)
    }

    @Throws(Throwable::class)
    override fun newInstance(p: Player): ActivityPlugin {
        return this
    }

    companion object {
        private val WAR_ZONE = ZoneBorders(2368, 5120, 2420, 5168)
        private val LOBBY_PLAYERS: MutableList<Player> = ArrayList(20)
        private val WAR_PLAYERS: MutableList<Player> = ArrayList(20)
        private const val INTERFACE_ID = Components.TZHAAR_FIGHTPIT_373
        private const val SKULL_ID = 1

        private var minutes = 0

        private var tokkulAmount = 0

        private var lastVictor: Player? = null
        private val PULSE: Pulse =
            object : Pulse(100) {
                override fun pulse(): Boolean {
                    if (++minutes == 3) {
                        startGameSession()
                    } else if (minutes == 4) {
                        sendDialogue("FIGHT!")
                    } else if ((minutes - 0) > 4) {
                        spawnWave()
                    }
                    return false
                }
            }

        private fun sendDialogue(string: String) {
            for (p in WAR_PLAYERS) {
                p.dialogueInterpreter.sendDialogues(NPCs.TZHAAR_MEJ_KAH_2618, FaceAnim.HALF_GUILTY, string)
            }
        }

        fun resetDamagePulse(e: Entity?) {
            val pl = e!!.getAttribute<Pulse?>("fp_pulse", null)
            if (pl != null) {
                pl.stop()
                e.removeAttribute("fp_pulse")
            }
        }

        private fun spawnWave() {
            val stage = (minutes - 4) / 5
            when (stage) {
                1 -> for (p in WAR_PLAYERS) {
                    var i = 0
                    while (i < 2 + ((minutes - 4) shr 2)) {
                        val n = NPC.create(NPCs.TZ_KIH_2734, zoneDestination)
                        n.init()
                        n.isAggressive = true
                        n.setDefaultBehavior()
                        n.properties.combatPulse.attack(p)
                        i++
                    }
                }

                2 -> for (p in WAR_PLAYERS) {
                    var i = 0
                    while (i < 2 + ((minutes - 4) shr 2)) {
                        val n = NPC.create(NPCs.TOK_XIL_2739, zoneDestination)
                        n.init()
                        n.properties.combatPulse.attack(p)
                        i++
                    }
                }

                3 -> for (p in WAR_PLAYERS) {
                    val pl: Pulse =
                        object : Pulse(1, p) {
                            var count: Int = 0

                            override fun pulse(): Boolean {
                                if (DeathTask.isDead(p)) {
                                    return true
                                }
                                p.impactHandler.manualHit(p, 1, HitsplatType.NORMAL)
                                p.impactHandler.manualHit(p, 1, HitsplatType.NORMAL)
                                return ++count == 100
                            }
                        }
                    p.setAttribute("fp_pulse", pl)
                    Pulser.submit(pl)
                }
            }
        }

        private fun startGameSession() {
            if (LOBBY_PLAYERS.size + WAR_PLAYERS.size < 2) {
                minutes = 2
                if (LOBBY_PLAYERS.size == 1) {
                    LOBBY_PLAYERS[0].packetDispatch.sendMessage("There's not enough players to start the game.")
                }
                return
            }
            tokkulAmount = 0
            var victor: String? = null
            if (lastVictor != null) {
                victor = "Current Champion: $championName"
            }
            val size = (LOBBY_PLAYERS.size + WAR_PLAYERS.size) - 1
            if (!WAR_PLAYERS.isEmpty()) {
                setVarp(WAR_PLAYERS[0], 560, size)
            }
            val it = LOBBY_PLAYERS.iterator()
            while (it.hasNext()) {
                val p = it.next()
                if (p != null && p.isActive) {
                    WAR_PLAYERS.add(p)
                    p.interfaceManager.openOverlay(Component(INTERFACE_ID))
                    if (victor != null) {
                        p.packetDispatch.sendString(victor, INTERFACE_ID, 0)
                    }
                    p.skullManager.isSkullCheckDisabled = true
                    p.skullManager.isWilderness = true
                    setVarp(p, 560, size)
                    p.properties.teleportLocation = zoneDestination
                    p.interaction.set(Option._P_ATTACK)
                    tokkulAmount += p.properties.currentCombatLevel
                }
                it.remove()
            }
            sendDialogue("Wait for my signal before fighting.")
        }

        private val championName: String
            get() {
                val strength = lastVictor!!.getSkills().getStaticLevel(Skills.STRENGTH)
                val defence = lastVictor!!.getSkills().getStaticLevel(Skills.DEFENCE)
                var count = 0
                for (i in 5..22) {
                    var skill: Int
                    if ((lastVictor!!.getSkills().getStaticLevel(i).also { skill = it }) > strength &&
                        skill > defence
                    ) {
                        if (++count == 5) {
                            return "JalYt-Hur-" + lastVictor!!.username
                        }
                    }
                }
                val skill = lastVictor!!.getSkills().highestCombatSkillId
                when (skill) {
                    Skills.ATTACK, Skills.STRENGTH -> return "JalYt-Ket-" + lastVictor!!.username
                    Skills.RANGE -> return "JalYt-Xil-" + lastVictor!!.username
                    Skills.MAGIC -> return "JalYt-Mej-" + lastVictor!!.username
                }
                return "JalYtHur-" + lastVictor!!.username
            }

        fun addTokkul(player: Player?) {
            var amount = tokkulAmount
            if (player!!.equipment.getNew(EquipmentContainer.SLOT_CAPE).id == Items.FIRE_CAPE_6570) {
                amount *= 2
            }
            if (amount > 0) {
                if (!player.inventory.add(Item(Items.TOKKUL_6529, amount))) {
                    player.packetDispatch.sendMessage("Your Tokkul reward was added to your bank.")
                    player.bank.add(Item(Items.TOKKUL_6529, amount))
                }
            }
        }

        private val zoneDestination: Location?
            get() {
                when (RandomFunction.randomize(5)) {
                    0 -> return Location.create(2384 + RandomFunction.random(29), 5133 + RandomFunction.random(4), 0)
                    1 -> return Location.create(2410 + RandomFunction.random(4), 5140 + RandomFunction.random(18), 0)
                    2 -> return Location.create(2392 + RandomFunction.random(11), 5141 + RandomFunction.random(26), 0)
                    3 -> return Location.create(2383 + RandomFunction.random(3), 5141 + RandomFunction.random(15), 0)
                    4 -> return Location.create(2392 + RandomFunction.random(12), 5145 + RandomFunction.random(20), 0)
                }
                return null
            }

        fun sendPlayersRemaining(value: Int) {
            for (p in WAR_PLAYERS) {
                setVarp(p, 560, value)
            }
        }
    }
}
