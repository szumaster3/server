package content.global.handlers.item.withitem

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

class PoisonWeaponListener : InteractionListener {
    private val poisonsId =
        intArrayOf(Items.WEAPON_POISON_187, Items.WEAPON_POISON_PLUS_5937, Items.WEAPON_POISON_PLUS_PLUS_5940)
    private val regularWeapon = PoisonSets.itemMap.keys.toIntArray()
    private val poisonedWeapon = PoisonSets.itemMap.values.toIntArray()
    private val regWeapon =
        KarambwanPoisonSets.regularWeaponsMap.values
            .map { it.base }
            .toIntArray()
    private val kpWeapon =
        KarambwanPoisonSets.poisonWeaponMap.values
            .map { it.kp }
            .toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, poisonsId, *regularWeapon) { player, used, with ->
            val index = poisonsId.indexOf(used.id)
            val product = PoisonSets.itemMap[with.id]!![index]
            val amt = min(5, with.asItem().amount)

            if (removeItem(player, Item(with.id, amt))) {
                replaceSlot(player, used.asItem().index, Item(Items.VIAL_229, 1))
                addItemOrDrop(player, product, amt)
                sendMessage(player, "You poison the ${with.name.lowercase()}.")
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.KARAMBWAN_PASTE_3153, *regWeapon) { player, used, with ->
            val product = KarambwanPoisonSets.regularWeaponsMap[with.id] ?: return@onUseWith true
            if (removeItem(player, used.asItem())) {
                replaceSlot(player, with.asItem().slot, Item(product.kp, 1))
                sendMessage(
                    player,
                    "You smear the poisonous Karambwan paste over the " +
                        "${if (!product.name.contains("spear", true)) "hasta" else "spear"}.",
                )
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.CLEANING_CLOTH_3188, *kpWeapon) { player, _, with ->
            val product = KarambwanPoisonSets.poisonWeaponMap[with.id] ?: return@onUseWith true
            replaceSlot(player, with.asItem().slot, Item(product.base, 1))
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.CLEANING_CLOTH_3188, *poisonedWeapon) { player, _, with ->
            val base = PoisonSets.getBase(with.id) ?: return@onUseWith false
            val amt = min(5, with.asItem().amount)
            removeItem(player, Item(with.id, amt))
            addItemOrDrop(player, base, amt)
            return@onUseWith true
        }
    }

    internal enum class KarambwanPoisonSets(
        val base: Int,
        val kp: Int,
    ) {
        BRONZE_SPEAR(base = Items.BRONZE_SPEAR_1237, kp = Items.BRONZE_SPEARKP_3170),
        IRON_SPEAR(base = Items.IRON_SPEAR_1239, kp = Items.IRON_SPEARKP_3171),
        STEEL_SPEAR(base = Items.STEEL_SPEAR_1241, kp = Items.STEEL_SPEARKP_3172),
        BLACK_SPEAR(base = Items.BLACK_SPEAR_4580, kp = Items.BLACK_SPEARKP_4584),
        MITHRIL_SPEAR(base = Items.MITHRIL_SPEAR_1243, kp = Items.MITHRIL_SPEARKP_3173),
        ADAMANT_SPEAR(base = Items.ADAMANT_SPEAR_1245, kp = Items.ADAMANT_SPEARKP_3174),
        RUNE_SPEAR(base = Items.RUNE_SPEAR_1247, kp = Items.RUNE_SPEARKP_3175),
        DRAGON_SPEAR(base = Items.DRAGON_SPEAR_1249, kp = Items.DRAGON_SPEARKP_3176),
        BRONZE_HASTA(base = Items.BRONZE_HASTA_11367, kp = Items.BRONZE_HASTAKP_11381),
        IRON_HASTA(base = Items.IRON_HASTA_11369, kp = Items.IRON_HASTAKP_11388),
        STEEL_HASTA(base = Items.STEEL_HASTA_11371, kp = Items.STEEL_HASTAKP_11395),
        MITHRIL_HASTA(base = Items.MITHRIL_HASTA_11373, kp = Items.MITHRIL_HASTAKP_11402),
        ADAMANT_HASTA(base = Items.ADAMANT_HASTA_11375, kp = Items.ADAMANT_HASTAKP_11409),
        RUNE_HASTA(base = Items.RUNE_HASTA_11377, kp = Items.RUNE_HASTAKP_11416),
        ;

        companion object {
            val regularWeaponsMap = HashMap<Int, KarambwanPoisonSets>()
            val poisonWeaponMap = HashMap<Int, KarambwanPoisonSets>()

            init {
                for (product in values()) {
                    regularWeaponsMap[product.base] = product
                    poisonWeaponMap[product.kp] = product
                }
            }
        }
    }

    internal enum class PoisonSets(
        val base: Int,
        val p: Int,
        val pp: Int,
        val ppp: Int,
    ) {
        BRONZE_ARROW(
            base = Items.BRONZE_ARROW_882,
            p = Items.BRONZE_ARROWP_883,
            pp = Items.BRONZE_ARROWP_PLUS_5616,
            ppp = Items.BRONZE_ARROWP_PLUS_PLUS_5622,
        ),
        IRON_ARROW(
            base = Items.IRON_ARROW_884,
            p = Items.IRON_ARROWP_885,
            pp = Items.IRON_ARROWP_PLUS_5617,
            ppp = Items.IRON_ARROWP_PLUS_PLUS_5623,
        ),
        STEEL_ARROW(
            base = Items.STEEL_ARROW_886,
            p = Items.STEEL_ARROWP_887,
            pp = Items.STEEL_ARROWP_PLUS_5618,
            ppp = Items.STEEL_ARROWP_PLUS_PLUS_5624,
        ),
        MITHRIL_ARROW(
            base = Items.MITHRIL_ARROW_888,
            p = Items.MITHRIL_ARROWP_889,
            pp = Items.MITHRIL_ARROWP_PLUS_5619,
            ppp = Items.MITHRIL_ARROWP_PLUS_PLUS_5625,
        ),
        ADAMANT_ARROW(
            base = Items.ADAMANT_ARROW_890,
            p = Items.ADAMANT_ARROWP_891,
            pp = Items.ADAMANT_ARROWP_PLUS_5620,
            ppp = Items.ADAMANT_ARROWP_PLUS_PLUS_5626,
        ),
        RUNE_ARROW(
            base = Items.RUNE_ARROW_892,
            p = Items.RUNE_ARROWP_893,
            pp = Items.RUNE_ARROWP_PLUS_5621,
            ppp = Items.RUNE_ARROWP_PLUS_PLUS_5627,
        ),
        BRONZE_KNIFE(
            base = Items.BRONZE_KNIFE_864,
            p = Items.BRONZE_KNIFEP_870,
            pp = Items.BRONZE_KNIFEP_PLUS_5654,
            ppp = Items.BRONZE_KNIFEP_PLUS_PLUS_5661,
        ),
        IRON_KNIFE(
            base = Items.IRON_KNIFE_863,
            p = Items.IRON_KNIFEP_871,
            pp = Items.IRON_KNIFEP_PLUS_5655,
            ppp = Items.IRON_KNIFEP_PLUS_PLUS_5662,
        ),
        STEEL_KNIFE(
            base = Items.STEEL_KNIFE_865,
            p = Items.STEEL_KNIFEP_872,
            pp = Items.STEEL_KNIFEP_PLUS_5656,
            ppp = Items.STEEL_KNIFEP_PLUS_PLUS_5663,
        ),
        BLACK_KNIFE(
            base = Items.BLACK_KNIFE_869,
            p = Items.BLACK_KNIFEP_874,
            pp = Items.BLACK_KNIFEP_PLUS_5658,
            ppp = Items.BLACK_KNIFEP_PLUS_PLUS_5665,
        ),
        MITHRIL_KNIFE(
            base = Items.MITHRIL_KNIFE_866,
            p = Items.MITHRIL_KNIFEP_873,
            pp = Items.MITHRIL_KNIFEP_PLUS_5657,
            ppp = Items.MITHRIL_KNIFEP_PLUS_PLUS_5664,
        ),
        ADAMANT_KNIFE(
            base = Items.ADAMANT_KNIFE_867,
            p = Items.ADAMANT_KNIFEP_875,
            pp = Items.ADAMANT_KNIFEP_PLUS_5659,
            ppp = Items.ADAMANT_KNIFEP_PLUS_PLUS_5666,
        ),
        RUNE_KNIFE(
            base = Items.RUNE_KNIFE_868,
            p = Items.RUNE_KNIFEP_876,
            pp = Items.RUNE_KNIFEP_PLUS_5660,
            ppp = Items.RUNE_KNIFEP_PLUS_PLUS_5667,
        ),
        BRONZE_DART(
            base = Items.BRONZE_DART_806,
            p = Items.BRONZE_DARTP_812,
            pp = Items.BRONZE_DARTP_PLUS_5628,
            ppp = Items.BRONZE_DARTP_PLUS_PLUS_5635,
        ),
        IRON_DART(
            base = Items.IRON_DART_807,
            p = Items.IRON_DARTP_813,
            pp = Items.IRON_DARTP_PLUS_5629,
            ppp = Items.IRON_DARTP_PLUS_PLUS_5636,
        ),
        STEEL_DART(
            base = Items.STEEL_DART_808,
            p = Items.STEEL_DARTP_814,
            pp = Items.STEEL_DARTP_PLUS_5630,
            ppp = Items.STEEL_DARTP_PLUS_PLUS_5637,
        ),
        BLACK_DART(
            base = Items.BLACK_DART_3093,
            p = Items.BLACK_DARTP_3094,
            pp = Items.BLACK_DARTP_PLUS_5631,
            ppp = Items.BLACK_DARTP_PLUS_PLUS_5638,
        ),
        MITHRIL_DART(
            base = Items.MITHRIL_DART_809,
            p = Items.MITHRIL_DARTP_815,
            pp = Items.MITHRIL_DARTP_PLUS_5632,
            ppp = Items.MITHRIL_DARTP_PLUS_PLUS_5639,
        ),
        ADAMANT_DART(
            base = Items.ADAMANT_DART_810,
            p = Items.ADAMANT_DARTP_816,
            pp = Items.ADAMANT_DARTP_PLUS_5633,
            ppp = Items.ADAMANT_DARTP_PLUS_PLUS_5640,
        ),
        RUNE_DART(
            base = Items.RUNE_DART_811,
            p = Items.RUNE_DARTP_817,
            pp = Items.RUNE_DARTP_PLUS_5634,
            ppp = Items.RUNE_DARTP_PLUS_PLUS_5641,
        ),
        BLURITE_BOLT(
            base = Items.BLURITE_BOLTS_9139,
            p = Items.BLURITE_BOLTSP_9286,
            pp = Items.BLURITE_BOLTSP_PLUS_9293,
            ppp = Items.BLURITE_BOLTSP_PLUS_PLUS_9300,
        ),
        BRONZE_BOLT(
            base = Items.BRONZE_BOLTS_877,
            p = Items.BRONZE_BOLTSP_878,
            pp = Items.BRONZE_BOLTSP_PLUS_6061,
            ppp = Items.BRONZE_BOLTSP_PLUS_PLUS_6062,
        ),
        IRON_BOLT(
            base = Items.IRON_BOLTS_9140,
            p = Items.IRON_BOLTS_P_9287,
            pp = Items.IRON_BOLTSP_PLUS_9294,
            ppp = Items.IRON_BOLTSP_PLUS_PLUS_9301,
        ),
        STEEL_BOLT(
            base = Items.STEEL_BOLTS_9141,
            p = Items.STEEL_BOLTS_P_9288,
            pp = Items.STEEL_BOLTSP_PLUS_9295,
            ppp = Items.STEEL_BOLTSP_PLUS_PLUS_9302,
        ),
        BLACK_BOLT(
            base = Items.BLACK_BOLTS_13083,
            p = Items.BLACK_BOLTSP_13084,
            pp = Items.BLACK_BOLTSP_PLUS_13085,
            ppp = Items.BLACK_BOLTSP_PLUS_PLUS_13086,
        ),
        MITHRIL_BOLT(
            base = Items.MITHRIL_BOLTS_9142,
            p = Items.MITHRIL_BOLTS_P_9289,
            pp = Items.MITHRIL_BOLTSP_PLUS_9296,
            ppp = Items.MITHRIL_BOLTSP_PLUS_PLUS_9303,
        ),
        ADAMANT_BOLT(
            base = Items.ADAMANT_BOLTS_9143,
            p = Items.ADAMANT_BOLTS_P_9290,
            pp = Items.ADAMANT_BOLTSP_PLUS_9297,
            ppp = Items.ADAMANT_BOLTSP_PLUS_PLUS_9304,
        ),
        RUNE_BOLT(
            base = Items.RUNE_BOLTS_9144,
            p = Items.RUNITE_BOLTS_P_9291,
            pp = Items.RUNITE_BOLTSP_PLUS_9298,
            ppp = Items.RUNITE_BOLTSP_PLUS_PLUS_9305,
        ),
        SILVER_BOLT(
            base = Items.SILVER_BOLTS_9145,
            p = Items.SILVER_BOLTS_P_9292,
            pp = Items.SILVER_BOLTSP_PLUS_9299,
            ppp = Items.SILVER_BOLTSP_PLUS_PLUS_9306,
        ),
        IRON_JAVELIN(
            base = Items.IRON_JAVELIN_826,
            p = Items.BRONZE_JAVELINP_831,
            pp = Items.IRON_JAVELINP_PLUS_5643,
            ppp = Items.BRONZE_JAVNP_PLUS_PLUS_5648,
        ),
        BRONZE_JAVELIN(
            base = Items.IRON_JAVELIN_826,
            p = Items.IRON_JAVELINP_832,
            pp = Items.BRONZE_JAVELINP_PLUS_5642,
            ppp = Items.IRON_JAVELINP_PLUS_PLUS_5649,
        ),
        STEEL_JAVELIN(
            base = Items.STEEL_JAVELIN_827,
            p = Items.STEEL_JAVELINP_833,
            pp = Items.STEEL_JAVELINP_PLUS_5644,
            ppp = Items.STEEL_JAVELINP_PLUS_PLUS_5650,
        ),
        MITHRIL_JAVELIN(
            base = Items.MITHRIL_JAVELIN_828,
            p = Items.MITHRIL_JAVELINP_834,
            pp = Items.MITHRIL_JAVELINP_PLUS_5645,
            ppp = Items.MITHRIL_JAVELINP_PLUS_PLUS_5651,
        ),
        ADAMANT_JAVELIN(
            base = Items.ADAMANT_JAVELIN_829,
            p = Items.ADAMANT_JAVELINP_835,
            pp = Items.ADAMANT_JAVELINP_PLUS_5646,
            ppp = Items.ADAMANT_JAVELINP_PLUS_PLUS_5652,
        ),
        RUNE_JAVELIN(
            base = Items.RUNE_JAVELIN_830,
            p = Items.RUNE_JAVELINP_836,
            pp = Items.RUNE_JAVELINP_PLUS_5647,
            ppp = Items.RUNE_JAVELINP_PLUS_PLUS_5653,
        ),
        MORRIGAN_JAVELIN(
            base = Items.MORRIGANS_JAVELIN_13879,
            p = Items.MORRIGANS_JAVELINP_13880,
            pp = Items.MORRIGANS_JAVELINP_PLUS_13881,
            ppp = Items.MORRIGANS_JAVELINP_PLUS_PLUS_13882,
        ),
        IRON_DAGGER(
            base = Items.IRON_DAGGER_1203,
            p = Items.IRON_DAGGERP_1219,
            pp = Items.IRON_DAGGERP_PLUS_5668,
            ppp = Items.IRON_DAGGERP_PLUS_PLUS_5686,
        ),
        BRONZE_DAGGER(
            base = Items.BRONZE_DAGGER_1205,
            p = Items.BRONZE_DAGGERP_1221,
            pp = Items.BRONZE_DAGGERP_PLUS_5670,
            ppp = Items.BRZE_DAGGERP_PLUS_PLUS_5688,
        ),
        STEEL_DAGGER(
            base = Items.STEEL_DAGGER_1207,
            p = Items.STEEL_DAGGERP_1223,
            pp = Items.STEEL_DAGGERP_PLUS_5672,
            ppp = Items.STEEL_DAGGERP_PLUS_PLUS_5690,
        ),
        BLACK_DAGGER(
            base = Items.BLACK_DAGGER_1217,
            p = Items.BLACK_DAGGERP_1233,
            pp = Items.BLACK_DAGGERP_PLUS_5682,
            ppp = Items.BLACK_DAGGERP_PLUS_PLUS_5700,
        ),
        BONE_DAGGER(
            base = Items.BONE_DAGGER_8872,
            p = Items.BONE_DAGGER_P_8874,
            pp = Items.BONE_DAGGER_P_PLUS_8876,
            ppp = Items.BONE_DAGGER_P_PLUS_PLUS_8878,
        ),
        MITHRIL_DAGGER(
            base = Items.MITHRIL_DAGGER_1209,
            p = Items.MITHRIL_DAGGERP_1225,
            pp = Items.MITHRIL_DAGGERP_PLUS_5674,
            ppp = Items.MITHRIL_DAGGERP_PLUS_PLUS_5692,
        ),
        ADAMANT_DAGGER(
            base = Items.ADAMANT_DAGGER_1211,
            p = Items.ADAMANT_DAGGERP_1227,
            pp = Items.ADAMANT_DAGGERP_PLUS_5676,
            ppp = Items.ADAMANT_DAGGERP_PLUS_PLUS_5694,
        ),
        RUNE_DAGGER(
            base = Items.RUNE_DAGGER_1213,
            p = Items.RUNE_DAGGERP_1229,
            pp = Items.RUNE_DAGGERP_PLUS_5678,
            ppp = Items.RUNE_DAGGERP_PLUS_PLUS_5696,
        ),
        DRAGON_DAGGER(
            base = Items.DRAGON_DAGGER_1215,
            p = Items.DRAGON_DAGGERP_1231,
            pp = Items.DRAGON_DAGGERP_PLUS_5680,
            ppp = Items.DRAGON_DAGGERP_PLUS_PLUS_5698,
        ),
        BRONZE_SPEAR(
            base = Items.BRONZE_SPEAR_1237,
            p = Items.BRONZE_SPEARP_1251,
            pp = Items.BRONZE_SPEARP_PLUS_5704,
            ppp = Items.BRONZE_SPEARP_PLUS_PLUS_5718,
        ),
        IRON_SPEAR(
            base = Items.IRON_SPEAR_1239,
            p = Items.IRON_SPEARP_1253,
            pp = Items.IRON_SPEARP_PLUS_5706,
            ppp = Items.IRON_SPEARP_PLUS_PLUS_5720,
        ),
        STEEL_SPEAR(
            base = Items.STEEL_SPEAR_1241,
            p = Items.STEEL_SPEARP_1255,
            pp = Items.STEEL_SPEARP_PLUS_5708,
            ppp = Items.STEEL_SPEARP_PLUS_PLUS_5722,
        ),
        BLACK_SPEAR(
            base = Items.BLACK_SPEAR_4580,
            p = Items.BLACK_SPEARP_4582,
            pp = Items.BLACK_SPEARP_PLUS_5734,
            ppp = Items.BLACK_SPEARP_PLUS_PLUS_5736,
        ),
        MITHRIL_SPEAR(
            base = Items.MITHRIL_SPEAR_1243,
            p = Items.MITHRIL_SPEARP_1257,
            pp = Items.MITHRIL_SPEARP_PLUS_5710,
            ppp = Items.MITHRIL_SPEARP_PLUS_PLUS_5724,
        ),
        ADAMANT_SPEAR(
            base = Items.ADAMANT_SPEAR_1245,
            p = Items.ADAMANT_SPEARP_1259,
            pp = Items.ADAMANT_SPEARP_PLUS_5712,
            ppp = Items.ADAMANT_SPEARP_PLUS_PLUS_5726,
        ),
        RUNE_SPEAR(
            base = Items.RUNE_SPEAR_1247,
            p = Items.RUNE_SPEARP_1261,
            pp = Items.RUNE_SPEARP_PLUS_5714,
            ppp = Items.RUNE_SPEARP_PLUS_PLUS_5728,
        ),
        DRAGON_SPEAR(
            base = Items.DRAGON_SPEAR_1249,
            p = Items.DRAGON_SPEARP_1263,
            pp = Items.DRAGON_SPEARP_PLUS_5716,
            ppp = Items.DRAGON_SPEARP_PLUS_PLUS_5730,
        ),
        BRONZE_HASTA(
            base = Items.BRONZE_HASTA_11367,
            p = Items.BRONZE_HASTAP_11379,
            pp = Items.BRONZE_HASTAP_PLUS_11382,
            ppp = Items.BRONZE_HASTAP_PLUS_PLUS_11384,
        ),
        IRON_HASTA(
            base = Items.IRON_HASTA_11369,
            p = Items.IRON_HASTAP_11386,
            pp = Items.IRON_HASTAP_PLUS_11389,
            ppp = Items.IRON_HASTAP_PLUS_PLUS_11391,
        ),
        STEEL_HASTA(
            base = Items.STEEL_HASTA_11371,
            p = Items.STEEL_HASTAP_11393,
            pp = Items.STEEL_HASTAP_PLUS_11396,
            ppp = Items.STEEL_HASTAP_PLUS_PLUS_11398,
        ),
        MITHRIL_HASTA(
            base = Items.MITHRIL_HASTA_11373,
            p = Items.MITHRIL_HASTAP_11400,
            pp = Items.MITHRIL_HASTAP_PLUS_11403,
            ppp = Items.MITHRIL_HASTAP_PLUS_PLUS_11405,
        ),
        ADAMANT_HASTA(
            base = Items.ADAMANT_HASTA_11375,
            p = Items.ADAMANT_HASTAP_11407,
            pp = Items.ADAMANT_HASTAP_PLUS_11410,
            ppp = Items.ADAMANT_HASTAP_PLUS_PLUS_11412,
        ),
        RUNE_HASTA(
            base = Items.RUNE_HASTA_11377,
            p = Items.RUNE_HASTAP_11414,
            pp = Items.RUNE_HASTAP_PLUS_11417,
            ppp = Items.RUNE_HASTAP_PLUS_PLUS_11419,
        ),
        ;

        companion object {
            val itemMap = values().associate { it.base to intArrayOf(it.p, it.pp, it.ppp) }

            fun getBase(poisoned: Int): Int? {
                for ((base, set) in itemMap.entries) {
                    if (set.contains(poisoned)) return base
                }
                return null
            }
        }
    }
}
