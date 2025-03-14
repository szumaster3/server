package content.global.handlers.item.equipment

import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class BarrowsEquipmentRegister : Plugin<Any> {
    companion object {
        @JvmField val TICKS = (15 * 6000) / 4
    }

    val DHAROK_HELM =
        arrayOf(
            Items.DHAROKS_HELM_4716,
            Items.DHAROKS_HELM_100_4880,
            Items.DHAROKS_HELM_75_4881,
            Items.DHAROKS_HELM_50_4882,
            Items.DHAROKS_HELM_25_4883,
            Items.DHAROKS_HELM_0_4884,
        )

    val DHAROK_AXE =
        arrayOf(
            Items.DHAROKS_GREATAXE_4718,
            Items.DHAROKS_AXE_100_4886,
            Items.DHAROKS_AXE_75_4887,
            Items.DHAROKS_AXE_50_4888,
            Items.DHAROKS_AXE_25_4889,
            Items.DHAROKS_AXE_0_4890,
        )

    val DHAROK_BODY =
        arrayOf(
            Items.DHAROKS_PLATEBODY_4720,
            Items.DHAROKS_BODY_100_4892,
            Items.DHAROKS_BODY_75_4893,
            Items.DHAROKS_BODY_50_4894,
            Items.DHAROKS_BODY_25_4895,
            Items.DHAROKS_BODY_0_4896,
        )

    val DHAROK_LEGS =
        arrayOf(
            Items.DHAROKS_PLATELEGS_4722,
            Items.DHAROKS_LEGS_100_4898,
            Items.DHAROKS_LEGS_75_4899,
            Items.DHAROKS_LEGS_50_4900,
            Items.DHAROKS_LEGS_25_4901,
            Items.DHAROKS_LEGS_0_4902,
        )

    val GUTHAN_HELM =
        arrayOf(
            Items.GUTHANS_HELM_4724,
            Items.GUTHANS_HELM_100_4904,
            Items.GUTHANS_HELM_75_4905,
            Items.GUTHANS_HELM_50_4906,
            Items.GUTHANS_HELM_25_4907,
            Items.GUTHANS_HELM_0_4908,
        )

    val GUTHAN_SPEAR =
        arrayOf(
            Items.GUTHANS_WARSPEAR_4726,
            Items.GUTHANS_SPEAR_100_4910,
            Items.GUTHANS_SPEAR_75_4911,
            Items.GUTHANS_SPEAR_50_4912,
            Items.GUTHANS_SPEAR_25_4913,
            Items.GUTHANS_SPEAR_0_4914,
        )

    val GUTHAN_BODY =
        arrayOf(
            Items.GUTHANS_PLATEBODY_4728,
            Items.GUTHANS_BODY_100_4916,
            Items.GUTHANS_BODY_75_4917,
            Items.GUTHANS_BODY_50_4918,
            Items.GUTHANS_BODY_25_4919,
            Items.GUTHANS_BODY_0_4920,
        )

    val GUTHAN_SKIRT =
        arrayOf(
            Items.GUTHANS_CHAINSKIRT_4730,
            Items.GUTHANS_SKIRT_100_4922,
            Items.GUTHANS_SKIRT_75_4923,
            Items.GUTHANS_SKIRT_50_4924,
            Items.GUTHANS_SKIRT_25_4925,
            Items.GUTHANS_SKIRT_0_4926,
        )

    val TORAG_HELM =
        arrayOf(
            Items.TORAGS_HELM_4745,
            Items.TORAGS_HELM_100_4952,
            Items.TORAGS_HELM_75_4953,
            Items.TORAGS_HELM_50_4954,
            Items.TORAGS_HELM_25_4955,
            Items.TORAGS_HELM_0_4956,
        )

    val TORAG_HAMMER =
        arrayOf(
            Items.TORAGS_HAMMERS_4747,
            Items.TORAGS_HAMMER_100_4958,
            Items.TORAGS_HAMMER_75_4959,
            Items.TORAGS_HAMMER_50_4960,
            Items.TORAGS_HAMMER_25_4961,
            Items.TORAGS_HAMMER_0_4962,
        )

    val TORAG_BODY =
        arrayOf(
            Items.TORAGS_PLATEBODY_4749,
            Items.TORAGS_BODY_100_4964,
            Items.TORAGS_BODY_75_4965,
            Items.TORAGS_BODY_50_4966,
            Items.TORAGS_BODY_25_4967,
            Items.TORAGS_BODY_0_4968,
        )

    val TORAG_LEGS =
        arrayOf(
            Items.TORAGS_PLATELEGS_4751,
            Items.TORAGS_LEGS_100_4970,
            Items.TORAGS_LEGS_75_4971,
            Items.TORAGS_LEGS_50_4972,
            Items.TORAGS_LEGS_25_4973,
            Items.TORAGS_LEGS_0_4974,
        )

    val VERAC_HELM =
        arrayOf(
            Items.VERACS_HELM_4753,
            Items.VERACS_HELM_100_4976,
            Items.VERACS_HELM_75_4977,
            Items.VERACS_HELM_50_4978,
            Items.VERACS_HELM_25_4979,
            Items.VERACS_HELM_0_4980,
        )

    val VERAC_FLAIL =
        arrayOf(
            Items.VERACS_FLAIL_4755,
            Items.VERACS_FLAIL_100_4982,
            Items.VERACS_FLAIL_75_4983,
            Items.VERACS_FLAIL_50_4984,
            Items.VERACS_FLAIL_25_4985,
            Items.VERACS_FLAIL_0_4986,
        )

    val VERAC_BRASS =
        arrayOf(
            Items.VERACS_BRASSARD_4757,
            Items.VERACS_TOP_100_4988,
            Items.VERACS_TOP_75_4989,
            Items.VERACS_TOP_50_4990,
            Items.VERACS_TOP_25_4991,
            Items.VERACS_TOP_0_4992,
        )

    val VERAC_SKIRT =
        arrayOf(
            Items.VERACS_PLATESKIRT_4759,
            Items.VERACS_SKIRT_100_4994,
            Items.VERACS_SKIRT_75_4995,
            Items.VERACS_SKIRT_50_4996,
            Items.VERACS_SKIRT_25_4997,
            Items.VERACS_SKIRT_0_4998,
        )

    val AHRIM_HOOD =
        arrayOf(
            Items.AHRIMS_HOOD_4708,
            Items.AHRIMS_HOOD_100_4856,
            Items.AHRIMS_HOOD_75_4857,
            Items.AHRIMS_HOOD_50_4858,
            Items.AHRIMS_HOOD_25_4859,
            Items.AHRIMS_HOOD_0_4860,
        )

    val AHRIM_STAFF =
        arrayOf(
            Items.AHRIMS_STAFF_4710,
            Items.AHRIMS_STAFF_100_4862,
            Items.AHRIMS_STAFF_75_4863,
            Items.AHRIMS_STAFF_50_4864,
            Items.AHRIMS_STAFF_25_4865,
            Items.AHRIMS_STAFF_0_4866,
        )

    val AHRIM_TOP =
        arrayOf(
            Items.AHRIMS_ROBETOP_4712,
            Items.AHRIMS_TOP_100_4868,
            Items.AHRIMS_TOP_75_4869,
            Items.AHRIMS_TOP_50_4870,
            Items.AHRIMS_TOP_25_4871,
            Items.AHRIMS_TOP_0_4872,
        )

    val AHRIM_SKIRT =
        arrayOf(
            Items.AHRIMS_ROBESKIRT_4714,
            Items.AHRIMS_SKIRT_100_4874,
            Items.AHRIMS_SKIRT_75_4875,
            Items.AHRIMS_SKIRT_50_4876,
            Items.AHRIMS_SKIRT_25_4877,
            Items.AHRIMS_SKIRT_0_4878,
        )

    val KARIL_COIF =
        arrayOf(
            Items.KARILS_COIF_4732,
            Items.KARILS_COIF_100_4928,
            Items.KARILS_COIF_75_4929,
            Items.KARILS_COIF_50_4930,
            Items.KARILS_COIF_25_4931,
            Items.KARILS_COIF_0_4932,
        )

    val KARIL_CBOW =
        arrayOf(
            Items.KARILS_CROSSBOW_4734,
            Items.KARILS_X_BOW_100_4934,
            Items.KARILS_X_BOW_75_4935,
            Items.KARILS_X_BOW_50_4936,
            Items.KARILS_X_BOW_25_4937,
            Items.KARILS_X_BOW_0_4938,
        )

    val KARIL_TOP =
        arrayOf(
            Items.KARILS_LEATHERTOP_4736,
            Items.KARILS_TOP_100_4940,
            Items.KARILS_TOP_75_4941,
            Items.KARILS_TOP_50_4942,
            Items.KARILS_TOP_25_4943,
            Items.KARILS_TOP_0_4944,
        )

    val KARIL_SKIRT =
        arrayOf(
            Items.KARILS_LEATHERSKIRT_4738,
            Items.KARILS_SKIRT_100_4946,
            Items.KARILS_SKIRT_75_4947,
            Items.KARILS_SKIRT_50_4948,
            Items.KARILS_SKIRT_25_4949,
            Items.KARILS_SKIRT_0_4950,
        )

    override fun newInstance(arg: Any?): Plugin<Any> {
        EquipmentDegrade.registerSet(TICKS, AHRIM_HOOD)
        EquipmentDegrade.registerSet(TICKS, AHRIM_STAFF)
        EquipmentDegrade.registerSet(TICKS, AHRIM_TOP)
        EquipmentDegrade.registerSet(TICKS, AHRIM_SKIRT)
        EquipmentDegrade.registerSet(TICKS, KARIL_COIF)
        EquipmentDegrade.registerSet(TICKS, KARIL_CBOW)
        EquipmentDegrade.registerSet(TICKS, KARIL_TOP)
        EquipmentDegrade.registerSet(TICKS, KARIL_SKIRT)
        EquipmentDegrade.registerSet(TICKS, DHAROK_HELM)
        EquipmentDegrade.registerSet(TICKS, DHAROK_AXE)
        EquipmentDegrade.registerSet(TICKS, DHAROK_BODY)
        EquipmentDegrade.registerSet(TICKS, DHAROK_LEGS)
        EquipmentDegrade.registerSet(TICKS, GUTHAN_HELM)
        EquipmentDegrade.registerSet(TICKS, GUTHAN_SPEAR)
        EquipmentDegrade.registerSet(TICKS, GUTHAN_BODY)
        EquipmentDegrade.registerSet(TICKS, GUTHAN_SKIRT)
        EquipmentDegrade.registerSet(TICKS, TORAG_HELM)
        EquipmentDegrade.registerSet(TICKS, TORAG_HAMMER)
        EquipmentDegrade.registerSet(TICKS, TORAG_BODY)
        EquipmentDegrade.registerSet(TICKS, TORAG_LEGS)
        EquipmentDegrade.registerSet(TICKS, VERAC_HELM)
        EquipmentDegrade.registerSet(TICKS, VERAC_FLAIL)
        EquipmentDegrade.registerSet(TICKS, VERAC_BRASS)
        EquipmentDegrade.registerSet(TICKS, VERAC_SKIRT)
        return this
    }

    override fun fireEvent(
        identifier: String?,
        vararg args: Any?,
    ): Any {
        return Unit
    }
}
