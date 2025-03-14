package content.region.kandarin.handlers.barbtraining.fishing

import content.region.kandarin.handlers.barbtraining.BarbarianTraining
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class BarbFishingListener : InteractionListener {
    private val fishCuttingIds =
        intArrayOf(Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330, Items.LEAPING_STURGEON_11332)
    private val barbFishingSpot = NPCs.FISHING_SPOT_1176
    private val barbarianBed = Scenery.BARBARIAN_BED_25268
    private val barbarianRod = Items.BARBARIAN_ROD_11323

    override fun defineListeners() {
        on(barbarianBed, IntType.SCENERY, "search") { player, _ ->
            if (getAttribute(player, BarbarianTraining.FISHING_BASE, false)) {
                if (!inInventory(player, barbarianRod) && freeSlots(player) > 0) {
                    sendMessage(player, "You find a heavy fishing rod under the bed and take it.")
                    addItem(player, barbarianRod)
                } else {
                    sendMessage(player, "You don't find anything that interests you.")
                }
            } else {
                sendMessage(player, "You don't find anything that interests you.")
            }
            return@on true
        }

        on(barbFishingSpot, IntType.NPC, "fish") { player, _ ->
            if (!getAttribute(player, BarbarianTraining.FISHING_START, false)) {
                sendMessage(player, "You must begin the relevant section of Otto Godblessed's barbarian training.")
                return@on true
            }

            sendMessage(player, "You cast out your line...")
            submitIndividualPulse(player, BarbFishingPulse(player))
            return@on true
        }

        onUseWith(IntType.ITEM, Items.KNIFE_946, *fishCuttingIds) { player, _, with ->
            submitIndividualPulse(player, BarbFishCuttingPulse(player, with.id))
            return@onUseWith true
        }
    }
}
