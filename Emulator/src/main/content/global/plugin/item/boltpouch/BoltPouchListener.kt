package content.global.plugin.item.boltpouch

import core.api.openInterface
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items

class BoltPouchListener : InteractionListener {

    private val ALLOWED_BOLT_IDS = intArrayOf(
        Items.ADAMANT_BOLTSP_PLUS_9297,
        Items.ADAMANT_BOLTSP_PLUS_PLUS_9304,
        Items.ADAMANT_BOLTS_9143,
        Items.ADAMANT_BOLTS_P_9290,
        Items.BLACK_BOLTSP_13084,
        Items.BLACK_BOLTSP_PLUS_13085,
        Items.BLACK_BOLTSP_PLUS_PLUS_13086,
        Items.BLACK_BOLTS_13083,
        Items.BLURITE_BOLTSP_9286,
        Items.BLURITE_BOLTSP_PLUS_9293,
        Items.BLURITE_BOLTSP_PLUS_PLUS_9300,
        Items.BLURITE_BOLTS_9139,
        Items.BROAD_TIPPED_BOLTS_13280,
        Items.BRONZE_BOLTSP_878,
        Items.BRONZE_BOLTSP_PLUS_6061,
        Items.BRONZE_BOLTSP_PLUS_PLUS_6062,
        Items.BRONZE_BOLTS_877,
        Items.DIAMOND_BOLTS_9340,
        Items.DIAMOND_BOLTS_E_9243,
        Items.DRAGON_BOLTS_9341,
        Items.DRAGON_BOLTS_E_9244,
        Items.EMERALD_BOLTS_9338,
        Items.EMERALD_BOLTS_E_9241,
        Items.IRON_BOLTSP_PLUS_9294,
        Items.IRON_BOLTSP_PLUS_PLUS_9301,
        Items.IRON_BOLTS_9140,
        Items.IRON_BOLTS_P_9287,
        Items.JADE_BOLTS_9335,
        Items.JADE_BOLTS_E_9237,
        Items.MITHRIL_BOLTSP_PLUS_9296,
        Items.MITHRIL_BOLTSP_PLUS_PLUS_9303,
        Items.MITHRIL_BOLTS_9142,
        Items.MITHRIL_BOLTS_P_9289,
        Items.ONYX_BOLTS_9342,
        Items.ONYX_BOLTS_E_9245,
        Items.OPAL_BOLTS_879,
        Items.OPAL_BOLTS_E_9236,
        Items.PEARL_BOLTS_880,
        Items.PEARL_BOLTS_E_9238,
        Items.RUBY_BOLTS_9339,
        Items.RUBY_BOLTS_E_9242,
        Items.RUNE_BOLTS_9144,
        Items.RUNITE_BOLTSP_PLUS_9298,
        Items.RUNITE_BOLTSP_PLUS_PLUS_9305,
        Items.RUNITE_BOLTS_P_9291,
        Items.SAPPHIRE_BOLTS_9337,
        Items.SAPPHIRE_BOLTS_E_9240,
        Items.SILVER_BOLTSP_PLUS_9299,
        Items.SILVER_BOLTSP_PLUS_PLUS_9306,
        Items.SILVER_BOLTS_9145,
        Items.SILVER_BOLTS_P_9292,
        Items.STEEL_BOLTSP_PLUS_9295,
        Items.STEEL_BOLTSP_PLUS_PLUS_9302,
        Items.STEEL_BOLTS_9141,
        Items.STEEL_BOLTS_P_9288,
        Items.TOPAZ_BOLTS_9336,
        Items.TOPAZ_BOLTS_E_9239
    )

    override fun defineListeners() {
        on(Items.BOLT_POUCH_9433, IntType.ITEM, "open") { player, _ ->
            openInterface(player, Components.XBOWS_POUCH_433)
            return@on true
        }

        onUseWith(IntType.ITEM, ALLOWED_BOLT_IDS, Items.BOLT_POUCH_9433) { player, item, otherItem ->
            val pouchItem = if (item.id == Items.BOLT_POUCH_9433) item else otherItem
            val boltItem = if (pouchItem == item) otherItem else item

            val addedAmount = player.boltPouchManager.addBolts(pouchItem.id, boltItem.id, boltItem.asItem().amount)
            if (addedAmount <= 0) {
                sendMessage(player, "You can't add that type of bolt or your pouch is full.")
            } else {
                sendMessage(player, "Added $addedAmount bolts to the pouch.")
                player.inventory.remove(boltItem.asItem())
            }
            return@onUseWith true
        }
    }
}
