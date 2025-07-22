package content.region.desert.sophanem.plugin

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class SophanemPlugin : InteractionListener {

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

        onUseWith(IntType.NPC, Items.POTION_195, NPCs.EMBALMER_1980) { player, _, with ->
            val npc  = with.asNpc()
            dialogue(player) {
                npc(npc, FaceAnim.SUSPICIOUS, "What are you doing?")
                player(FaceAnim.FRIENDLY,"I have this potion which I thought might help ease your itching.")
                npc(npc, FaceAnim.NEUTRAL, "If I thought that these spots could be cured by some potion, I would have mixed up one for myself before now.")
                player(FaceAnim.HALF_GUILTY, "Sorry... I was just trying to help.")
            }
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
