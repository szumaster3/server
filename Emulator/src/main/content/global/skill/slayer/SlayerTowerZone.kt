package content.global.skill.slayer

import core.api.getStatLevel
import core.api.sendMessage
import core.game.global.action.DoorActionHandler
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.map.zone.ZoneRestriction
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class SlayerTowerZone :
    MapZone("slayer tower", true, ZoneRestriction.CANNON),
    Plugin<Any?> {
    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        return this
    }

    override fun interact(
        entity: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (entity is Player) {
            val level = if (entity.location.z == 0) 61 else 71
            if (target.id == 9319 && getStatLevel(entity, Skills.AGILITY) < level) {
                sendMessage(entity, "You need an agility level of at least $level in order to do this.")
                return true
            }
            if (target.id == 10527 || target.id == 10528) {
                DoorActionHandler.handleAutowalkDoor(entity, target as Scenery)
                return true
            }
        }
        return super.interact(entity, target, option)
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    override fun configure() {
        register(ZoneBorders(3401, 3527, 3459, 3585))
    }
}
