package content.region.kandarin.miniquest.ctr.plugin

import content.data.GameAttributes
import content.region.kandarin.miniquest.ctr.npc.KnightWavesNPC
import core.api.*
import core.api.hasRequirement
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class TrainingGroundPlugin : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles start of activity.
         */

        on(intArrayOf(Scenery.LARGE_DOOR_25594, Scenery.LARGE_DOOR_25595), IntType.SCENERY, "open") { player, _ ->
            if (!hasRequirement(player, Quests.KINGS_RANSOM)) {
                return@on true
            }

            val kwComplete = getAttribute(player, GameAttributes.KW_COMPLETE, false)
            val kwBegin = getAttribute(player, GameAttributes.KW_BEGIN, false)

            if (kwComplete || !kwBegin) {
                openDialogue(player, NPCs.SQUIRE_6169)
                return@on false
            }

            val insideArea = player.location.x >= 2752

            if (insideArea) {
                teleport(player, Location.create(2751, 3507, 2))
                clearLogoutListener(player, "Knight's training")
                return@on true
            }

            // entering from outside.
            teleport(player, Location.create(2753, 3507, 2))
            registerLogoutListener(player, "Knight's training") {
                removeAttributes(player, GameAttributes.PRAYER_LOCK, GameAttributes.KW_SPAWN, GameAttributes.KW_TIER, GameAttributes.KW_BEGIN)
            }

            GameWorld.Pulser.submit(object : Pulse(1) {
                override fun pulse(): Boolean {
                    val tier = player.getAttribute(GameAttributes.KW_TIER, 6177)
                    val spawnLocation = Location.create(2758, 3508, 2)

                    KnightWavesNPC(tier, spawnLocation, player).init()
                    sendMessageWithDelay(player, "Remember, only melee combat is allowed in here.", 1)
                    return true
                }
            })
            return@on true
        }
    }
}
