package content.global.skill.construction.decoration.workshop

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Scenery

class ToolSpace : InteractionListener {
    val sceneryIDs = ToolStore.values().map { it.objectId }.toIntArray()

    private enum class ToolStore(
        val objectId: Int,
        vararg val tools: Int,
    ) {
        TOOLSTORE_1(Scenery.TOOLS_13699, Items.SAW_8794, Items.CHISEL_1755, Items.HAMMER_2347, Items.SHEARS_1735),
        TOOLSTORE_2(Scenery.TOOLS_13700, Items.BUCKET_1925, Items.SPADE_952, Items.TINDERBOX_590),
        TOOLSTORE_3(Scenery.TOOLS_13701, Items.BROWN_APRON_1757, Items.GLASSBLOWING_PIPE_1785, Items.NEEDLE_1733),
        TOOLSTORE_4(
            Scenery.TOOLS_13702,
            Items.AMULET_MOULD_1595,
            Items.NECKLACE_MOULD_1597,
            Items.RING_MOULD_1592,
            Items.HOLY_MOULD_1599,
            Items.TIARA_MOULD_5523,
        ),
        TOOLSTORE_5(
            Scenery.TOOLS_13703,
            Items.RAKE_5341,
            Items.SPADE_952,
            Items.TROWEL_676,
            Items.SEED_DIBBER_5343,
            Items.WATERING_CAN_5331,
        ),
        ;

        companion object {
            fun forId(objectId: Int): ToolStore? = values().find { it.objectId == objectId }
        }
    }

    override fun defineListeners() {
        on(sceneryIDs, IntType.SCENERY, "search") { player, node ->
            ToolStore.forId(node.id)?.let { toolStore ->
                val optionName = toolStore.tools.map { getItemName(it) }.toTypedArray()
                sendDialogueOptions(player, "Select a tool", *optionName)
                addDialogueAction(player) { player, buttonID ->
                    val index = buttonID - 2
                    if (index in toolStore.tools.indices) {
                        val item = Item(toolStore.tools[index], 1)
                        if (freeSlots(player) == 0) {
                            sendDialogue(player, "You have no space in your inventory.")
                            return@addDialogueAction
                        }
                        addItem(player, item.id, 1)
                    }
                }
            }
            return@on true
        }
    }
}
