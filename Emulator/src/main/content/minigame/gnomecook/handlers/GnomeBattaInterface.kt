package content.minigame.gnomecook.handlers

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Items

class GnomeBattaInterface : InterfaceListener {
    private val gnomeBattaInterface = 434

    override fun defineInterfaceListeners() {
        onOpen(gnomeBattaInterface) { player, component ->
            sendItemOnInterface(player, component.id, 3, Items.PREMADE_FRT_BATTA_2225, 1)
            sendItemOnInterface(player, component.id, 14, Items.PREMADE_TD_BATTA_2221, 1)
            sendItemOnInterface(player, component.id, 25, Items.PREMADE_WM_BATTA_2219, 1)
            sendItemOnInterface(player, component.id, 34, Items.PREMADE_VEG_BATTA_2227, 1)
            sendItemOnInterface(player, component.id, 47, Items.PREMADE_C_PLUST_BATTA_2223, 1)
            return@onOpen true
        }

        on(gnomeBattaInterface) { player, _, _, buttonID, _, _ ->
            var hasAll = true
            val batta: CookedProduct? =
                when (buttonID) {
                    3 -> CookedProduct.HALF_MADE_FR
                    14 -> CookedProduct.HALF_MADE_TO
                    25 -> CookedProduct.HALF_MADE_WO
                    34 -> CookedProduct.HALF_MADE_VE
                    47 -> CookedProduct.HALF_MADE_CT
                    else -> null
                }

            if (batta != null) {
                if (getStatLevel(player, Skills.COOKING) < batta.levelReq) {
                    sendDialogue(player, "You don't have the needed level to make this.")
                    return@on true
                }

                val requiredItems = batta.requiredItems.map { it }
                if (!inInventory(player, Items.GNOME_SPICE_2169) &&
                    (batta == CookedProduct.HALF_MADE_TO || batta == CookedProduct.HALF_MADE_WO)
                ) {
                    sendDialogue(player, "You need gnome spices for this.")
                    return@on true
                }

                for (ingredient in requiredItems) {
                    if (!inInventory(player, ingredient)) {
                        hasAll = false
                        break
                    }
                }

                if (!hasAll) {
                    sendDialogue(player, "You don't have the ingredients needed for this.")
                    return@on true
                }

                requiredItems.forEach { removeItem(player, it) }
                removeItem(player, Items.HALF_BAKED_BATTA_2249, Container.INVENTORY)
                addItem(player, batta.product, 1)

                rewardXP(player, Skills.COOKING, batta.experience)
                closeInterface(player)
            }

            return@on true
        }
    }

    internal enum class CookedProduct(
        val product: Int,
        val levelReq: Int,
        val experience: Double,
        vararg val requiredItems: Int,
    ) {
        HALF_MADE_CT(Items.HALF_MADE_BATTA_9478, 29, 40.0, Items.TOMATO_1982, Items.CHEESE_1985),
        HALF_MADE_FR(
            Items.HALF_MADE_BATTA_9480,
            25,
            40.0,
            Items.EQUA_LEAVES_2128,
            Items.EQUA_LEAVES_2128,
            Items.LIME_CHUNKS_2122,
            Items.ORANGE_CHUNKS_2110,
            Items.PINEAPPLE_CHUNKS_2116,
        ),
        HALF_MADE_TO(Items.HALF_MADE_BATTA_9482, 26, 40.0, Items.EQUA_LEAVES_2128, Items.CHEESE_1985, Items.TOADS_LEGS_2152),
        HALF_MADE_VE(
            Items.HALF_MADE_BATTA_9483,
            28,
            40.0,
            Items.TOMATO_1982,
            Items.TOMATO_1982,
            Items.CHEESE_1985,
            Items.DWELLBERRIES_2126,
            Items.ONION_1957,
            Items.CABBAGE_1965,
        ),
        HALF_MADE_WO(Items.HALF_MADE_BATTA_9485, 27, 40.0, Items.KING_WORM_2162, Items.CHEESE_1985),
    }
}
