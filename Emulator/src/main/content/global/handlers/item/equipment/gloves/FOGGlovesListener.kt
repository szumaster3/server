package content.global.handlers.item.equipment.gloves

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.tools.colorize
import org.rs.consts.Items
import kotlin.math.min

class FOGGlovesListener : InteractionListener {
    companion object {
        val MAX_CHARGES = intArrayOf(100, 100, 100, 100, 1000, 1000, 1000, 1000, 1000, 1000)
        private val FOG_GLOVES = (Items.IRIT_GLOVES_12856..Items.EARTH_RUNECRAFTING_GLOVES_12865).toIntArray()

        @JvmStatic
        fun updateCharges(
            player: Player,
            charges: Int = 1,
        ): Int {
            val gloves = getItemFromEquipment(player, EquipmentSlot.HANDS) ?: return 0
            gloves.charge = min(gloves.charge, MAX_CHARGES[gloves.id - Items.IRIT_GLOVES_12856])
            if (gloves.charge - charges <= 0) {
                removeItem(player, gloves, Container.EQUIPMENT)
                player.sendMessage(colorize("%RThe charges in your gloves have been used up and they crumble to dust."))
                return gloves.charge
            }
            gloves.charge -= charges
            return charges
        }
    }

    override fun defineListeners() {
        on(FOG_GLOVES, IntType.ITEM, "inspect") { player, node ->
            sendMessage(
                player,
                "${node.name}: ${
                    min(
                        node.asItem().charge,
                        MAX_CHARGES[node.id - Items.IRIT_GLOVES_12856],
                    )
                } charge left.",
            )
            return@on true
        }
    }
}
