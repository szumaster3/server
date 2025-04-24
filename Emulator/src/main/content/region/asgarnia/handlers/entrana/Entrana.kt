package content.region.asgarnia.handlers.entrana

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.bots.AIPlayer
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.NPCs

class Entrana : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(
        getRegionBorders(11060), getRegionBorders(11316)
    )

    override fun areaEnter(entity: Entity) {
        if (entity is Player && entity !is AIPlayer && entity.details.rights != Rights.ADMINISTRATOR) {
            // Checking items made on the island.
            if (!ItemDefinition.canEnterEntrana(entity)) {
                enforceIslandRestrictions(entity)
            }
        }
    }

    private fun enforceIslandRestrictions(entity: Player) {
        val monk = NPC(NPCs.MONK_4608).apply {
            isNeverWalks = true
            isWalks = false
            location = entity.location
            init()
        }

        entity.lock(10)
        sendGraphics(Graphics.create(86, 96), monk.location)

        sendChat(monk, "Hey, ${entity.asPlayer().username}, get that stuff off our island!")
        sendMessage(entity.asPlayer(), "A passing monk objects to the items you are carrying!")

        runTask(monk, 1) {
            monk.moveStep()
            monk.face(entity)
            monk.animate(Animation.create(442))
            entity.asPlayer().animator.animate(Animation(Animations.DEATH_836))
            openInterface(entity.asPlayer(), Components.FADE_TO_BLACK_115)
            GameWorld.Pulser.submit(
                object : Pulse(3) {
                    override fun pulse(): Boolean {
                        if (getAttribute(entity, "teleporting-away", false)) return true
                        teleport(entity.asPlayer(), Location.create(3047, 3231, 1))
                        openInterface(entity.asPlayer(), Components.FADE_FROM_BLACK_170)
                        entity.asPlayer().animator.animate(Animation(Animations.DEATH_4200))
                        sendGraphics(
                            Graphics(org.rs.consts.Graphics.STUN_BIRDIES_ABOVE_HEAD_80, 96), entity.asPlayer().location
                        )
                        sendChat(entity.asPlayer(), "Urrrrrgh!", 6)
                        if (!entity.isActive) poofClear(monk)
                        return !monk.isActive || !entity.isActive
                    }
                },
            )
        }
    }
}