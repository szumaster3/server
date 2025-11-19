package content.region.kandarin.piscatoris.plugin

import content.region.kandarin.piscatoris.dialogue.KathyCorkatDialogue.Companion.sail
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import shared.consts.*

class PiscatorisPlugin : InteractionListener {
    companion object {
        private const val NET_SCENERY = Scenery.NET_14973
        private const val EMPTY_NET_SCENERY = Scenery.NET_14972
        private const val KATHY_CORKAT = NPCs.KATHY_CORKAT_3831
        private const val HERMANS_DESK = Scenery.HERMAN_S_DESK_14971
    }

    override fun defineListeners() {

        /*
         * Handles talking to Karthy Corkat NPCs.
         */

        on(KATHY_CORKAT, IntType.NPC, "travel") { player, node ->
            sail(player, node.asNpc())
            return@on true
        }

        /*
         * Handles interaction with herman desk.
         */

        on(HERMANS_DESK, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search Herman's desk...")
            val hasBook = hasAnItem(player, Items.HERMANS_BOOK_7951).container != null
            when {
                hasBook -> {
                    sendMessage(player, "...but find nothing.")
                }
                freeSlots(player) == 0 -> {
                    sendMessage(player, "You find 'Herman's book' but you don't have enough room to take it.")
                }
                else -> {
                    sendMessage(player, "You find Herman's book.")
                    addItemOrDrop(player, Items.HERMANS_BOOK_7951)
                }
            }
            return@on true
        }

        /*
         * Handles interaction with net desk.
         */

        on(NET_SCENERY, IntType.SCENERY, "Take-from") { player, node ->
            if (!hasRequirement(player, Quests.SWAN_SONG)) return@on false

            if (!hasSpaceFor(player, Item(Items.SEAWEED_401, 1))) {
                sendMessage(player, "You do not have space in your inventory.")
                return@on true
            }

            submitIndividualPulse(player, object : Pulse() {
                var tick = 0
                override fun pulse(): Boolean {
                    when (tick++) {
                        0 -> animate(player, Animations.HUMAN_BURYING_BONES_827)
                        1 -> {
                            if (addItem(player, Items.SEAWEED_401)) {
                                replaceScenery(node.asScenery(), EMPTY_NET_SCENERY, 5)
                            }
                            return true
                        }
                    }
                    return false
                }
            })

            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC, NPCs.HERMAN_CARANOS_3822) { _, _ ->
            return@setDest Location.create(2354, 3681, 0)
        }
    }
}
