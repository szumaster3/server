package content.region.kandarin.handlers.seers

import core.api.*
import core.api.quest.getQuestStage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.shops.Shops.Companion.openId
import org.rs.consts.*

class SeersVillageListener : InteractionListener {
    companion object {
        private val GATES = intArrayOf(47, 48, 52, 53)
        private const val COURTHOUSE_STAIRS = Scenery.STAIRS_26017
        private const val CAGE = Scenery.CAGE_6836
        private const val CRATE = Scenery.CRATE_6839
        private const val TICKET_MERCHANT = NPCs.TICKET_MERCHANT_694
    }

    override fun defineListeners() {
        on(COURTHOUSE_STAIRS, IntType.SCENERY, "climb-down") { player, _ ->
            sendMessage(player, "Court is not in session.")
            return@on true
        }

        on(CRATE, IntType.SCENERY, "buy") { player, _ ->
            openId(player, 93)
            return@on true
        }

        on(TICKET_MERCHANT, IntType.NPC, "trade") { player: Player, _: Node ->
            openInterface(player, Components.RANGING_GUILD_TICKET_EXCHANGE_278)
            return@on true
        }

        on(GATES, IntType.SCENERY, "open") { player, node ->
            if (node.id == 47 || node.id == 48) {
                if (!getAttribute(player, "fishing_contest:pass-shown", false) ||
                    getQuestStage(
                        player,
                        Quests.FISHING_CONTEST,
                    ) < 10
                ) {
                    player.pulseManager.run(
                        object :
                            MovementPulse(player, node.asScenery().location.transform(1, 0, 0)) {
                            override fun pulse(): Boolean {
                                if (getQuestStage(player, Quests.FISHING_CONTEST) >= 10) {
                                    sendMessage(player, "You should give your pass to Morris.")
                                } else {
                                    sendMessage(player, "You need a fishing pass to fish here.")
                                }
                                return true
                            }
                        },
                        PulseType.STANDARD,
                    )
                } else {
                    if (!inInventory(player, Items.FISHING_ROD_307)) {
                        sendDialogue(player, "I should probably get a rod from Grandpa Jack before starting.")
                    }
                }
                return@on true
            }

            if (node.id == 52 || node.id == 53) {
                if (inBorders(player, 2647, 3468, 2652, 3469)) {
                    face(findNPC(NPCs.FORESTER_231)!!, player, 3)
                    sendNPCDialogue(
                        player,
                        NPCs.FORESTER_231,
                        "Hey! You can't come through here! This is private land!",
                    )
                    sendMessage(
                        player,
                        "There might be a gap in the fence somewhere where he wouldn't see you sneak in.",
                    )
                    sendMessage(player, "You should look around.")
                } else {
                    sendDialogue(player, "This gate is locked.")
                }
            }
            return@on true
        }
    }
}
