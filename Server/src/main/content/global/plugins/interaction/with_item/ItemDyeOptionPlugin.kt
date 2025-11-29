package content.global.plugins.interaction.with_item

import content.data.Capes
import content.data.Dyes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items
import java.util.*

/**
 * Plugin handling item dyeing, including mixing dyes,
 * dyeing capes, and dyeing goblin armor.
 */
class ItemDyeOptionPlugin : InteractionListener {

    companion object {
        private val DYES = Dyes.values().map { it.id }.toIntArray()
        private val CAPES = Capes.values().map { it.capeId }.toIntArray()
        private val GOBLIN_MAIL = intArrayOf(Items.GOBLIN_MAIL_288, Items.RED_GOBLIN_MAIL_9054, Items.ORANGE_GOBLIN_MAIL_286, Items.YELLOW_GOBLIN_MAIL_9056, Items.GREEN_GOBLIN_MAIL_9057, Items.BLUE_GOBLIN_MAIL_287, Items.PURPLE_GOBLIN_MAIL_9058, Items.BLACK_GOBLIN_MAIL_9055, Items.PINK_GOBLIN_MAIL_9059)
    }

    override fun defineListeners() {

        /*
         * Handles mix two dyes together.
         */

        onUseWith(IntType.ITEM, DYES, *DYES) { player, used, with ->
            handleDyeCombine(player, used.id, with.id)
            return@onUseWith true
        }

        /*
         * Handles coloring the capes with a dye.
         */

        onUseWith(IntType.ITEM, DYES, *CAPES) { player, used, with ->
            Capes.forDyeId(used.id)?.let { cape ->
                if (!removeItem(player, used.id)) return@onUseWith false
                replaceSlot(player, with.index, Item(cape.capeId))
                player.sendMessage("You dye the cape.")
                return@onUseWith true
            } ?: run {
                player.sendMessage("This dye cannot be used with this cape.")
                return@onUseWith false
            }
        }

        /*
         * Handles dye the goblin mail for (Goblin Diplomacy quest).
         */

        onUseWith(IntType.ITEM, DYES, *GOBLIN_MAIL) { player, used, with ->
            if (with.id == Items.GOBLIN_MAIL_288) {
                dyeGoblinMail(player, used.id, with.id, with.index)
            } else {
                player.sendMessage("That item is already dyed.")
            }
            return@onUseWith true
        }

        /*
         * Handles message when trying to wear goblin armor.
         */

        onEquip(GOBLIN_MAIL){ player, _ ->
            sendMessage(player,"That armour is too small for a human.")
            return@onEquip false
        }
    }

    /**
     * Mixes two different dyes to create a new color.
     */
    private fun handleDyeCombine(player: Player, primaryId: Int, secondaryId: Int): Boolean {
        val firstColor = Dyes.forId(primaryId) ?: return false
        val secondColor = Dyes.forId(secondaryId) ?: return false
        if (firstColor == secondColor) return false

        val mix = when (setOf(firstColor, secondColor)) {
            setOf(Dyes.RED, Dyes.YELLOW) -> Dyes.ORANGE
            setOf(Dyes.YELLOW, Dyes.BLUE) -> Dyes.GREEN
            setOf(Dyes.RED, Dyes.BLUE) -> Dyes.PURPLE
            else -> null
        } ?: return sendMessage(player, "Those dyes don't mix together.").let { false }

        if (!inInventory(player, firstColor.id) || !inInventory(player, secondColor.id)) {
            sendMessage(player, "You don't have the required dyes to mix.")
            return false
        }

        val article = if (mix.name.first().lowercaseChar() in "aeiou") "an" else "a"
        if(removeItem(player, firstColor) && removeItem(player, secondColor))
        {
            player.animate(Animation(Animations.DYE_COMBINE_4348))
            sendMessage(player, "You mix the two dyes and make $article ${mix.name.lowercase()} dye.")
            addItemOrDrop(player, mix.id)
        }
        
        return true
    }

    /**
     * Dyes goblin armor using a dye item.
     */
    private fun dyeGoblinMail(player: Player, dyeId: Int, mailId: Int, mailSlot: Int): Boolean {
        val dye = Dyes.forId(dyeId) ?: return false
        if (mailId != Items.GOBLIN_MAIL_288) return false

        val productId = GOBLIN_MAIL.getOrNull(dye.ordinal) ?: return false
        if (!removeItem(player, Item(dye.id))) return false

        replaceSlot(player, mailSlot, Item(productId))
        player.sendMessage("You dye the goblin armour ${dye.name.lowercase(Locale.getDefault())}.")
        return true
    }
}