package content.global.plugins.interaction.with_item

import content.data.Dyes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items

/**
 * Plugin handling item coloring (dyeing), including mixing dyes,
 * dyeing capes, and dyeing goblin armor.
 */
class ItemColoringPlugin : InteractionListener {

    companion object {
        private val DYES = Dyes.values().map { it.dyeId }.toIntArray()
        private val CAPES = Dyes.values().map { it.capeId }.toIntArray()
        private val GOBLIN_MAIL = Dyes.values().map { it.goblinMailId }.toIntArray()
    }

    override fun defineListeners() {

        /*
         * Handles mix two dyes together.
         */

        onUseWith(IntType.ITEM, DYES, *DYES) { player, used, with ->
            val first = Dyes.forId(used.id)
            val second = Dyes.forId(with.id)

            if (first == null || second == null) return@onUseWith false
            if (first == second) return@onUseWith false

            val mix = when (setOf(first, second))
            {
                setOf(Dyes.RED, Dyes.YELLOW)  -> Dyes.ORANGE
                setOf(Dyes.YELLOW, Dyes.BLUE) -> Dyes.GREEN
                setOf(Dyes.RED, Dyes.BLUE)    -> Dyes.PURPLE
                else -> {
                    sendMessage(player, "Those dyes don't mix together.")
                    return@onUseWith false
                }
            }

            if (!inInventory(player, first.dyeId) || !inInventory(player, second.dyeId))
            {
                sendMessage(player, "You don't have the required dyes to mix.")
                return@onUseWith false
            }

            val article = if (mix.name.first().lowercaseChar() in "aeiou") "an" else "a"
            if (removeItem(player, first.dyeId) && removeItem(player, second.dyeId))
            {
                player.animate(Animation(Animations.DYE_COMBINE_4348))
                sendMessage(player, "You mix the two dyes and make $article ${mix.name.lowercase()} dye.")
                addItemOrDrop(player, mix.dyeId, 1)
            }
            return@onUseWith true
        }

        /*
         * Handles coloring the capes with a dye.
         */

        onUseWith(IntType.ITEM, DYES, *CAPES) { player, used, with ->
            val dye = Dyes.forId(used.id)
            if (dye != null)
            {
                if (!removeItem(player, used.id)) return@onUseWith false
                replaceSlot(player, with.asItem().slot, Item(dye.capeId))
                sendMessage(player, "You dye the cape ${dye.name.lowercase()}.")
                return@onUseWith true
            }

            sendMessage(player, "This dye cannot be used with this item.")
            return@onUseWith false
        }

        /*
         * Handles dye the goblin mail for (Goblin Diplomacy quest).
         */

        onUseWith(IntType.ITEM, DYES, *GOBLIN_MAIL + Items.GOBLIN_MAIL_288) { player, used, with ->
            val dye = Dyes.forId(used.id)
            if (dye == null) {
                sendMessage(player, "This dye cannot be used.")
                return@onUseWith false
            }

            when (with.id) {
                Items.GOBLIN_MAIL_288 -> {
                    if (!removeItem(player, used.id)) return@onUseWith false
                    val dyedMailId = GOBLIN_MAIL[dye.ordinal]
                    replaceSlot(player, with.asItem().slot, Item(dyedMailId))
                    sendMessage(player, "You dye the goblin armour ${dye.name.lowercase()}.")
                }
                else -> {
                    sendMessage(player, "That item is already dyed.")
                }
            }
            return@onUseWith true
        }

        /*
         * Handles message when trying to wear goblin armor.
         */

        on(GOBLIN_MAIL + Items.GOBLIN_MAIL_288, IntType.ITEM, "wear") { player, _ ->
            sendMessage(player, "That armour is too small for a human.")
            return@on false
        }
    }

}
