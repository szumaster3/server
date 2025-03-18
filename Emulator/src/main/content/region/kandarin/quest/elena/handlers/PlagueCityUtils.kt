package content.region.kandarin.quest.elena.handlers

import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs

object PlagueCityUtils {
    val kittenItemIds =
        intArrayOf(
            NPCs.CLOCKWORK_CAT_3598,
            NPCs.KITTEN_8217,
            NPCs.HELL_KITTEN_3505,
            NPCs.KITTEN_766,
            NPCs.KITTEN_765,
            NPCs.KITTEN_764,
            NPCs.KITTEN_763,
            NPCs.KITTEN_762,
            NPCs.KITTEN_761,
        )

    val grownCatItemIds =
        arrayOf(
            Item(Items.WITCHS_CAT_1491),
            Item(Items.PET_CAT_1561),
            Item(Items.PET_CAT_1562),
            Item(Items.PET_CAT_1563),
            Item(Items.PET_CAT_1564),
            Item(Items.PET_CAT_1565),
            Item(Items.PET_CAT_1566),
            Item(Items.OVERGROWN_CAT_1567),
            Item(Items.OVERGROWN_CAT_1568),
            Item(Items.OVERGROWN_CAT_1569),
            Item(Items.OVERGROWN_CAT_1570),
            Item(Items.OVERGROWN_CAT_1571),
            Item(Items.OVERGROWN_CAT_1572),
            Item(Items.LAZY_CAT_6551),
            Item(Items.LAZY_CAT_6552),
            Item(Items.LAZY_CAT_6553),
            Item(Items.LAZY_CAT_6554),
            Item(Items.WILY_CAT_6555),
            Item(Items.WILY_CAT_6556),
            Item(Items.WILY_CAT_6557),
            Item(Items.WILY_CAT_6558),
            Item(Items.WILY_CAT_6559),
            Item(Items.WILY_CAT_6560),
            Item(Items.HELL_CAT_7582),
            Item(Items.OVERGROWN_HELLCAT_7581),
            Item(Items.LAZY_HELL_CAT_7584),
            Item(Items.WILY_HELLCAT_7585),
        )

    val purpleCats =
        intArrayOf(
            Items.PET_CAT_14090,
            Items.LAZY_CAT_14091,
            Items.WILY_CAT_14093,
        )

    fun hasFullMournerGear(player: Player): Boolean =
        player.equipment.containsItems(
            Item(Items.GAS_MASK_1506),
            Item(Items.MOURNER_TOP_6065),
            Item(Items.MOURNER_TROUSERS_6067),
            Item(Items.MOURNER_BOOTS_6069),
            Item(Items.MOURNER_CLOAK_6070),
            Item(Items.MOURNER_GLOVES_6068),
        )

    fun hasAnCat(player: Player): Boolean =
        player.inventory.containsAtLeastOneItem(
            Item(Items.PET_CAT_1561),
            Item(Items.PET_CAT_1562),
            Item(Items.PET_CAT_1563),
            Item(Items.PET_CAT_1564),
            Item(Items.PET_CAT_1565),
            Item(Items.PET_CAT_1566),
            Item(Items.OVERGROWN_CAT_1567),
            Item(Items.OVERGROWN_CAT_1568),
            Item(Items.OVERGROWN_CAT_1569),
            Item(Items.OVERGROWN_CAT_1570),
            Item(Items.OVERGROWN_CAT_1571),
            Item(Items.OVERGROWN_CAT_1572),
            Item(Items.LAZY_CAT_6551),
            Item(Items.LAZY_CAT_6552),
            Item(Items.LAZY_CAT_6553),
            Item(Items.LAZY_CAT_6554),
            Item(Items.WILY_CAT_6555),
            Item(Items.WILY_CAT_6556),
            Item(Items.WILY_CAT_6557),
            Item(Items.WILY_CAT_6558),
            Item(Items.WILY_CAT_6559),
            Item(Items.WILY_CAT_6560),
            Item(Items.HELL_CAT_7582),
            Item(Items.OVERGROWN_HELLCAT_7581),
            Item(Items.LAZY_HELL_CAT_7584),
            Item(Items.WILY_HELLCAT_7585),
        )

    fun hasAnKitten(player: Player): Boolean =
        player.inventory.containsAtLeastOneItem(
            Item(NPCs.CLOCKWORK_CAT_3598),
            Item(NPCs.KITTEN_8217),
            Item(NPCs.HELL_KITTEN_3505),
            Item(NPCs.KITTEN_766),
            Item(NPCs.KITTEN_765),
            Item(NPCs.KITTEN_764),
            Item(NPCs.KITTEN_763),
            Item(NPCs.KITTEN_762),
            Item(NPCs.KITTEN_761),
        )
}
