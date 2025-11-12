package content.region.island.mosle.plugin

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

class MosLeHarmlessPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles honest jimmy interactions.
         */

        on(NPCs.HONEST_JIMMY_4362, IntType.NPC, "Join-Team") { player, _ ->
            sendMessage(player, "There must be at least 3 people on each team for the game to start.")
            return@on true
        }

        /*
         * Handles patchy sewing interaction.
         */

        on(NPCs.PATCHY_4359, IntType.NPC, "sew") { player, _ ->
            openInterface(player, Components.SEW_INTERFACE_419)
            return@on true
        }

        /*
         * Handles split beret and mask.
         */

        onUseWith(IntType.NPC, BERET_AND_MASK, NPCs.PATCHY_4359) { player, used, _ ->
            if (freeSlots(player) < 2) {
                sendNPCDialogueLines(
                    player,
                    NPCs.PATCHY_4359,
                    FaceAnim.STRUGGLE,
                    false,
                    "Ye don't seem te have enough free space few the two items.",
                    "Ye might want te visit the bank.",
                )
                return@onUseWith false
            }

            if (!removeItem(player, Items.BERET_AND_MASK_11282)) {
                sendNPCDialogue(
                    player,
                    NPCs.PATCHY_4359,
                    "Sorry, I can't do anythin' with that.",
                    FaceAnim.SAD
                )
                return@onUseWith false
            }

            addItemOrDrop(player, Items.BLACK_BERET_2635)
            addItemOrDrop(player, Items.MIME_MASK_3057)
            return@onUseWith true
        }

        /*
         * Handles interaction with shop owners.
         */

        on(SHOP_OWNERS, IntType.NPC, "trade") { player, node ->
            if (!inInventory(player, Items.BOOK_O_PIRACY_7144)) {
                player.dialogueInterpreter.open(node.asNpc().id, node)
            } else {
                openNpcShop(player, node.asNpc().id)
            }
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        /*
         * Handles destination to shop NPCs.
         */

        setDest(IntType.NPC, intArrayOf(NPCs.MIKE_3166), "talk-to", "trade") { _, _ ->
            return@setDest Location.create(3693, 2975, 0)
        }

        setDest(IntType.NPC, intArrayOf(NPCs.CHARLEY_3161), "talk-to", "trade") { _, _ ->
            return@setDest Location.create(3674, 2968, 0)
        }

        setDest(IntType.NPC, intArrayOf(NPCs.MAMA_3164), "talk-to", "trade") { player, _ ->
            if (player.location.y < 76) {
                return@setDest Location.create(3665, 2980, 0).transform(0, -2, 0)
            } else {
                return@setDest Location.create(3665, 2980, 0).transform(0, 2, 0)
            }
        }

        setDest(IntType.NPC, intArrayOf(NPCs.JOE_3163), "talk-to", "trade") { player, _ ->
            if (inBorders(player, 3666, 2990, 3670, 2997)) {
                return@setDest Location.create(3667, 2992, 0)
            } else {
                return@setDest Location.create(3665, 2992, 0)
            }
        }
    }

    companion object {
        private val SHOP_OWNERS = intArrayOf(
            NPCs.MIKE_3166,
            NPCs.CHARLEY_3161,
            NPCs.MAMA_3164,
            NPCs.JOE_3163,
            NPCs.HONEST_JIMMY_4362
        )
        private const val BERET_AND_MASK = Items.BERET_AND_MASK_11282
    }
}
