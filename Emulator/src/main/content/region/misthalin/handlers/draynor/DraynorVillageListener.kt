package content.region.misthalin.handlers.draynor

import content.global.handlers.iface.DiangoReclaimInterface
import content.region.misthalin.dialogue.draynor.TreeGuardDialogue
import core.api.*
import core.game.activity.ActivityManager
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.NPCs

class DraynorVillageListener : InteractionListener {
    override fun defineListeners() {
        on(DraynorUtils.aggie, IntType.NPC, "make-dyes") { player, node ->
            openDialogue(player, node.asNpc().id, node, true)
            return@on true
        }

        on(DraynorUtils.telescope, IntType.SCENERY, "observe") { player, _ ->
            ActivityManager.start(player, "draynor telescope", false)
            return@on true
        }

        on(DraynorUtils.trapdoor, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), 6435, 500)
            return@on true
        }

        on(DraynorUtils.diango, IntType.NPC, "holiday-items") { player, _ ->
            DiangoReclaimInterface.open(player)
            return@on true
        }

        on(DraynorUtils.bookshelf, IntType.SCENERY, "search") { player, node ->
            if (freeSlots(player) == 0) {
                sendDialogue(player, "You need at least one free inventory space to take from the shelves.")
                return@on true
            }
            when (node.id) {
                7065 ->
                    if (!inInventory(player, Items.STRANGE_BOOK_5507)) {
                        sendMessage(player, "You search the bookcase and find a book named 'Strange Book'.")
                        addItem(player, Items.STRANGE_BOOK_5507)
                    }

                7066 ->
                    if (!inInventory(player, Items.BOOK_OF_FOLKLORE_5508)) {
                        sendMessage(player, "You search the bookcase and find a book named 'Book of folklore'.")
                        addItem(player, Items.BOOK_OF_FOLKLORE_5508)
                    }

                7068 ->
                    if (!inInventory(player, Items.BOOK_ON_CHICKENS_7464)) {
                        sendMessage(player, "You search the bookcase and find a book named 'Book on chickens'.")
                        addItem(player, Items.BOOK_ON_CHICKENS_7464)
                    }

                else -> sendMessage(player, "You search the bookcase and find nothing of interest.")
            }
            return@on true
        }

        on(DraynorUtils.tree, IntType.SCENERY, "chop down", "talk to") { player, _ ->
            when (getUsedOption(player)) {
                "chop down" ->
                    sendNPCDialogue(
                        player,
                        NPCs.GUARD_345,
                        DraynorUtils.treeGuardChat.random(),
                        FaceAnim.ANNOYED,
                    )

                "talk to" -> openDialogue(player, TreeGuardDialogue())
            }
            return@on true
        }
    }
}
