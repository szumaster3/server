package content.region.fremennik.quests.horror.handlers

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.node.item.Item
import org.rs.consts.Items

class StrangeWallDialogue(
    private val items: Int,
) : DialogueFile() {
    private val itemAttributes =
        mapOf(
            Items.BRONZE_ARROW_882 to GameAttributes.QUEST_HFTD_USE_ARROW,
            Items.BRONZE_SWORD_1277 to GameAttributes.QUEST_HFTD_USE_SWORD,
            Items.AIR_RUNE_556 to GameAttributes.QUEST_HFTD_USE_AIR_RUNE,
            Items.FIRE_RUNE_554 to GameAttributes.QUEST_HFTD_USE_FIRE_RUNE,
            Items.EARTH_RUNE_557 to GameAttributes.QUEST_HFTD_USE_EARTH_RUNE,
            Items.WATER_RUNE_555 to GameAttributes.QUEST_HFTD_USE_WATER_RUNE,
        )

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val player = player ?: return
        when (stage) {
            0 -> {
                sendDialogue(player, "I don't think I'll get that back if I put it in there.")
                stage++
            }

            1 -> {
                setTitle(player, 2)
                sendDialogueOptions(player, "Really place the rune into the door?", "Yes", "No")
                stage++
            }

            2 ->
                when (buttonID) {
                    1 -> handleItemAction()
                    2 -> end()
                }
        }
    }

    private fun handleItemAction() {
        val player = player ?: return
        val attributeName = itemAttributes[items] ?: return
        val itemName = getItemName(items).lowercase()

        end()
        if (!removeItem(player, Item(items, 1), Container.INVENTORY)) {
            sendMessage(player, "Nothing interesting happens.")
        } else {
            sendMessage(player, "You place a $itemName into the slot in the wall.")
            setAttribute(player, attributeName, 1)
            player.incrementAttribute(GameAttributes.QUEST_HFTD_UNLOCK_DOOR)
        }
    }
}
