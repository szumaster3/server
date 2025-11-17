package content.global.skill.construction.decoration.workpace

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items
import shared.consts.Scenery

class ToolPlugin : InteractionListener {

    override fun defineListeners() {
        on(TOOL_IDS, IntType.SCENERY, "search") { player, node ->
            val toolStore = ToolStore.forId(node.id) ?: return@on true

            val optionNames = toolStore.tools.map { getItemName(it) }.toTypedArray()
            sendOptions(player, "Select a tool", *optionNames)

            addDialogueAction(player) { _, buttonID ->
                val index = buttonID - 2
                if (index !in toolStore.tools.indices) return@addDialogueAction

                if (freeSlots(player) <= 0) {
                    sendDialogue(player, "You have no space in your inventory.")
                    return@addDialogueAction
                }

                val toolId = toolStore.tools[index]
                addItem(player, toolId, 1)
                closeDialogue(player)
            }

            return@on true
        }
    }

    private enum class ToolStore(val objectId: Int, vararg val tools: Int) {
        TOOLSTORE_0(Scenery.TOOLS_13699, Items.SAW_8794, Items.CHISEL_1755, Items.HAMMER_2347, Items.SHEARS_1735),
        TOOLSTORE_1(Scenery.TOOLS_13700, Items.BUCKET_1925, Items.SPADE_952, Items.TINDERBOX_590, Items.KNIFE_946),
        TOOLSTORE_2(Scenery.TOOLS_13701, Items.BROWN_APRON_1757, Items.GLASSBLOWING_PIPE_1785, Items.NEEDLE_1733),
        TOOLSTORE_3(Scenery.TOOLS_13702, Items.AMULET_MOULD_1595, Items.NECKLACE_MOULD_1597, Items.RING_MOULD_1592, Items.HOLY_MOULD_1599, Items.TIARA_MOULD_5523),
        TOOLSTORE_4(Scenery.TOOLS_13703, Items.RAKE_5341, Items.SPADE_952, Items.TROWEL_676, Items.SEED_DIBBER_5343, Items.WATERING_CAN_5331),
        ;

        companion object {
            fun forId(objectId: Int): ToolStore? = values().find { it.objectId == objectId }
        }
    }

    companion object {
        private val TOOL_IDS = intArrayOf(
            Scenery.TOOLS_13699,
            Scenery.TOOLS_13700,
            Scenery.TOOLS_13701,
            Scenery.TOOLS_13702,
            Scenery.TOOLS_13703
        )
    }
}
