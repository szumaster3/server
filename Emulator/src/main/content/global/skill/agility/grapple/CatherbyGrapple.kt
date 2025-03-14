package content.global.skill.agility.grapple

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class CatherbyGrapple : InteractionListener {
    companion object {
        private val START_LOCATION: Location = Location.create(2866, 3429, 0)
        private val END_LOCATION: Location = Location.create(2869, 3430, 0)

        private val REQUIREMENTS =
            hashMapOf(
                Skills.AGILITY to 32,
                Skills.RANGE to 35,
                Skills.STRENGTH to 35,
            )

        private val crossbowIds =
            intArrayOf(
                Items.DORGESHUUN_CBOW_8880,
                Items.MITH_CROSSBOW_9181,
                Items.ADAMANT_CROSSBOW_9183,
                Items.RUNE_CROSSBOW_9185,
                Items.KARILS_CROSSBOW_4734,
                Items.HUNTERS_CROSSBOW_10156,
            )
        private val grappleId = Items.MITH_GRAPPLE_9419
    }

    private var rocks = getScenery(Location.create(2869, 3429, 0))

    override fun defineListeners() {
        flagInstant()
        on(Scenery.ROCKS_17042, IntType.SCENERY, "grapple") { player, _ ->
            if (isPlayerInRangeToGrapple(player)) {
                forceWalk(player, START_LOCATION, "smart")
            } else {
                sendMessage(player, "Nothing interesting happens.")
                return@on true
            }

            if (!inEquipment(player, grappleId) || !anyInEquipment(player, *crossbowIds)) {
                sendMessage(player, "You need a mithril grapple tipped bolt with a rope to do that.")
                return@on true
            }

            if (!doesPlayerHaveRequiredLevels(player)) {
                sendDialogueLines(
                    player,
                    "You need at least " +
                        REQUIREMENTS[Skills.AGILITY] + " " + Skills.SKILL_NAME[Skills.AGILITY] + ", " +
                        REQUIREMENTS[Skills.RANGE] + " " + Skills.SKILL_NAME[Skills.RANGE] + ", ",
                    "and " +
                        REQUIREMENTS[Skills.STRENGTH] + " " + Skills.SKILL_NAME[Skills.STRENGTH] +
                        " to use this shortcut.",
                )
                return@on true
            }

            lock(player, 15)
            submitWorldPulse(
                object : Pulse(2) {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            1 -> {
                                face(player, END_LOCATION)
                                animate(player, Animation(Animations.FIRE_CROSSBOW_TO_CLIMB_WALL_4455))
                            }

                            3 -> {
                                replaceScenery(rocks!!, rocks!!.id + 1, 10)
                            }

                            8 -> {
                                teleport(player, END_LOCATION)
                            }

                            9 -> {
                                sendMessage(player, "You successfully grapple the rock and climb the cliffside.")
                                unlock(player)
                                return true
                            }
                        }
                        return false
                    }
                },
            )

            return@on true
        }
    }

    private fun doesPlayerHaveRequiredLevels(player: Player): Boolean {
        for ((skill, requiredLevel) in REQUIREMENTS) {
            if (!hasLevelDyn(player, skill, requiredLevel)) {
                return false
            }
        }
        return true
    }

    private fun isPlayerInRangeToGrapple(player: Player): Boolean {
        return inBorders(player, START_LOCATION.x - 2, START_LOCATION.y - 2, START_LOCATION.x, START_LOCATION.y)
    }
}
