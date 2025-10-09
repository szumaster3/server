package content.region.fremennik.lighthouse.quest.horror.dialogue

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Vars

/**
 * Represents the Strange Wall dialogue.
 */
class StrangeWallDialogue(private val items: Int) : DialogueFile() {

    private val itemVbits = mapOf(
        Items.BRONZE_ARROW_882  to Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_ARROW_45,
        Items.BRONZE_SWORD_1277 to Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_SWORD_44,
        Items.AIR_RUNE_556      to Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_AIR_RUNE_43,
        Items.FIRE_RUNE_554     to Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_FIRE_RUNE_40,
        Items.EARTH_RUNE_557    to Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_EARTH_RUNE_42,
        Items.WATER_RUNE_555    to Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_WATER_RUNE_41
    )

    override fun handle(componentID: Int, buttonID: Int) {
        val player = player ?: return
        when (stage) {
            0 -> {
                sendDialogue(player, "I don't think I'll get that back if I put it in there.")
                stage++
            }
            1 -> {
                setTitle(player, 2)
                sendOptions(player, "Really place the rune into the door?", "Yes", "No")
                stage++
            }
            2 -> when (buttonID) {
                1 -> handleItemAction()
                2 -> end()
            }
        }
    }

    private fun handleItemAction() {
        val player = player ?: return
        val vbit = itemVbits[items] ?: return
        val itemName = getItemName(items).lowercase()

        end()
        if (!removeItem(player, Item(items, 1), Container.INVENTORY)) {
            sendMessage(player, "Nothing interesting happens.")
            return
        }

        sendMessage(player, "You place a $itemName into the slot in the wall.")
        setVarbit(player, vbit, 1, true)

        /*
         * Increment strange wall door unlock only if all items are placed.
         */
        val allPlaced = itemVbits.values.all { getVarbit(player, it) == 1 }
        if (allPlaced) {
            incrementVarbit(player, Vars.VARBIT_HORROR_FROM_THE_DEEP_STRANGE_WALL_UNLOCKED_35, 1, true)
        }
    }
}