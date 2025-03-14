package content.region.misthalin.handlers.rc_guild

import core.api.*
import core.api.quest.isQuestComplete
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.Quests
import org.rs.consts.Scenery

class RCGuildPortal : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles the interaction with the RC Portal scenery.
         * Checks if the player has the required RC level and has completed the Rune Mysteries quest.
         */

        on(Scenery.PORTAL_38279, IntType.SCENERY, "Enter") { player, _ ->
            if (getStatLevel(player, Skills.RUNECRAFTING) < 50) {
                sendDialogue(player, "You require 50 Runecrafting to enter the Runecrafters' Guild.")
                return@on true
            }
            if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                sendDialogue(player, "You need to complete Rune Mysteries to enter the Runecrafting guild.")
                return@on true
            }

            val destination =
                if (player.viewport.region.regionId == 12337) {
                    Location.create(1696, 5461, 2)
                } else {
                    Location.create(3106, 3160, 1)
                }

            player.lock(4)
            visualize(player, Animations.RC_TP_A_10180, Graphics.RC_GUILD_TP)
            queueScript(player, 3, QueueStrength.SOFT) {
                teleport(player, destination)
                visualize(player, Animations.RC_TP_B_10182, Graphics.RC_GUILD_TP)
                face(player, destination)
                return@queueScript stopExecuting(player)
            }
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.PORTAL_38279), "enter") { player, node ->
            if (player.viewport.region.regionId == 12337) {
                return@setDest node.asScenery().location
            } else {
                return@setDest Location.create(1696, 5461, 2)
            }
        }
    }
}
