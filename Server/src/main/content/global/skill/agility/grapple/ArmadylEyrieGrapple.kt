package content.global.skill.agility.grapple

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.equipment.RangeWeapon
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import shared.consts.Animations
import shared.consts.Graphics as Gfx
import shared.consts.Items
import shared.consts.Scenery

/**
 * Handles access to Armadyl's Eyrie.
 */
class ArmadylEyrieGrapple : InteractionListener {

    override fun defineListeners() {
        on(Scenery.PILLAR_26303, IntType.SCENERY, "grapple") { player, _ ->
            when {
                getStatLevel(player, Skills.RANGE) < 70 ->
                    sendMessage(player, "You need a Range level of 70 to enter here.")
                player.equipment.getNew(EquipmentContainer.SLOT_ARROWS).id != Items.MITH_GRAPPLE_9419 ->
                    sendMessage(player, "You need a mithril grapple to cross this.")
                RangeWeapon.get(player.equipment.getNew(3).id)?.type != 1 ->
                    sendMessage(player, "You need to wield a crossbow to fire a mithril grapple.")
                else -> {
                    val destination = Location.create(2872, if (player.location.y <= 5269) 5279 else 5269, 2)
                    val delay = animationCycles(Animations.SWING_WITH_CROSSBOW_BANDOS_THRONE_ROOM_6067)
                    lock(player, 3)
                    sendGraphics(Graphics(Gfx.MITHRIL_GRAPPLE_ABOUT_TO_SHOOT_1036, 96), player.location)
                    forceMove(
                        player = player,
                        start = player.location,
                        dest = destination,
                        startArrive = 0,
                        destArrive = 90,
                        dir = null,
                        anim = Animations.SWING_WITH_CROSSBOW_BANDOS_THRONE_ROOM_6067
                    )
                }
            }
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, Scenery.PILLAR_26303) { player, _ ->
            val y = if (player.location.y > 5269) 5279 else 5269
            return@setDest Location.create(2872, y, 2)
        }
    }
}
