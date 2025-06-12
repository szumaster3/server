package content.region.asgarnia.gwd

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import org.rs.consts.*

class GodwarsEntranceListener : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles tie rope on gwd entrance.
         */

        on(Scenery.HOLE_26340, IntType.SCENERY, "tie-rope") { player, _ ->
            if (!removeItem(player, Items.ROPE_954)) {
                sendMessage(player, "You don't have a rope to tie around the pillar.")
                return@on true
            }
            setVarbit(player, 3932, 1, true)
            return@on true
        }

        /*
         * Handles climb down the gwd entrance.
         */

        on(Scenery.HOLE_26341, IntType.SCENERY, "climb-down") { player, _ ->
            if (getStatLevel(player, Skills.AGILITY) < 15) {
                sendMessage(player, "You need an agility level of 15 to enter this.")
                return@on true
            }
            if (getVarbit(player, 3936) == 0) {
                sendNPCDialogue(player, NPCs.KNIGHT_6201, "Cough... Hey, over here.", FaceAnim.HALF_GUILTY)
                return@on true
            }
            lock(player, 2)
            sendMessage(player, "You climb down the rope.")
            animate(player, Animations.USE_LADDER_828)
            GameWorld.Pulser.submit(
                object : Pulse(1, player) {
                    override fun pulse(): Boolean {
                        teleport(player, Location.create(2882, 5311, 2))
                        return true
                    }
                },
            )
            return@on true
        }

        /*
         * Handles gwd shortcut.
         */

        on(Scenery.LITTLE_CRACK_26305, IntType.SCENERY, "crawl-through") { player, node ->
            if (getStatLevel(player, Skills.AGILITY) < 60) {
                sendMessage(player, "You need an agility level of 60 to squeeze through the crack.")
                return@on true
            }
            val from = node.location
            val destination = if (from == Location.create(2900, 3713, 0)) {
                Location.create(2904, 3720, 0)
            } else {
                Location.create(2899, 3713, 0)
            }

            lock(player, 6)
            openOverlay(player, Components.FADE_TO_BLACK_115)
            submitWorldPulse(
                object : Pulse(6, player) {
                    override fun pulse(): Boolean {
                        openOverlay(player, Components.FADE_FROM_BLACK_170)
                        teleport(player, destination)
                        return true
                    }
                },
            )
            return@on true
        }

        /*
         * Handles climb the wall.
         */

        on(Scenery.CLIMBING_WALL_41068, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(2941, 3822, 0))
            return@on true
        }
    }
}
