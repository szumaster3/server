package content.global.skill.crafting.items.armour.leather.soft

import core.api.openInterface
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Components
import org.rs.consts.Items

enum class SoftLeather(
    val button: Int,
    val level: Int,
    val experience: Double,
    val product: Item,
) {
    ARMOUR(
        button = 28,
        level = 14,
        experience = 25.0,
        product = Item(Items.LEATHER_BODY_1129),
    ),
    GLOVES(
        button = 29,
        level = 1,
        experience = 13.8,
        product = Item(Items.LEATHER_GLOVES_1059),
    ),
    BOOTS(
        button = 30,
        level = 7,
        experience = 16.3,
        product = Item(Items.LEATHER_BOOTS_1061),
    ),
    VAMBRACES(
        button = 31,
        level = 11,
        experience = 22.0,
        product = Item(Items.LEATHER_VAMBRACES_1063),
    ),
    CHAPS(
        button = 32,
        level = 18,
        experience = 27.0,
        product = Item(Items.LEATHER_CHAPS_1095),
    ),
    COIF(
        button = 33,
        level = 38,
        experience = 37.0,
        product = Item(Items.COIF_1169),
    ),
    COWL(
        button = 34,
        level = 9,
        experience = 18.5,
        product = Item(Items.LEATHER_COWL_1167),
    ),
    ;

    companion object {
        @JvmStatic
        fun openCraftingInterface(player: Player) {
            openInterface(player, Components.LEATHER_CRAFTING_154)
        }

        @JvmStatic
        fun forButton(button: Int): SoftLeather? {
            return values().find { it.button == button }
        }
    }
}
