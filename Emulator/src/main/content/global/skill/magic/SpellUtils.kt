package content.global.skill.magic

import core.game.node.entity.combat.spell.CombinationRune
import core.game.node.entity.combat.spell.MagicStaff
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Components

object SpellUtils {
    @JvmStatic
    fun usingStaff(
        player: Player,
        rune: Int,
    ): Boolean {
        val weapon = player.equipment[3] ?: return false
        val staff = MagicStaff.forId(rune) ?: return false
        return staff.staves.any { weapon.id == it }
    }

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

    @JvmStatic
    fun attackableNPC(npc: NPC): Boolean {
        return npc.definition.hasAction("attack")
    }

    @JvmStatic
    fun getBookFromInterface(id: Int): String {
        return when (id) {
            Components.MAGIC_192 -> "modern"
            Components.MAGIC_ZAROS_193 -> "ancient"
            Components.MAGIC_LUNAR_430 -> "lunar"
            else -> "none"
        }
    }
}
