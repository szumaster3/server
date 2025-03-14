package content.global.handlers.item

import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class WaterSkinPlugin :
    UseWithHandler(
        Items.WATERSKIN3_1825,
        Items.WATERSKIN2_1827,
        Items.WATERSKIN1_1829,
        Items.WATERSKIN0_1831,
    ) {
    private val waterSkinId = Item(Items.WATERSKIN4_1823)
    private val data =
        arrayOf(
            intArrayOf(Items.JUG_OF_WATER_1937, Items.JUG_1935),
            intArrayOf(Items.BUCKET_OF_WATER_1929, Items.BUCKET_1925),
            intArrayOf(Items.BOWL_OF_WATER_1921, Items.BOWL_1923),
            intArrayOf(Items.VIAL_OF_WATER_227, Items.VIAL_229),
        )

    override fun newInstance(arg: Any?): Plugin<Any> {
        for (i in data.indices) {
            addHandler(data[i][0], ITEM_TYPE, this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean {
        val waterSkin = if (event.usedItem.name.contains("skin")) event.usedItem else event.baseItem
        val vessil = if (!event.usedItem.name.contains("skin")) event.usedItem else event.baseItem
        if (event.player.inventory.remove(waterSkin)) {
            event.player.inventory.add(
                if (vessil.id ==
                    Items.VIAL_OF_WATER_227
                ) {
                    Item(waterSkin.id - 2)
                } else {
                    waterSkinId
                },
            )
            for (i in data.indices) {
                if (data[i][0] == vessil.id && event.player.inventory.remove(vessil)) {
                    event.player.inventory.add(Item(data[i][1]))
                }
            }
        }
        return true
    }
}
