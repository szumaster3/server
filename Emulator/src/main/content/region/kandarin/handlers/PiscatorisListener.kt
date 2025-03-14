package content.region.kandarin.handlers

import content.global.travel.RowingBoat
import core.api.*
import core.api.quest.hasRequirement
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.system.task.Pulse
import org.rs.consts.*

class PiscatorisListener : InteractionListener {
    companion object {
        private const val NET_SCENERY = Scenery.NET_14973
        private const val EMPTY_NET_SCENERY = Scenery.NET_14972
        private const val KATHY_CORKAT = NPCs.KATHY_CORKAT_3831
    }

    override fun defineListeners() {
        on(KATHY_CORKAT, IntType.NPC, "travel") { player, node ->
            RowingBoat.sail(player, node.asNpc())
            return@on true
        }

        on(NET_SCENERY, IntType.SCENERY, "Take-from") { player, node ->
            if (!hasRequirement(player, Quests.SWAN_SONG)) return@on true

            if (!hasSpaceFor(player, Item(Items.SEAWEED_401, 1))) {
                sendMessage(player, "You do not have space in your inventory.")
                return@on true
            }
            submitIndividualPulse(
                player,
                object : Pulse() {
                    private var tick = 0

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
                },
            )
            return@on true
        }
    }
}
