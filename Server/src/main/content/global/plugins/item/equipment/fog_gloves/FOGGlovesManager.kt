package content.global.plugins.item.equipment.fog_gloves

import core.api.*
import core.game.node.entity.player.Player
import core.tools.colorize
import shared.consts.Items
import kotlin.math.min

private val MAX_CHARGES = intArrayOf(100, 100, 100, 100, 1000, 1000, 1000, 1000, 1000, 1000)

/**
 * Manager to help updating Fist of Guthix glove charges.
 * @author RiL
 */
class FOGGlovesManager {

    companion object{
        /**
         * Reduces the charge of the hand equip by [charges].
         * Return the number of used charges.
         */
        @JvmStatic
        fun updateCharges(player: Player, charges: Int = 1): Int {
            val gloves = getItemFromEquipment(player, EquipmentSlot.HANDS) ?: return 0
            gloves.charge = min(gloves.charge, MAX_CHARGES[gloves.id - Items.IRIT_GLOVES_12856])
            if (gloves.charge - charges <= 0) {
                removeItem(player, gloves, Container.EQUIPMENT)
                sendMessage(player, colorize("%RThe charges in your gloves have been used up and they crumble to dust."))
                return gloves.charge
            }
            gloves.charge -= charges
            return charges
        }
    }
}
