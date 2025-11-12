package content.global.plugins.item

import core.cache.def.impl.ItemDefinition
import core.game.interaction.InteractionListener
import core.game.world.GameWorld
import shared.consts.Items

/**
 * Represents the items that reduce player weight on equip.
 */
class ReduceWeightItemPlugin : InteractionListener {

    private val AGILE_TOP = intArrayOf(Items.AGILE_TOP_14696, Items.AGILE_TOP_14697)
    private val AGILE_LEGS = intArrayOf(Items.AGILE_LEGS_14698, Items.AGILE_LEGS_14699)
    private val BOOTS_OF_LIGHTNESS = intArrayOf(Items.BOOTS_OF_LIGHTNESS_88)

    override fun defineListeners() {
        applyWeightModifiers()
    }

    /**
     * Applies negative weight modifiers.
     */
    private fun applyWeightModifiers() {
        if (!GameWorld.settings!!.isMembers) {
            return
        }

        AGILE_TOP.forEach { id ->
            val def = ItemDefinition.forId(id)
            def.handlers["weight"] = -12.0
        }

        AGILE_LEGS.forEach { id ->
            val def = ItemDefinition.forId(id)
            def.handlers["weight"] = -10.0
        }

        BOOTS_OF_LIGHTNESS.forEach { id ->
            val def = ItemDefinition.forId(id)
            def.handlers["weight"] = -4.534
        }
    }

}
