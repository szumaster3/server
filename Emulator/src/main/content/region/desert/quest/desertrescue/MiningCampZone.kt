package content.region.desert.quest.desertrescue

import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Plugin
import org.rs.consts.Quests

class MiningCampZone :
    MapZone("mining camp", true),
    Plugin<Any> {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun enter(entity: Entity): Boolean {
        return super.enter(entity)
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (!logout && e is Player) {
            if (checkAnna(e)) {
                return false
            }
        }
        return super.leave(e, logout)
    }

    override fun interact(
        e: Entity,
        node: Node,
        option: Option,
    ): Boolean {
        when (option.name) {
            "Equip", "Wear" -> {
                val player = e as Player
                Pulser.submit(
                    object : Pulse(1, player) {
                        override fun pulse(): Boolean {
                            if (TouristTrap.isJailable(player)) {
                                TouristTrap.jail(player, "Hey! What do you think you're doing!?")
                            }
                            return true
                        }
                    },
                )
            }
        }
        return super.interact(e, node, option)
    }

    override fun teleport(
        entity: Entity,
        type: Int,
        node: Node,
    ): Boolean {
        return if (entity is Player && type != -1) {
            !checkAnna(entity)
        } else {
            super.teleport(entity, type, node)
        }
    }

    fun checkAnna(p: Player): Boolean {
        val quest = p.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
        if (p.getAttribute("ana-delay", 0) > ticks) {
            return false
        }
        if (quest.getStage(p) in 61..94 && p.inventory.containsItem(TouristTrap.ANNA_BARREL)) {
            p.inventory.remove(TouristTrap.ANNA_BARREL)
            p.lock(5)
            TouristTrap.addConfig(p, (1 shl 4))
            quest.setStage(p, 61)
            p.properties.teleportLocation = Location.create(3285, 3034, 0)
            p.packetDispatch.sendMessage("The guards spot anna and throw you in jail.")
            return true
        }
        return false
    }

    override fun configure() {
        register(ZoneBorders(3274, 3014, 3305, 3041))
        register(ZoneBorders(3260, 9408, 3331, 9472))
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }
}
