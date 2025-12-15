package content.global.skill.construction.decoration.workpace

import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.Scenery

class ToolStoragePlugin : InteractionListener {

    override fun defineListeners() {
        on(TOOL_SCENERY_IDS, IntType.SCENERY, "search") { player, node ->
            val definition = ToolStorageDefinition.forId(node.id) ?: return@on true
            openDialogue(player, ToolDialogue(definition.tools.toList()))
            return@on true
        }
    }

    private class ToolDialogue(
        private val tools: List<Int>
    ) : DialogueFile() {

        private val pageSize = 3
        private var page = 0

        override fun handle(componentID: Int, buttonID: Int) {
            val p = player ?: return

            when (stage) {
                0 -> showPage(p)
                1 -> handleSelection(p, buttonID - 1)
            }
        }

        private fun showPage(p: Player) {
            val pageTools = currentPageTools()
            val options = buildList {
                addAll(pageTools.map { getItemName(it) })
                if (page > 0) add("Back...")
                if (hasNextPage()) add("More...")
            }

            sendOptions(p, "Select a tool", *options.toTypedArray())
            stage = 1
        }

        private fun handleSelection(p: Player, index: Int) {
            val pageTools = currentPageTools()
            val backIndex = pageTools.size
            val moreIndex = backIndex + if (page > 0) 1 else 0

            when {
                index < 0 -> end()
                index < pageTools.size -> {
                    takeTool(p, pageTools[index])
                    end()
                }
                index == backIndex && page > 0 -> {
                    page--
                    stage = 0
                    handle(0, 0)
                }
                index == moreIndex && hasNextPage() -> {
                    page++
                    stage = 0
                    handle(0, 0)
                }
                else -> end()
            }
        }

        private fun currentPageTools(): List<Int> {
            val from = page * pageSize
            val to = minOf(from + pageSize, tools.size)
            return tools.subList(from, to)
        }

        private fun hasNextPage() =
            (page + 1) * pageSize < tools.size

        private fun takeTool(p: Player, toolId: Int) {
            val def = ItemDefinition.forId(toolId)
            val hasSpace =
                freeSlots(p) > 0 ||
                        (def.isStackable && amountInInventory(p, toolId) > 0)

            if (!hasSpace) {
                sendMessage(p, "You have no space in your inventory.")
                return
            }

            addItem(p, toolId, 1)
            sendMessage(p, "You take a ${getItemName(toolId).lowercase()}.")
            stage = END_DIALOGUE
        }
    }

    private enum class ToolStorageDefinition(val objectId: Int, vararg val tools: Int) {
        TOOLSTORE_1(Scenery.TOOLS_13699, Items.SAW_8794, Items.HAMMER_2347, Items.CHISEL_1755, Items.SHEARS_1735),
        TOOLSTORE_2(Scenery.TOOLS_13700, Items.BUCKET_1925, Items.KNIFE_946, Items.SPADE_952, Items.TINDERBOX_590),
        TOOLSTORE_3(Scenery.TOOLS_13701, Items.BROWN_APRON_1757, Items.GLASSBLOWING_PIPE_1785, Items.NEEDLE_1733),
        TOOLSTORE_4(Scenery.TOOLS_13702, Items.AMULET_MOULD_1595, Items.NECKLACE_MOULD_1597, Items.RING_MOULD_1592, Items.HOLY_MOULD_1599, Items.BRACELET_MOULD_11065, Items.TIARA_MOULD_5523),
        TOOLSTORE_5(Scenery.TOOLS_13703, Items.RAKE_5341, Items.SPADE_952, Items.TROWEL_676, Items.SEED_DIBBER_5343, Items.WATERING_CAN_5331);

        companion object {
            fun forId(objectId: Int) = values().find { it.objectId == objectId }
        }
    }

    companion object {
        private val TOOL_SCENERY_IDS = intArrayOf(
            Scenery.TOOLS_13699,
            Scenery.TOOLS_13700,
            Scenery.TOOLS_13701,
            Scenery.TOOLS_13702,
            Scenery.TOOLS_13703
        )
    }
}
