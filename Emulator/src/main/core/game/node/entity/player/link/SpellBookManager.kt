package core.game.node.entity.player.link

import core.api.setVarbit
import core.game.component.Component
import core.game.node.entity.combat.spell.MagicSpell
import core.game.node.entity.player.Player
import shared.consts.Components
import java.util.*

/**
 * Manages a player's spellbook, allowing switching between different magic spellbooks.
 */
class SpellBookManager {
    /**
     * The currently active spellbook, defaulting to Modern.
     */
    var spellBook: Int = SpellBook.MODERN.interfaceId

    /**
     * Sets the spellbook to a new type.
     * @param book The spellbook to switch to.
     */
    fun setSpellBook(book: SpellBook) {
        this.spellBook = book.interfaceId
    }

    /**
     * Updates the player's interface to reflect the currently selected spellbook.
     * @param player The player whose interface should be updated.
     */
    fun update(player: Player) {
        player.interfaceManager.openTab(Component(spellBook))
        SpellBook.forInterface(spellBook)?.let { setVarbit(player, 357, it.ordinal) }
    }

    /**
     * Represents different spellbooks available to a player.
     * @property interfaceId The interface ID associated with the spellbook.
     */
    enum class SpellBook(
        val interfaceId: Int,
    ) {
        MODERN(Components.MAGIC_192),
        ANCIENT(Components.MAGIC_ZAROS_193),
        LUNAR(Components.MAGIC_LUNAR_430)
        ;

        /**
         * Stores registered spells for this spellbook, mapped by button ID.
         */
        private val spells: HashMap<Int, MagicSpell> = HashMap()

        /**
         * Registers a spell in this spellbook.
         * @param buttonId The button ID associated with the spell.
         * @param spell The spell to register.
         */
        fun register(
            buttonId: Int,
            spell: MagicSpell,
        ) {
            spell.spellId = buttonId
            spells[buttonId] = spell
        }

        /**
         * Retrieves a spell by its button ID.
         * @param buttonId The button ID of the spell.
         * @return The spell associated with the button ID, or null if not found.
         */
        fun getSpell(buttonId: Int): MagicSpell? = spells[buttonId]

        companion object {
            /**
             * Finds a spellbook based on its interface ID.
             * @param id The interface ID to look for.
             * @return The corresponding spellbook, or null if not found.
             */
            @JvmStatic
            fun forInterface(id: Int): SpellBook? = values().find { it.interfaceId == id }
        }
    }
}