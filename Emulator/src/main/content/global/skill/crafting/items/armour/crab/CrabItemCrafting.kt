package content.global.skill.crafting.items.armour.crab

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Items

class CrabItemCrafting : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles crafting the crab claws.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, Items.FRESH_CRAB_CLAW_7536) { player, _, with ->
            if (getStatLevel(player, Skills.CRAFTING) < 15) {
                sendDialogue(player, "You need a crafting level of at least 15 in order to do this.")
                return@onUseWith false
            }
            if (removeItem(player, with.asItem())) {
                addItem(player, Items.CRAB_CLAW_7537)
                rewardXP(player, Skills.CRAFTING, 32.5)
            }
            return@onUseWith true
        }

        /*
         * Handles crafting the crab helmet.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, Items.FRESH_CRAB_SHELL_7538) { player, _, with ->
            if (getStatLevel(player, Skills.CRAFTING) < 15) {
                sendDialogue(player, "You need a crafting level of at least 15 in order to do this.")
                return@onUseWith false
            }
            if (removeItem(player, with.asItem())) {
                addItem(player, Items.CRAB_HELMET_7539)
                rewardXP(player, Skills.CRAFTING, 32.5)
            }
            return@onUseWith true
        }
    }
}
