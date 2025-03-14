package content.minigame.clanwars

import core.api.setVarp
import core.game.activity.ActivityPlugin
import core.game.component.Component
import core.game.interaction.Option
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.impl.PulseManager
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.communication.ClanEntry
import core.game.system.communication.ClanRepository
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import core.game.world.map.RegionManager.getRegionChunk
import core.game.world.map.RegionManager.isTeleportPermitted
import core.game.world.map.build.DynamicRegion
import core.game.world.map.zone.RegionZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneType
import core.game.world.map.zone.impl.MultiwayCombatZone
import core.game.world.update.flag.chunk.AnimateSceneryUpdateFlag
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import java.util.*

@Initializable
class ClanWarsActivityPlugin : ActivityPlugin("Clan wars", true, false, true) {
    private var firstClan: ClanRepository? = null
    private var secondClan: ClanRepository? = null
    private val firstClanPlayers: MutableList<Player> = ArrayList(20)
    private val secondClanPlayers: MutableList<Player> = ArrayList(20)
    private val viewingPlayers: MutableList<Player> = ArrayList(20)

    var ticks = 0
    private var pulse: Pulse? = null

    override fun start(
        player: Player,
        login: Boolean,
        vararg args: Any,
    ): Boolean {
        val other = args[0] as Player
        firstClan = player.communication.clan
        secondClan = other.communication.clan
        firstClan!!.clanWar = this
        secondClan!!.clanWar = this
        sendWarDeclaration(firstClan!!.players)
        sendWarDeclaration(secondClan!!.players)
        handleWall()
        join(player)
        join(other)
        return true
    }

    private fun handleWall() {
        var offset = 0
        for (x in 5..53) {
            offset = (offset + 1) % 3
            SceneryBuilder.add(
                Scenery(
                    28174 + offset,
                    base.transform(x, 64, 0),
                ),
            )
        }
        Pulser.submit(
            object : Pulse(200) {
                override fun pulse(): Boolean {
                    for (x in 5..53) {
                        val l = base.transform(x, 64, 0)
                        val scenery = getObject(l)
                        if (scenery != null) {
                            val anim = Animation.create(7386 + (scenery.id - 28174) % 3)
                            anim.setObject(scenery)
                            getRegionChunk(l).flag(
                                AnimateSceneryUpdateFlag(
                                    anim,
                                ),
                            )
                        }
                    }
                    Pulser.submit(
                        object : Pulse(5) {
                            override fun pulse(): Boolean {
                                for (x in 5..53) {
                                    val l = base.transform(x, 64, 0)
                                    val `object` = getObject(l)
                                    if (`object` != null) {
                                        SceneryBuilder.remove(`object`)
                                    }
                                }
                                return true
                            }
                        },
                    )
                    super.setTicksPassed(250)
                    sendGameData()
                    if (firstClanPlayers.isEmpty() || secondClanPlayers.isEmpty()) {
                        finishWar()
                    }
                    return true
                }
            }.also { pulse = it },
        )
    }

    fun finishWar() {
        firstClan!!.clanWar = null
        secondClan!!.clanWar = null
        val firstInterfaceId = if (firstClanPlayers.isEmpty()) 650 else 651
        val secondInterfaceId = if (firstClanPlayers.isEmpty()) 651 else 650
        val message = arrayOf("Your clan has been defeated!", "Your clan is victorious!")
        for (p in firstClanPlayers) {
            p.properties.teleportLocation = leaveLocation
            p.interfaceManager.openComponent(firstInterfaceId)
            p.packetDispatch.sendMessage(message[firstInterfaceId - 650])
            p.fullRestore()
            PulseManager.cancelDeathTask(p)
        }
        for (p in secondClanPlayers) {
            p.properties.teleportLocation = leaveLocation
            p.interfaceManager.openComponent(secondInterfaceId)
            p.packetDispatch.sendMessage(message[secondInterfaceId - 650])
            p.fullRestore()
            PulseManager.cancelDeathTask(p)
        }
        for (p in viewingPlayers) {
            p.properties.teleportLocation = leaveLocation
            if (p.communication.clan == firstClan) {
                p.interfaceManager.openComponent(firstInterfaceId)
                p.packetDispatch.sendMessage(message[firstInterfaceId - 650])
            } else {
                p.interfaceManager.openComponent(secondInterfaceId)
                p.packetDispatch.sendMessage(message[secondInterfaceId - 650])
            }
        }
    }

    fun sendGameData() {
        for (p in firstClanPlayers) {
            sendGameData(p)
        }
        for (p in secondClanPlayers) {
            sendGameData(p)
        }
        for (p in viewingPlayers) {
            sendGameData(p)
        }
    }

    fun sendGameData(p: Player) {
        var value = 0
        val first = p.communication.clan == firstClan
        var name = firstClan!!.name
        if (first) {
            value = value or (firstClanPlayers.size shl 5)
            value = value or (secondClanPlayers.size shl 14)
        } else {
            name = secondClan!!.name
            value = value or (secondClanPlayers.size shl 5)
            value = value or (firstClanPlayers.size shl 14)
        }
        if (pulse!!.ticksPassed < 200) {
            value = value or (200 - pulse!!.ticksPassed shl 23)
        }
        setVarp(p, 1147, value)
        p.packetDispatch.sendString(name, 265, 2)
    }

    override fun teleport(
        e: Entity,
        type: Int,
        node: Node,
    ): Boolean {
        if (type != -1 && type != 2 && e is Player) {
            e.packetDispatch.sendMessage("You can't teleport away from a war.")
            return false
        }
        return true
    }

    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            val player = e
            player.interaction.set(ATTACK_OPTION)
            player.interaction.remove(Option._P_ASSIST)
            player.skullManager.isSkullCheckDisabled = true
            player.skullManager.isWilderness = true
            player.interfaceManager.openOverlay(Component(265))
        } else if (e is content.global.skill.summoning.familiar.Familiar &&
            e !is content.global.skill.summoning.pet.Pet
        ) {
            val familiar = e
            if (familiar.isCombatFamiliar) {
                familiar.transform(familiar.originalId + 1)
            }
        }
        return true
    }

    override fun death(
        e: Entity,
        killer: Entity,
    ): Boolean {
        return if (e is Player) {
            enterViewingRoom(e)
        } else {
            false
        }
    }

    fun enterViewingRoom(player: Player): Boolean {
        var destination: Location? = null
        destination =
            if (player.communication.clan == firstClan) {
                remove(player, firstClanPlayers)
                base.transform(55 + RandomFunction.randomize(3), 51 + RandomFunction.randomize(11), 0)
            } else if (player.communication.clan == secondClan) {
                remove(player, secondClanPlayers)
                base.transform(55 + RandomFunction.randomize(3), 66 + RandomFunction.randomize(11), 0)
            } else {
                return false
            }
        player.properties.teleportLocation = destination
        viewingPlayers.add(player)
        sendGameData()
        return true
    }

    override fun continueAttack(
        e: Entity,
        target: Node,
        style: CombatStyle,
        message: Boolean,
    ): Boolean {
        var e: Entity? = e
        var target: Node? = target
        if (e is content.global.skill.summoning.familiar.Familiar) {
            e = e.owner
        }
        if (target is content.global.skill.summoning.familiar.Familiar) {
            target = target.owner
        }
        if (e is Player && target is Player) {
            val player = e
            val other = target
            val clan = player.communication.clan
            if (pulse!!.isRunning) {
                player.packetDispatch.sendMessage("The war hasn't started yet.")
                return false
            }
            if (!firstClanPlayers.contains(player) && !secondClanPlayers.contains(player)) {
                return false
            }
            if (other.communication.clan == clan) {
                player.packetDispatch.sendMessage("You can only attack players in a different clan.")
                return false
            }
            if (!firstClanPlayers.contains(other) && !secondClanPlayers.contains(other)) {
                player.packetDispatch.sendMessage("You can't attack this player.")
                return false
            }
        }
        return true
    }

    fun remove(
        player: Player,
        players: MutableList<Player>,
    ) {
        players.remove(player)
        if (!pulse!!.isRunning && players.isEmpty()) {
            finishWar()
        }
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (target is Scenery) {
            val `object` = target
            if (`object`.id == 28214 || `object`.id == 28140) {
                e.properties.teleportLocation = leaveLocation
                return true
            }
        }
        return false
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (e is Player) {
            val player = e
            player.interfaceManager.closeOverlay()
            player.interaction.remove(ATTACK_OPTION)
            if (firstClanPlayers.contains(player)) {
                remove(player, firstClanPlayers)
            } else if (secondClanPlayers.contains(player)) {
                remove(player, secondClanPlayers)
            } else {
                viewingPlayers.remove(player)
            }
            sendGameData()
            player.skullManager.isSkullCheckDisabled = false
            player.skullManager.isWilderness = false
            if (logout) {
                e.setLocation(leaveLocation)
            }
        } else if (e is content.global.skill.summoning.familiar.Familiar &&
            e !is content.global.skill.summoning.pet.Pet
        ) {
            val familiar = e
            if (familiar.isCombatFamiliar) {
                familiar.reTransform()
            }
        }
        return true
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        if (identifier == "join") {
            join(args[0] as Player)
            return true
        }
        if (identifier == "leavefc") {
            val p = args[0] as Player
            p.properties.teleportLocation = leaveLocation
            return true
        }
        return null
    }

    fun join(player: Player) {
        if (!pulse!!.isRunning) {
            enterViewingRoom(player)
            return
        }
        val first = player.communication.clan == firstClan
        if (first) {
            if (firstClanPlayers.contains(player)) {
                return
            }
            firstClanPlayers.add(player)
            player.properties.teleportLocation = base.transform(34 + RandomFunction.randomize(4), 10, 0)
        } else {
            if (secondClanPlayers.contains(player)) {
                return
            }
            secondClanPlayers.add(player)
            player.properties.teleportLocation = base.transform(26 + RandomFunction.randomize(4), 118, 0)
        }
        sendGameData()
    }

    override fun configure() {
        zoneType = ZoneType.SAFE.id
        val regions = DynamicRegion.create(ZoneBorders(3264, 3712, 3328, 3840))
        setRegionBase(regions)
        val x = base.x
        val y = base.y + 20
        val multi = RegionZone(MultiwayCombatZone.instance, ZoneBorders(x, y, x + 63, y + 88))
        for (r in regions) {
            r.isMulticombat = true
            r.regionZones.add(multi)
            r.setRegionTimeOut(250)
            r.setMusicId(442)
        }
    }

    override fun getSpawnLocation(): Location? {
        return null
    }

    @Throws(Throwable::class)
    override fun newInstance(p: Player): ActivityPlugin {
        return ClanWarsActivityPlugin()
    }

    companion object {
        private val ATTACK_OPTION =
            Option("Attack", 0).setHandler(
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
        private val leaveLocation: Location
            private get() {
                var l = Location.create(3265 + RandomFunction.randomize(13), 3675 + RandomFunction.randomize(18), 0)
                while (!isTeleportPermitted(l)) {
                    l = l.transform(1, 1, 0)
                }
                return l
            }

        private fun sendWarDeclaration(players: List<ClanEntry>) {
            val check = Location.create(3272, 3682, 0)
            for (e in players) {
                val p = e.player
                if (p.location.withinDistance(check, 128)) {
                    p.packetDispatch.sendMessage("<col=ff0000>Your clan has been challenged to a clan war!")
                    p.packetDispatch.sendMessage("<col=ff0000>Step through the purple portal in the Challenge Hall.")
                    p.packetDispatch.sendMessage("<col=ff0000>Battle will commence in 2 minutes.")
                }
            }
        }
    }
}
