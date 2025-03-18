package core.game.node.entity.player.link

import core.game.component.Component
import core.game.node.entity.combat.spell.MagicSpell
import core.game.node.entity.player.Player
import org.rs.consts.Components
import java.util.HashMap

class SpellBookManager {
    var spellBook: Int = SpellBook.MODERN.interfaceId

    fun setSpellBook(book: SpellBook) {
        this.spellBook = book.interfaceId
    }

    fun update(player: Player) {
        val spellBook = SpellBook.forInterface(this.spellBook)
        if (spellBook != null) {
            player.interfaceManager.openTab(Component(spellBook.interfaceId))
        } else {
            player.interfaceManager.openTab(Component(SpellBook.MODERN.interfaceId))
        }
    }

    enum class SpellBook(
        val interfaceId: Int,
    ) {
        MODERN(Components.MAGIC_192),

        ANCIENT(Components.MAGIC_ZAROS_193),

        LUNAR(Components.MAGIC_LUNAR_430),
        ;

        private val spells: HashMap<Int, MagicSpell> = HashMap()

        fun register(
            buttonId: Int,
            spell: MagicSpell,
        ) {
            spell.spellId = buttonId
            spells[buttonId] = spell
        }

        fun getSpell(buttonId: Int): MagicSpell? = spells[buttonId]

        companion object {
            @JvmStatic
            fun forInterface(id: Int): SpellBook? = values().find { it.interfaceId == id }
        }
    }
}
