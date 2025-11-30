package content.global.plugins.interaction.with_item

import content.data.Dyes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import java.util.*
import shared.consts.Animations
import shared.consts.Items

/**
 * Plugin handling item dyeing, including mixing dyes,
 * dyeing capes, and dyeing goblin armor.
 */
class ItemDyeingPlugin : InteractionListener {

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
            handleDyeCombine(player, used.id, with.id)
            return@onUseWith true
        }

        /*
         * Handles coloring the capes with a dye.
         */

        onUseWith(IntType.ITEM, DYES, *CAPES) { player, used, with ->
            val dye = Dyes.forId(used.id)
            if (dye != null && dye.capeId == with.id) {
                if (!removeItem(player, used.id)) return@onUseWith false
                replaceSlot(player, with.index, Item(dye.capeId))
                sendMessage(player, "You dye the cape.")
                return@onUseWith true
            } else {
                sendMessage(player, "This dye cannot be used with this cape.")
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
                sendMessage(player, "That item is already dyed.")
            }
            return@onUseWith true
        }

        /*
         * Handles message when trying to wear goblin armor.
         */

        onEquip(GOBLIN_MAIL) { player, _ ->
            sendMessage(player, "That armour is too small for a human.")
            return@onEquip false
        }
    }

    /**
     * Mixes two different dyes to create a new color.
     */
    private fun handleDyeCombine(player: Player, primaryId: Int, secondaryId: Int): Boolean {
        val first = Dyes.forId(primaryId) ?: return false
        val second = Dyes.forId(secondaryId) ?: return false
        if (first == second) return false

        val mix =
            when (setOf(first, second)) {
                setOf(Dyes.RED, Dyes.YELLOW) -> Dyes.ORANGE
                setOf(Dyes.YELLOW, Dyes.BLUE) -> Dyes.GREEN
                setOf(Dyes.RED, Dyes.BLUE) -> Dyes.PURPLE
                else -> return sendMessage(player, "Those dyes don't mix together.").let { false }
            }

        if (!inInventory(player, first.dyeId) || !inInventory(player, second.dyeId)) {
            sendMessage(player, "You don't have the required dyes to mix.")
            return false
        }

        val article = if (mix.name.first().lowercaseChar() in "aeiou") "an" else "a"
        if (removeItem(player, first.dyeId) && removeItem(player, second.dyeId)) {
            player.animate(Animation(Animations.DYE_COMBINE_4348))
            sendMessage(player, "You mix the two dyes and make $article ${mix.name.lowercase()} dye.")
            addItemOrDrop(player, mix.dyeId)
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
        if (!removeItem(player, Item(dye.dyeId))) return false

        replaceSlot(player, mailSlot, Item(productId))
        player.sendMessage("You dye the goblin armour ${dye.name.lowercase(Locale.getDefault())}.")
        return true
    }
}
