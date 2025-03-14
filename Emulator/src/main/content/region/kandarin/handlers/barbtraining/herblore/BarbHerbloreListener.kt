package content.region.kandarin.handlers.barbtraining.herblore

import content.region.kandarin.handlers.barbtraining.BarbarianTraining
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs.consts.Animations
import org.rs.consts.Items

class BarbHerbloreListener : InteractionListener {
    override fun defineListeners() {

        for (potion in BarbarianMix.values()) {
            /*
             * Handle cases where both items are needed.
             */

            if (potion.both) {
                onUseWith(IntType.ITEM, potion.item, Items.ROE_11324) { player, used, with ->
                    handle(player, used, with)
                    return@onUseWith true
                }
            }

            /*
             * Handle cases where one of the items is caviar.
             */

            onUseWith(IntType.ITEM, potion.item, Items.CAVIAR_11326) { player, used, with ->
                handle(player, used, with)
                return@onUseWith true
            }
        }
    }

    /*
     * Handles the potion mixing process.
     */
    private fun handle(
        player: Player,
        inputPotion: Node,
        egg: Node
    ): Boolean {
        val potion = BarbarianMix.forId(inputPotion.id) ?: return false
        if (!getAttribute(player, BarbarianTraining.HERBLORE_START, false)) {
            sendMessage(player, "You must begin the relevant section of Otto Godblessed's barbarian training.")
            return true
        }
        if (!hasLevelStat(player, Skills.HERBLORE, potion.level)) {
            sendMessage(player, "You need a Herblore level of ${potion.level} to make this mix.")
            return true
        }
        if (!removeItem(player, potion.item)) {
            return false
        }

        if (!removeItem(player, egg.id)) {
            addItem(player, potion.item)
            return false
        }

        animate(player, Animations.PESTLE_MORTAR_364)
        addItem(player, potion.product)
        rewardXP(player, Skills.HERBLORE, potion.exp)
        sendMessage(player, "You combine your potion with the ${getItemName(egg.id).lowercase()}.")

        if (getAttribute(player, BarbarianTraining.HERBLORE_BASE, false)) {
            removeAttribute(player, BarbarianTraining.HERBLORE_BASE)
            setAttribute(player, BarbarianTraining.HERBLORE_FULL, true)
            sendDialogueLines(
                player,
                "You feel you have learned more of barbarian ways. Otto might wish",
                "to talk to you more."
            )
        }
        return true
    }
}
