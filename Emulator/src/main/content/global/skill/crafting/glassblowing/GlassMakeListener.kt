package content.global.skill.crafting.glassblowing

import core.api.amountInInventory
import core.api.inInventory
import core.api.sendMessage
import core.api.skill.sendSkillDialogue
import core.api.submitIndividualPulse
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Scenery
import kotlin.math.min

class GlassMakeListener : InteractionListener {
    companion object {
        private const val SODA_ASH = Items.SODA_ASH_1781
        private const val BUCKET_OF_SAND = Items.BUCKET_OF_SAND_1783
        private const val MOLTEN_GLASS = Items.MOLTEN_GLASS_1775
        private val INPUTS = intArrayOf(SODA_ASH, BUCKET_OF_SAND)
        private val FURNACES =
            intArrayOf(
                Scenery.FURNACE_4304,
                Scenery.FURNACE_6189,
                Scenery.LAVA_FORGE_9390,
                Scenery.FURNACE_11010,
                Scenery.FURNACE_11666,
                Scenery.FURNACE_12100,
                Scenery.FURNACE_12809,
                Scenery.FURNACE_18497,
                Scenery.FURNACE_26814,
                Scenery.FURNACE_30021,
                Scenery.FURNACE_30510,
                Scenery.FURNACE_36956,
                Scenery.FURNACE_37651,
            )
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, INPUTS, *FURNACES) { player, _, _ ->
            if (!inInventory(player, SODA_ASH, 1) || !inInventory(player, BUCKET_OF_SAND, 1)) {
                sendMessage(player, "You need at least one heap of soda ash and one bucket of sand to do this.")
                return@onUseWith true
            }
            sendSkillDialogue(player) {
                withItems(MOLTEN_GLASS)
                create { id, amount ->
                    submitIndividualPulse(player, GlassMakePulse(player, id, amount))
                }
                calculateMaxAmount { _ ->
                    min(amountInInventory(player, SODA_ASH), amountInInventory(player, BUCKET_OF_SAND))
                }
            }

            return@onUseWith true
        }
    }
}
