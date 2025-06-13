package content.global.plugin.item.equipment

import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

/**
 * The PVP equipment.
 */
@Initializable
class PVPEquipmentRegister : Plugin<Any> {

    private val ticksMap: MutableMap<Int, Array<Int>> = mutableMapOf()

    init {
        ticksMap[6000] = arrayOf(
            Items.VESTAS_CHAINBODY_13887,
            Items.VESTAS_CHAINBODY_DEG_13889,
            Items.VESTAS_PLATESKIRT_13893,
            Items.VESTAS_PLATESKIRT_DEG_13895,
            Items.VESTAS_LONGSWORD_13899,
            Items.VESTAS_LONGSWORD_DEG_13901,
            Items.VESTAS_SPEAR_13905,
            Items.VESTAS_SPEAR_DEG_13907,
            Items.STATIUS_PLATEBODY_13884,
            Items.STATIUS_PLATEBODY_DEG_13886,
            Items.STATIUS_PLATELEGS_13890,
            Items.STATIUS_PLATELEGS_DEG_13892,
            Items.STATIUS_FULL_HELM_13896,
            Items.STATIUS_FULL_HELM_DEG_13898,
            Items.STATIUS_WARHAMMER_13902,
            Items.STATIUS_WARHAMMER_DEG_13904,
            Items.ZURIELS_ROBE_TOP_13858,
            Items.ZURIELS_ROBE_TOP_DEG_13860,
            Items.ZURIELS_ROBE_BOTTOM_13861,
            Items.ZURIELS_ROBE_BOTTOM_DEG_13863,
            Items.ZURIELS_HOOD_13864,
            Items.ZURIELS_HOOD_DEG_13866,
            Items.ZURIELS_STAFF_13867,
            Items.ZURIELS_STAFF_DEG_13869,
            Items.MORRIGANS_LEATHER_BODY_13870,
            Items.MORRIGANS_LEATHER_BODY_DEG_13872,
            Items.MORRIGANS_LEATHER_CHAPS_13873,
            Items.MORRIGANS_LEATHER_CHAPS_DEG_13875,
            Items.MORRIGANS_COIF_13876,
            Items.MORRIGANS_COIF_DEG_13878
        )

        ticksMap[1500] = arrayOf(
            Items.CORRUPT_VESTAS_CHAINBODY_13911,
            Items.CORRUPT_VESTAS_CHAINBODY_DEG_13913,
            Items.CORRUPT_VESTAS_PLATESKIRT_13917,
            Items.CORRUPT_VESTAS_PLATESKIRT_DEG_13919,
            Items.CORRUPT_VESTAS_LONGSWORD_13924,
            Items.CORRUPT_VESTAS_LONGSWORD_DEG_13925,
            Items.CORRUPT_VESTAS_SPEAR_13929,
            Items.CORRUPT_VESTAS_SPEAR_DEG_13931,
            Items.CORRUPT_STATIUS_PLATEBODY_13908,
            Items.CORRUPT_STATIUS_PLATEBODY_DEG_13910,
            Items.CORRUPT_STATIUS_PLATELEGS_13914,
            Items.CORRUPT_STATIUS_PLATELEGS_DEG_13916,
            Items.CORRUPT_STATIUS_FULL_HELM_13920,
            Items.CORRUPT_STATIUS_FULL_HELM_DEG_13922,
            Items.CORRUPT_STATIUS_WARHAMMER_13926,
            Items.CORRUPT_STATIUS_WARHAMMER_DEG_13928,
            Items.CORRUPT_ZURIELS_ROBE_TOP_13932,
            Items.CORRUPT_ZURIELS_ROBE_TOP_DEG_13934,
            Items.CORRUPT_ZURIELS_ROBE_BOTTOM_13935,
            Items.CORRUPT_ZURIELS_ROBE_BOTTOM_DEG_13937,
            Items.CORRUPT_ZURIELS_HOOD_13938,
            Items.CORRUPT_ZURIELS_HOOD_DEG_13940,
            Items.CORRUPT_ZURIELS_STAFF_13941,
            Items.CORRUPT_ZURIELS_STAFF_DEG_13943,
            Items.CORRUPT_MORRIGANS_LEATHER_BODY_13944,
            Items.CORRUPT_MORRIGANS_LEATHER_BODY_DEG_13946,
            Items.CORRUPT_MORRIGANS_LEATHER_CHAPS_13947,
            Items.CORRUPT_MORRIGANS_LEATHER_CHAPS_DEG_13949,
            Items.CORRUPT_MORRIGANS_COIF_13950,
            Items.CORRUPT_MORRIGANS_COIF_DEG_13952
        )

        ticksMap[3000] = arrayOf(
            Items.CORRUPT_DRAGON_CHAINBODY_13958,
            Items.CORRUPT_DRAGON_CHAINBODY_DEG_13960,
            Items.CORRUPT_DRAGON_PLATELEGS_13970,
            Items.CORRUPT_DRAGON_PLATELEGS_DEG_13972,
            Items.CORRUPT_DRAGON_MED_HELM_13961,
            Items.CORRUPT_DRAGON_MED_HELM_DEG_13963,
            Items.CORRUPT_DRAGON_SQ_SHIELD_13964,
            Items.CORRUPT_DRAGON_SQ_SHIELD_DEG_13966,
            Items.CORRUPT_DRAGON_PLATESKIRT_13967,
            Items.CORRUPT_DRAGON_PLATESKIRT_DEG_13969,
            Items.CORRUPT_DRAGON_BATTLEAXE_13973,
            Items.CORRUPT_DRAGON_BATTLEAXE_DEG_13975,
            Items.CORRUPT_DRAGON_DAGGER_13976,
            Items.CORRUPT_DRAGON_DAGGER_DEG_13978,
            Items.CORRUPT_DRAGON_SCIMITAR_13979,
            Items.CORRUPT_DRAGON_SCIMITAR_DEG_13981,
            Items.CORRUPT_DRAGON_LONGSWORD_13982,
            Items.CORRUPT_DRAGON_LONGSWORD_DEG_13984,
            Items.CORRUPT_DRAGON_MACE_13985,
            Items.CORRUPT_DRAGON_MACE_DEG_13987,
            Items.CORRUPT_DRAGON_SPEAR_13988,
            Items.CORRUPT_DRAGON_SPEAR_DEG_13990
        )
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ticksMap.forEach { (ticks, items) ->
            EquipmentDegrade.registerSet(ticks, items)
        }
        return this
    }

    override fun fireEvent(identifier: String, vararg args: Any?): Any? {
        return null
    }
}