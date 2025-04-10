package content.region.desert.handlers

import content.region.desert.dialogue.sophanem.EmbalmerDialogueFile
import core.api.*
import core.api.quest.hasRequirement
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class SophanemListener : InteractionListener {

    override fun defineListeners() {
        on(LADDER_UP, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.USE_LADDER_828), Location(3315, 2796, 0))
            return@on true
        }

        on(LADDER_DOWN, IntType.SCENERY, "climb-down") { player, _ ->
            if (!hasRequirement(player, Quests.CONTACT)) return@on true
            ClimbActionHandler.climb(player, Animation(Animations.MULTI_BEND_OVER_827), Location(2799, 5160, 0))
            return@on true
        }

        on(Scenery.DOOR_6614, IntType.SCENERY, "open") { player, _ ->
            lock(player, 3)
            openInterface(player, Components.FADE_TO_BLACK_115)
            runTask(player, 2) {
                teleport(player, Location.create(3277, 9171, 0), TeleportManager.TeleportType.INSTANT)
            }
            return@on true
        }

        /*
         * Handles using Acne potion on Embalmer.
         */

        onUseWith(IntType.NPC, Items.POTION_195, NPCs.EMBALMER_1980) { player, _, _ ->
            openDialogue(player, EmbalmerDialogueFile())
            return@onUseWith true
        }

        /*
         * Handles search the spice stall.
         */

        on(Scenery.SPICE_STALL_20348, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "Nothing interesting happens.")
            return@on true
        }

    }

    companion object {
        private const val LADDER_UP = Scenery.LADDER_20277
        private const val LADDER_DOWN = Scenery.LADDER_20275
    }

}
