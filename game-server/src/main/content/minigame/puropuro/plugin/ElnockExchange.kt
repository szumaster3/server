package content.minigame.puropuro.plugin

import content.global.skill.hunter.bnet.BNetTypes
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

enum class ElnockExchange(
    val button: Int,
    val configValue: Int,
    val sendItem: Int,
    val reward: Item,
    vararg val required: Item,
) {
    IMP_REPELLENT(
        23,
        444928,
        Items.PICTURE_11271,
        Item(Items.IMP_REPELLENT_11262),
        Item(Items.BABY_IMPLING_JAR_11238, 3),
        Item(Items.YOUNG_IMPLING_JAR_11240, 2),
        Item(Items.GOURM_IMPLING_JAR_11242),
    ),

    MAGIC_BUTTERFLY(
        26,
        707072,
        Items.PICTURE_11268,
        Item(Items.MAGIC_BUTTERFLY_NET_11259),
        Item(Items.GOURM_IMPLING_JAR_11242, 3),
        Item(Items.EARTH_IMPLING_JAR_11244, 2),
        Item(Items.ESS_IMPLING_JAR_11246),
    ),

    JAR_GENERATOR(
        29,
        969216,
        Items.PICTURE_11267,
        Item(Items.JAR_GENERATOR_11258),
        Item(Items.ESS_IMPLING_JAR_11246, 3),
        Item(Items.ECLECTIC_IMPLING_JAR_11248, 2),
        Item(Items.NATURE_IMPLING_JAR_11250),
    ),

    IMPLING_JAR(32, 1231360, Items.PICTURE_11269, Item(Items.IMPLING_JAR_11260, 3)) {
        override fun hasItems(player: Player): Boolean = BNetTypes.getImplings().any { player.inventory.containsItem(it.reward) }
    }, ;

    open fun hasItems(player: Player): Boolean = player.inventory.containsItems(*required)

    companion object {
        @JvmStatic
        fun getItem(player: Player): Item? = BNetTypes.getImplings().firstOrNull { player.inventory.containsItem(it.reward) }?.reward

        @JvmStatic
        fun forButton(button: Int): ElnockExchange? = values().firstOrNull { it.button == button }
    }
}
