package content.global.skill.crafting.items.armour.capes

import core.api.inInventory
import core.api.removeItem
import core.api.replaceSlot
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import java.util.*

class DyeableListener : InteractionListener {

    companion object {
        private val DYES = Dyes.values().map { it.item.id }.toIntArray()
        private val CAPE = Capes.values().map { it.cape.id }.toIntArray()
        private val GOBLIN_MAIL = intArrayOf(Items.GOBLIN_MAIL_288, Items.RED_GOBLIN_MAIL_9054, Items.ORANGE_GOBLIN_MAIL_286, Items.YELLOW_GOBLIN_MAIL_9056, Items.GREEN_GOBLIN_MAIL_9057, Items.BLUE_GOBLIN_MAIL_287, Items.PURPLE_GOBLIN_MAIL_9058, Items.BLACK_GOBLIN_MAIL_9055, Items.PINK_GOBLIN_MAIL_9059)
    }

    override fun defineListeners() {

        /*
         * Handles mix the dyes.
         */

        onUseWith(IntType.ITEM, DYES, *DYES) { player, used, with ->
            val usedDye = Dyes.forItem(used.asItem())
            val withDye = Dyes.forItem(with.asItem())
            if (usedDye != null && withDye != null && usedDye != withDye) {
                handleDyeCombine(player, usedDye, withDye)
                return@onUseWith true
            } else {
                return@onUseWith false
            }
        }

        /*
         * Handles dyeing the capes.
         */

        onUseWith(IntType.ITEM, DYES, *CAPE) { player, used, with ->
            val product = Capes.forDye(used.id)

            product?.let {
                if (!inInventory(player, used.id)) {
                    sendMessage(player, "You don't have the required item to make this.")
                    return@onUseWith false
                }

                if (removeItem(player, used.id)) {
                    replaceSlot(player, with.index, it.cape)
                    sendMessage(player, "You dye the cape.")
                }
                return@onUseWith true
            }

            sendMessage(player, "This dye cannot be used with this cape.")
            return@onUseWith false
        }

        /*
         * Handles dyeing goblin mails.
         */

        onUseWith(IntType.ITEM, DYES, *GOBLIN_MAIL) { player, used, with ->
            if (with.id == Items.GOBLIN_MAIL_288) {
                dyeGoblinMail(player, used.asItem(), with.asItem())
            } else {
                player.sendMessage("That item is already dyed.")
            }
            return@onUseWith true
        }

        on(GOBLIN_MAIL, IntType.ITEM, "wear") { player, _ ->
            sendMessage(player, "That armour is to small for a human.")
            return@on true
        }

    }

    /**
     * Handles combining two basic dyes into a new dye.
     *
     * @param player The player attempting to mix the dyes.
     * @param primary The first dye being used.
     * @param secondary The second dye being used.
     */
    private fun handleDyeCombine(player: Player, primary: Dyes, secondary: Dyes) {
        val anim = Animation.create(Animations.DYE_COMBINE_4348)
        val mix = when {
            (primary == Dyes.RED && secondary == Dyes.YELLOW) || (primary == Dyes.YELLOW && secondary == Dyes.RED) -> Dyes.ORANGE

            (primary == Dyes.YELLOW && secondary == Dyes.BLUE) || (primary == Dyes.BLUE && secondary == Dyes.YELLOW) -> Dyes.GREEN

            (primary == Dyes.RED && secondary == Dyes.BLUE) || (primary == Dyes.BLUE && secondary == Dyes.RED) -> Dyes.PURPLE

            else -> null
        }

        if (mix == null) {
            sendMessage(player, "Those dyes don't mix together.")
            return
        }

        if (!player.inventory.contains(primary.item.id, 1) || !player.inventory.contains(secondary.item.id, 1)) {
            sendMessage(player, "You don't have the required dyes to mix.")
            return
        }

        player.lock(1)
        player.animate(anim)
        player.inventory.remove(primary.item)
        player.inventory.remove(secondary.item)
        player.inventory.add(mix.item)

        val article = if (mix.name.lowercase().first() in "aeiou") "an" else "a"
        sendMessage(player, "You mix the two dyes and make $article ${mix.name.lowercase()} dye.")

    }

    /**
     *  Dyes a goblin mail item (Goblin Diplomacy).
     */
    private fun dyeGoblinMail(player: Player, used: Item, with: Item): Boolean {
        val dye = Dyes.forItem(used) ?: Dyes.forItem(with) ?: return false
        val mail = if (used.id == Items.GOBLIN_MAIL_288) used else if (with.id == Items.GOBLIN_MAIL_288) with else return false

        val productId = GOBLIN_MAIL[dye.item.id] ?: return false

        if (!removeItem(player, dye.item)) return false
        replaceSlot(player, mail.index, Item(productId))

        sendMessage(player, "You dye the goblin armour ${dye.name.lowercase(Locale.getDefault())}.")
        return true
    }
}
