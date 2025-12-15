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

    private class ToolDialogue(private val tools: List<Int>) : DialogueFile() {

        private val pageSize = 3
        private var page = 0

        override fun handle(componentID: Int, buttonID: Int) {
            val p = player ?: return
            val start = page * pageSize
            val end = minOf(start + pageSize, tools.size)
            val pageTools = tools.subList(start, end)

            val hasBack = page > 0
            val hasMore = end < tools.size

            when (stage) {
                0 -> {
                    val options = pageTools.map { getItemName(it) }.toMutableList()
                    if (hasBack) options.add("Back...")
                    if (hasMore) options.add("More...")
                    sendOptions(p, "Select a tool", *options.toTypedArray())
                    stage = 1
                }

                1 -> {
                    val selectedIndex = buttonID - 1
                    val backIndex = pageTools.size
                    val moreIndex = backIndex + if (hasBack) 1 else 0
                    val optionsCount = pageTools.size + (if (hasBack) 1 else 0) + (if (hasMore) 1 else 0)

                    if (selectedIndex !in 0 until optionsCount) {
                        end()
                        return
                    }

                    when (selectedIndex) {
                        backIndex.takeIf { hasBack } -> { page--; stage = 0; handle(componentID, 1) }
                        moreIndex.takeIf { hasMore } -> { page++; stage = 0; handle(componentID, 1) }
                        else -> { takeTool(p, pageTools[selectedIndex]); end() }
                    }
                }
            }
        }

        private fun takeTool(p: Player, toolId: Int) {
            val canStack = ItemDefinition.forId(toolId).isStackable
            val hasInInventory = amountInInventory(p, toolId) > 0

            if (freeSlots(p) == 0 && (!canStack || !hasInInventory)) {
                sendMessage(p, "You have no space in your inventory.")
            } else {
                addItem(p, toolId, 1)
                sendMessage(p, "You take a ${getItemName(toolId).lowercase()}.")
            }
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
