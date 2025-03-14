package content.global.skill.herblore.assistance

import content.data.consumables.Consumables
import core.api.*
import core.api.interaction.decantContainer
import core.game.consumable.Potion
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Sounds

class Decanting : InteractionListener {
    override fun defineListeners() {
        on(IntType.NPC, "decant") { player, node ->
            val (toRemove, toAdd) = decantContainer(player.inventory)
            for (item in toRemove) removeItem(player, item)
            for (item in toAdd) addItem(player, item.id, item.amount)

            sendNPCDialogue(player, node.id, "There you go!")
            addDialogueAction(player) { p, button ->
                if (button >= 1) sendPlayerDialogue(p, "Thanks!")
            }

            return@on true
        }

        onUseWith(IntType.ITEM, potions, *potions) { player, used, with ->
            if (used !is Item) return@onUseWith false
            if (with !is Item) return@onUseWith false

            val potionUsed = Consumables.getConsumableById(used.id)?.consumable as? Potion ?: return@onUseWith false
            val potionWith = Consumables.getConsumableById(with.id)?.consumable as? Potion ?: return@onUseWith false

            if (potionUsed != potionWith) {
                return@onUseWith false
            }

            val usedDose = potionUsed.getDose(used)
            val withDose = potionWith.getDose(with)

            if (usedDose == 4 || withDose == 4) {
                return@onUseWith false
            }

            val totalDosage = usedDose + withDose
            val fullDoses = totalDosage / 4
            val leftoverDose = totalDosage % 4

            if (fullDoses != 0) {
                replaceSlot(player, with.slot, Item(potionWith.ids.first()), with, Container.INVENTORY)
            }

            if (leftoverDose != 0 && fullDoses == 0) {
                replaceSlot(
                    player,
                    with.slot,
                    Item(potionUsed.ids[potionUsed.ids.size - totalDosage]),
                    with,
                    Container.INVENTORY,
                )
            } else if (leftoverDose != 0) {
                replaceSlot(
                    player,
                    used.slot,
                    Item(potionUsed.ids[potionUsed.ids.size - leftoverDose]),
                    used,
                    Container.INVENTORY,
                )
            }

            if (leftoverDose == 0 || fullDoses == 0) {
                replaceSlot(player, used.slot, Item(Items.VIAL_229), used, Container.INVENTORY)
            }

            val amountString =
                when {
                    totalDosage >= 4 -> "four"
                    totalDosage == 3 -> "three"
                    else -> "two"
                }

            sendMessage(player, "You have combined the liquid into $amountString doses.")
            playAudio(player, Sounds.LIQUID_2401)

            return@onUseWith true
        }
    }

    companion object {
        val potions = Consumables.potions.toIntArray()
    }
}
