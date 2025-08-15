package content.global.skill.magic

import core.game.node.entity.combat.spell.CombinationRune
import core.game.node.entity.combat.spell.MagicStaff
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Components

/**
 * Utility for handling spellcasting.
 */
object SpellUtils {

    /**
     * Checks if the player is using a staff that provides the given rune.
     * @param player The player to check.
     * @param rune The rune ID to check for.
     * @return True if the player's equipped staff covers the rune.
     */
    @JvmStatic
    fun usingStaff(
        player: Player,
        rune: Int,
    ): Boolean {
        val weapon = player.equipment[3] ?: return false
        val staff = MagicStaff.forId(rune) ?: return false
        return staff.staves.any { weapon.id == it }
    }

    /**
     * Checks if the player has the required rune(s) to cast a spell.
     * Considers combo runes and staff usage.
     * Adds used runes to the "spell:runes" attribute for removal.
     * @param player The player to check.
     * @param rune The Item representing the rune and amount required.
     * @return True if the player has enough runes to cast.
     */
    @JvmStatic
    fun hasRune(
        player: Player,
        rune: Item,
    ): Boolean {
        val removeItems = player.getAttribute("spell:runes", ArrayList<Item>())

        if (usingStaff(player, rune.id)) return true

        if (player.inventory.containsItem(rune)) {
            removeItems.add(rune)
            player.setAttribute("spell:runes", removeItems)
        }

        var amtRemaining = rune.amount - player.inventory.getAmount(rune.id)

        val possibleComboRunes = Runes.forId(rune.id)?.let { CombinationRune.eligibleFor(it) }
        possibleComboRunes?.forEach { comboRune ->
            if (player.inventory.containsItem(Item(comboRune.id)) && amtRemaining > 0) {
                val amt = player.inventory.getAmount(comboRune.id)
                if (amtRemaining <= amt) {
                    removeItems.add(Item(comboRune.id, amtRemaining))
                    amtRemaining = 0
                } else {
                    removeItems.add(Item(comboRune.id, amt))
                    amtRemaining -= amt
                }
            }
        }

        player.setAttribute("spell:runes", removeItems)
        return amtRemaining <= 0
    }

    /**
     * Checks if the player has enough of a given rune, optionally sending a message if not.
     * Fills a list with items to remove when casting the spell.
     * @param player The player to check.
     * @param item The rune Item and amount required.
     * @param toRemove List to populate with runes that will be removed.
     * @param message Whether to send a message if insufficient runes.
     * @return True if the player has enough runes (including combo runes or staff usage).
     */
    @JvmStatic
    fun hasRune(
        player: Player,
        item: Item,
        toRemove: MutableList<Item?>,
        message: Boolean,
    ): Boolean {
        if (!usingStaff(player, item.id)) {
            val hasBaseRune = player.inventory.contains(item.id, item.amount)
            if (!hasBaseRune) {
                val baseAmt = player.inventory.getAmount(item.id)
                if (baseAmt > 0) toRemove.add(Item(item.id, baseAmt))

                var amtRemaining = item.amount - baseAmt
                val possibleComboRunes = CombinationRune.eligibleFor(Runes.forId(item.id)!!)

                possibleComboRunes.forEach { comboRune ->
                    if (player.inventory.containsItem(Item(comboRune.id)) && amtRemaining > 0) {
                        val amt = player.inventory.getAmount(comboRune.id)
                        if (amtRemaining < amt) {
                            toRemove.add(Item(comboRune.id, amtRemaining))
                            amtRemaining = 0
                        } else {
                            amtRemaining -= amt
                            toRemove.add(Item(comboRune.id, amt))
                        }
                    }
                }

                return if (amtRemaining <= 0) {
                    true
                } else {
                    if (message) {
                        player.packetDispatch.sendMessage("You don't have enough ${item.name}s to cast this spell.")
                    }
                    false
                }
            }
            toRemove.add(item)
            return true
        }
        return true
    }

    /**
     * Checks if an NPC is attackable.
     */
    @JvmStatic
    fun attackableNPC(npc: NPC): Boolean = npc.definition.hasAction("attack")

    /**
     * Gets the spellbook name based on interface id.
     * @param id The interface component ID.
     * @return The spellbook name ("modern", "ancient", "lunar", or "none").
     */
    @JvmStatic
    fun getBookFromInterface(id: Int): String =
        when (id) {
            Components.MAGIC_192 -> "modern"
            Components.MAGIC_ZAROS_193 -> "ancient"
            Components.MAGIC_LUNAR_430 -> "lunar"
            else -> "none"
        }
}
