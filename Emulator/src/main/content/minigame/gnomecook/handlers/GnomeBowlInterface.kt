package content.minigame.gnomecook.handlers

import core.api.*
import core.game.node.entity.skill.Skills
import core.game.interaction.InterfaceListener
import org.rs.consts.Items

class GnomeBowlInterface : InterfaceListener {

    private val gnomeBowlInterface = 435

    override fun defineInterfaceListeners() {
        onOpen(gnomeBowlInterface) { player, component ->
            sendItemOnInterface(player, component.id, 3, PreparedProduct.HALF_MADE_WOR_HO.product, 1)
            sendItemOnInterface(player, component.id, 12, PreparedProduct.HALF_MADE_VEG_BA.product, 1)
            sendItemOnInterface(player, component.id, 21, PreparedProduct.HALF_MADE_TAN_TO.product, 1)
            sendItemOnInterface(player, component.id, 34, PreparedProduct.HALF_MADE_CHOC_B.product, 1)
            return@onOpen true
        }

        on(gnomeBowlInterface) { player, _, _, buttonID, _, _ ->
            var hasAll = true
            val bowl: PreparedProduct? = when (buttonID) {
                3 -> PreparedProduct.HALF_MADE_WOR_HO
                12 -> PreparedProduct.HALF_MADE_VEG_BA
                21 -> PreparedProduct.HALF_MADE_TAN_TO
                34 -> PreparedProduct.HALF_MADE_CHOC_B
                else -> null
            }

            if (bowl != null) {
                if (!inInventory(player, Items.GNOME_SPICE_2169) && (bowl != PreparedProduct.HALF_MADE_CHOC_B)) {
                    sendDialogue(player, "You need gnome spices for this.")
                    return@on true
                }

                if (getStatLevel(player, Skills.COOKING) < bowl.levelReq) {
                    sendDialogue(player, "You don't have the needed level to make this.")
                    return@on true
                }

                val requiredItems = bowl.requiredItems.map { it }
                for (ingredient in requiredItems) {
                    if (!inInventory(player, ingredient)) {
                        hasAll = false
                        break
                    }
                }

                if (!hasAll) {
                    sendDialogue(player, "You don't have all the ingredients needed for this.")
                    return@on true
                }

                requiredItems.forEach { removeItem(player, it) }
                removeItem(player, Items.HALF_BAKED_BOWL_2177, Container.INVENTORY)
                addItem(player, bowl.product, 1)

                closeInterface(player)
            }

            return@on true
        }
    }

    internal enum class PreparedProduct(
        val product: Int,
        val levelReq: Int,
        vararg val requiredItems: Int
    ) {
        HALF_MADE_CHOC_B(Items.HALF_MADE_BOWL_9558, 42, Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_BAR_1973, Items.EQUA_LEAVES_2128),
        HALF_MADE_TAN_TO(Items.HALF_MADE_BOWL_9559, 40, Items.TOADS_LEGS_2152, Items.TOADS_LEGS_2152, Items.TOADS_LEGS_2152, Items.TOADS_LEGS_2152, Items.CHEESE_1985, Items.CHEESE_1985, Items.DWELLBERRIES_2126, Items.EQUA_LEAVES_2128, Items.EQUA_LEAVES_2128),
        HALF_MADE_VEG_BA(Items.HALF_MADE_BOWL_9561, 35, Items.POTATO_1942, Items.POTATO_1942, Items.ONION_1957, Items.ONION_1957),
        HALF_MADE_WOR_HO(Items.HALF_MADE_BOWL_9563, 30, Items.KING_WORM_2162, Items.KING_WORM_2162, Items.KING_WORM_2162, Items.KING_WORM_2162, Items.ONION_1957, Items.ONION_1957),
    }
}
