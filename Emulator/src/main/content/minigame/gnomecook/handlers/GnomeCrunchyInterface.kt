package content.minigame.gnomecook.handlers

import core.api.*
import core.game.node.entity.skill.Skills
import core.game.interaction.InterfaceListener
import org.rs.consts.Items

class GnomeCrunchyInterface : InterfaceListener {

    private val gnomeCrunchyInterface = 437

    override fun defineInterfaceListeners() {
        onOpen(gnomeCrunchyInterface) { player, component ->
            sendItemOnInterface(player, component.id, 3, Items.TOAD_CRUNCHIES_9538, 1)
            sendItemOnInterface(player, component.id, 10, Items.SPICY_CRUNCHIES_9540, 1)
            sendItemOnInterface(player, component.id, 17, Items.WORM_CRUNCHIES_9542, 1)
            sendItemOnInterface(player, component.id, 26, Items.CHOCCHIP_CRUNCHIES_9544, 1)
            return@onOpen true
        }

        on(gnomeCrunchyInterface) { player, _, _, buttonID, _, _ ->
            var hasAll = true
            val crunchy: HalfMadeCrunchy? = when (buttonID) {
                3 -> HalfMadeCrunchy.TOAD
                10 -> HalfMadeCrunchy.SPICY
                17 -> HalfMadeCrunchy.WORM
                26 -> HalfMadeCrunchy.CHOCCHIP
                else -> null
            }

            if (crunchy != null) {
                if (getStatLevel(player, Skills.COOKING) < crunchy.reqLevel) {
                    sendDialogue(player, "You don't have the required level to make these.")
                    return@on true
                }

                val requiredItems = crunchy.requiredItems.map { it }
                if (!inInventory(player, Items.GNOME_SPICE_2169)) {
                    sendDialogue(player, "You need some gnome spice to make these.")
                    return@on true
                }

                for (ingredient in requiredItems) {
                    if (!inInventory(player, ingredient)) {
                        hasAll = false
                        break
                    }
                }

                if (!hasAll) {
                    sendDialogue(player, "You don't have the required ingredients to make these.")
                    return@on true
                }

                requiredItems.forEach { removeItem(player, it) }
                removeItem(player, Items.HALF_BAKED_CRUNCHY_2201, Container.INVENTORY)
                addItem(player, crunchy.product, 1)

                rewardXP(player, Skills.COOKING, 30.0)
                closeInterface(player)
            }

            return@on true
        }
    }

    internal enum class HalfMadeCrunchy(
        val product: Int,
        val reqLevel: Int,
        vararg val requiredItems: Int
    ) {
        CHOCCHIP(Items.HALF_MADE_CRUNCHY_9577, 16, Items.CHOCOLATE_BAR_1973, Items.CHOCOLATE_BAR_1973),
        SPICY(Items.HALF_MADE_CRUNCHY_9579, 12, Items.EQUA_LEAVES_2128, Items.EQUA_LEAVES_2128),
        TOAD(Items.HALF_MADE_CRUNCHY_9581, 10, Items.TOADS_LEGS_2152, Items.TOADS_LEGS_2152),
        WORM(Items.HALF_MADE_CRUNCHY_9583, 14, Items.EQUA_LEAVES_2128, Items.KING_WORM_2162, Items.KING_WORM_2162),
    }
}
