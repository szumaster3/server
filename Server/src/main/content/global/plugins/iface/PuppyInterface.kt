package content.global.plugins.iface

import core.api.openDialogue
import core.game.interaction.InterfaceListener
import core.tools.RandomFunction
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the puppy interface plugin.
 * @author Ceikry
 */
class PuppyInterface : InterfaceListener {

    override fun defineInterfaceListeners() {
        on(Components.PICK_A_PUPPY_668) { player, _, _, buttonID, _, _ ->
            val index = when (buttonID) {
                8 -> 0
                3 -> 1
                4 -> 2
                5 -> 3
                6 -> 4
                7 -> 5
                else -> 0
            }
            val itemId = PUPPIES[index][RandomFunction.random(PUPPIES[index].size)]
            openDialogue(
                player,
                NPCs.PET_SHOP_OWNER_6893,
                NAMES[index],
                itemId
            )
            return@on true
        }
    }

    companion object {
        private val NAMES = arrayOf("labrador", "bulldog", "dalmatian", "greyhound", "terrier", "sheepdog")

        private val PUPPIES = arrayOf(
            arrayOf(
                Items.LABRADOR_PUPPY_12516,
                Items.LABRADOR_PUPPY_12708,
                Items.LABRADOR_PUPPY_12710
            ),
            arrayOf(
                Items.BULLDOG_PUPPY_12522,
                Items.BULLDOG_PUPPY_12720,
                Items.BULLDOG_PUPPY_12722
            ),
            arrayOf(
                Items.DALMATIAN_PUPPY_12518,
                Items.DALMATIAN_PUPPY_12712,
                Items.DALMATIAN_PUPPY_12714
            ),
            arrayOf(
                Items.GREYHOUND_PUPPY_12514,
                Items.GREYHOUND_PUPPY_12704,
                Items.GREYHOUND_PUPPY_12706
            ),
            arrayOf(
                Items.TERRIER_PUPPY_12512,
                Items.TERRIER_PUPPY_12700,
                Items.TERRIER_PUPPY_12702
            ),
            arrayOf(
                Items.SHEEPDOG_PUPPY_12520,
                Items.SHEEPDOG_PUPPY_12716,
                Items.SHEEPDOG_PUPPY_12718
            )
        )
    }
}
