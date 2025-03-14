package content.global.skill.slayer.dungeon

import content.global.skill.slayer.SlayerEquipmentFlags
import core.api.sendMessage
import core.cache.def.impl.NPCDefinition
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.plugin.ClassScanner
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class LumbridgeDungeon :
    MapZone("lumbridge swamp dungeon", true),
    Plugin<Any?> {
    companion object {
        private val beasts = mutableMapOf<Location, WallBeastNPC>()
    }

    override fun newInstance(arg: Any?): Plugin<Any?> {
        ZoneBuilder.configure(this)
        ClassScanner.definePlugin(WallBeastNPC())
        return this
    }

    override fun locationUpdate(
        entity: Entity,
        last: Location,
    ) {
        if (entity is Player) {
            beasts[last]?.takeIf { it.canAttack(entity) }?.trigger(entity)
        }
    }

    private fun hasHelmet(player: Player): Boolean {
        return SlayerEquipmentFlags.hasSpinyHelmet(player)
    }

    override fun interact(
        entity: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        return super.interact(entity, target, option)
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any?,
    ): Any? = null

    override fun configure() {
        register(ZoneBorders(3137, 9534, 3295, 9602))
    }

    inner class WallBeastNPC : AbstractNPC {
        constructor(id: Int, location: Location) : super(id, location, false)
        constructor() : super(NPCs.WALL_BEAST_7823, null, false)

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any?,
        ): AbstractNPC {
            return WallBeastNPC(id, location)
        }

        override fun init() {
            super.init()
            locks.lockMovement(Int.MAX_VALUE)
            beasts[location.transform(Direction.SOUTH, 1)] = this
        }

        fun trigger(player: Player) {
            val isProtected = hasHelmet(player)
            player.face(this)
            if (!isProtected) {
                animate(NPCDefinition.forId(NPCs.WALL_BEAST_7823).getCombatAnimation(3))
                player.animate(Animation.create(1810))
                GameWorld.Pulser.submit(
                    object : Pulse(8, player) {
                        override fun pulse(): Boolean {
                            animator.reset()
                            player.animator.reset()
                            player.impactHandler.handleImpact(
                                this@WallBeastNPC,
                                RandomFunction.random(1, 18),
                                CombatStyle.MELEE,
                            )
                            return true
                        }
                    },
                )
            } else {
                sendMessage(player, "Your spiny helmet repels the wall beast!")
                transform(NPCs.WALL_BEAST_7823)
                attack(player)
            }
        }

        override fun finalizeDeath(killer: Entity?) {
            super.finalizeDeath(killer)
        }

        override fun isPoisonImmune(): Boolean = true

        override fun getIds(): IntArray = intArrayOf()
    }
}
