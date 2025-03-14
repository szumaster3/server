package content.global.handlers.iface

import core.api.openDialogue
import core.game.interaction.InterfaceListener
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

class PetStoreInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.PICK_A_PUPPY_668) { player, _, _, buttonID, _, _ ->
            val index =
                when (buttonID) {
                    8 -> 0
                    3 -> 1
                    4 -> 2
                    5 -> 3
                    6 -> 4
                    7 -> 5
                    else -> 0
                }
            openDialogue(
                player,
                NPCs.PET_SHOP_OWNER_6893,
                NAMES[index],
                PUPPIES[index][RandomFunction.random(PUPPIES[index].size)],
            )
            return@on true
        }
    }

    companion object {
        private val NAMES = arrayOf("labrador", "bulldog", "dalmatian", "greyhound", "terrier", "sheepdog")
        private val PUPPIES =
            arrayOf(
                arrayOf(
                    Item(Items.LABRADOR_PUPPY_12516),
                    Item(Items.LABRADOR_PUPPY_12708),
                    Item(Items.LABRADOR_PUPPY_12710),
                ),
                arrayOf(
                    Item(Items.BULLDOG_PUPPY_12522),
                    Item(Items.BULLDOG_PUPPY_12720),
                    Item(Items.BULLDOG_PUPPY_12722),
                ),
                arrayOf(
                    Item(Items.DALMATIAN_PUPPY_12518),
                    Item(Items.DALMATIAN_PUPPY_12712),
                    Item(Items.DALMATIAN_PUPPY_12714),
                ),
                arrayOf(
                    Item(Items.GREYHOUND_PUPPY_12514),
                    Item(Items.GREYHOUND_PUPPY_12704),
                    Item(Items.GREYHOUND_PUPPY_12706),
                ),
                arrayOf(
                    Item(Items.TERRIER_PUPPY_12512),
                    Item(Items.TERRIER_PUPPY_12700),
                    Item(Items.TERRIER_PUPPY_12702),
                ),
                arrayOf(
                    Item(Items.SHEEPDOG_PUPPY_12520),
                    Item(Items.SHEEPDOG_PUPPY_12716),
                    Item(Items.SHEEPDOG_PUPPY_12718),
                ),
            )
    }
}
