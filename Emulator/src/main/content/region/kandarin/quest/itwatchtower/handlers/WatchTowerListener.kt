package content.region.kandarin.quest.itwatchtower.handlers

import core.api.*
import core.api.quest.getQuestStage
import core.api.ui.closeDialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class WatchTowerListener : InteractionListener {
    companion object {
        val NW_PILLAR = 3127
        val NE_PILLAR = 3129
        val SW_PILLAR = 3128
        val SE_PILLAR = 3129
    }

    override fun defineListeners() {
        val bushes =
            mapOf(
                Scenery.BUSH_2798 to null,
                Scenery.BUSH_2799 to Pair(Items.FINGERNAILS_2384, "What's this? Disgusting! Some fingernails. They may be a clue though... I'd better take them."),
                Scenery.BUSH_2800 to Pair(Items.DAMAGED_DAGGER_2387, "Aha, a dagger! I wonder if this is evidence..."),
                Scenery.BUSH_2801 to Pair(Items.TATTERED_EYE_PATCH_2388, "I've found an eye patch; I had better show this to the Watchtower Wizard."),
                Scenery.BUSH_2802 to Pair(Items.OLD_ROBE_2385, "Aha! A robe. This could be a clue..."),
                Scenery.BUSH_2803 to Pair(Items.UNUSUAL_ARMOUR_2386, "Here's some armour; it could be evidence..."),
            )

        /*
         * Handles searching the bushes for quest items.
         * Source: https://youtu.be/27eg7zxUtZc?si=_L8zZhvdY8P-jQJh
         */

        bushes.forEach { (bush, item) ->
            on(bush, IntType.SCENERY, "search") { player, _ ->
                searchBush(player, item)
                return@on true
            }
        }

        /*
         * Handles enter to skavid caves.
         */

        on(Scenery.CAVE_ENTRANCE_2806, IntType.SCENERY, "enter") { player, _ ->
            sendDialogueLines(player, "If your light source goes out down there you'll be in trouble! Are", "you sure you want to go in without a tinderbox?")
            addDialogueAction(player) { _, _ ->
                sendDialogueOptions(player, "Select an Option", "I'll be fine without a tinderbox.", "I'll come back with a tinderbox.")
                addDialogueAction(player) { _, button ->
                    if (button == 2) {
                        teleport(player, Location.create(2530, 9467, 0), TeleportManager.TeleportType.INSTANT)
                        sendMessage(player, "You enter the cave...")
                    } else {
                        closeDialogue(player)
                    }
                }
            }
            return@on true
        }

        on(Scenery.CAVE_EXIT_2818, IntType.SCENERY, "leave") { player, _ ->
            teleport(player, Location.create(2524, 3070, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles open the chest.
         */

        onUseWith(IntType.SCENERY, Items.TOBANS_KEY_2378, Scenery.CHEST_2790) { player, _, with ->
            if (
                freeSlots(player) <= 1 ||
                inInventory(player, Items.TOBANS_GOLD_2393) ||
                getQuestStage(player, Quests.WATCHTOWER) < 6
            ) {
                sendMessage(player, "This chest is securely locked shut.")
            } else {
                replaceScenery(with.asScenery(), 2828, 8)
                sendItemDialogue(player, Items.TOBANS_GOLD_2393, "You find a stash of gold inside.")
                addItem(player, Items.TOBANS_GOLD_2393)
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, Items.CAVE_NIGHTSHADE_2398, NPCs.ENCLAVE_GUARD_870) { _, _, _ ->
            return@onUseWith true
        }
    }

    private fun searchBush(player: Player, item: Pair<Int, String>?, ): Boolean {
        lock(player, 3)
        animate(player, 800)
        when {
            item == null -> sendPlayerDialogue(player, "Hmmm, nothing here.", FaceAnim.NEUTRAL)
            !inInventory(player, item.first) -> {
                sendPlayerDialogue(player, item.second, FaceAnim.NEUTRAL)
                addItem(player, item.first)
            }
            else -> sendPlayerDialogue(player, "I have already searched this place.", FaceAnim.NEUTRAL)
        }
        return true
    }
}
