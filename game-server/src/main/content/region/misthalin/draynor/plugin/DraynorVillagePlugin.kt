package content.region.misthalin.draynor.plugin

import content.global.plugin.iface.DiangoReclaimInterface
import content.region.misthalin.draynor.dialogue.TreeGuardDialogue
import core.api.*
import core.game.activity.ActivityManager
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class DraynorVillagePlugin : InteractionListener {

    val WOM_BOOKCASE_ID = intArrayOf(Scenery.OLD_BOOKSHELF_7065, Scenery.OLD_BOOKSHELF_7066, Scenery.OLD_BOOKSHELF_7068)

    override fun defineListeners() {

        on(NPCs.AGGIE_922, IntType.NPC, "make-dyes") { player, node ->
            openDialogue(player, node.asNpc().id, node, true)
            return@on true
        }

        on(Scenery.TELESCOPE_7092, IntType.SCENERY, "observe") { player, _ ->
            ActivityManager.start(player, "draynor telescope", false)
            return@on true
        }

        on(Scenery.TRAPDOOR_6434, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), 6435, -1)
            return@on true
        }

        on(NPCs.DIANGO_970, IntType.NPC, "holiday-items") { player, _ ->
            DiangoReclaimInterface.open(player)
            return@on true
        }

        /*
         * Handles searching wise old man bookcases.
         */

        on(WOM_BOOKCASE_ID, IntType.SCENERY, "search") { player, node ->
            sendMessage(player, "You search the bookcase...")

            if (freeSlots(player) == 0) {
                sendDialogue(player, "You need at least one free inventory space to take from the shelves.")
                return@on true
            }

            val (itemId, bookName) = when (node.id) {
                7065 -> Items.STRANGE_BOOK_5507 to "Strange Book"
                7066 -> Items.BOOK_OF_FOLKLORE_5508 to "Book of folklore"
                7068 -> Items.BOOK_ON_CHICKENS_7464 to "Book on chickens"
                else -> null to null
            }

            if (itemId != null && !inInventory(player, itemId)) {
                addItem(player, itemId)
                sendMessage(player, "...and find a book named '$bookName'.")
            } else {
                sendMessage(player, "...and find nothing of interest.")
            }
            return@on true
        }

        on(Scenery.TREE_10041, IntType.SCENERY, "chop down", "talk to") { player, _ ->
            val treeGuardChat = arrayOf("Hey - gerroff me!", "You'll blow my cover! I'm meant to be hidden!", "Don't draw attention to me!", "Will you stop that?", "Watch what you're doing with that hatchet, you nit!", "Ooooch!", "Ow! That really hurt!", "Oi!")
            when (getUsedOption(player)) {
                "chop down" -> sendNPCDialogue(player, NPCs.GUARD_345, treeGuardChat.random(), FaceAnim.ANNOYED)
                "talk to" -> openDialogue(player, TreeGuardDialogue())
                else -> return@on false
            }
            return@on true
        }
    }
}
