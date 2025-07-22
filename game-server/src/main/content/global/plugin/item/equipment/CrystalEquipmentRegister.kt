package content.global.plugin.item.equipment

import core.Util.convertToIntegerArray
import core.api.StartupListener
import core.plugin.Initializable
import org.rs.consts.Items

/**
 * The Crystal equipment.
 */
@Initializable
class CrystalEquipmentRegister : StartupListener {

    private val shield = intArrayOf(
        Items.NEW_CRYSTAL_SHIELD_4224,
        Items.CRYSTAL_SHIELD_FULL_4225,
        Items.CRYSTAL_SHIELD_9_10_4226,
        Items.CRYSTAL_SHIELD_8_10_4227,
        Items.CRYSTAL_SHIELD_7_10_4228,
        Items.CRYSTAL_SHIELD_6_10_4229,
        Items.CRYSTAL_SHIELD_5_10_4230,
        Items.CRYSTAL_SHIELD_4_10_4231,
        Items.CRYSTAL_SHIELD_3_10_4232,
        Items.CRYSTAL_SHIELD_2_10_4233,
        Items.CRYSTAL_SHIELD_1_10_4234,
        Items.CRYSTAL_SEED_4207
    )

    private val bow = intArrayOf(
        Items.NEW_CRYSTAL_BOW_4212,
        Items.CRYSTAL_BOW_FULL_4214,
        Items.CRYSTAL_BOW_9_10_4215,
        Items.CRYSTAL_BOW_8_10_4216,
        Items.CRYSTAL_BOW_7_10_4217,
        Items.CRYSTAL_BOW_6_10_4218,
        Items.CRYSTAL_BOW_5_10_4219,
        Items.CRYSTAL_BOW_4_10_4220,
        Items.CRYSTAL_BOW_3_10_4221,
        Items.CRYSTAL_BOW_2_10_4222,
        Items.CRYSTAL_BOW_1_10_4223,
        Items.CRYSTAL_SEED_4207
    )

    override fun startup() {
        val shieldWrapper = convertToIntegerArray(shield).requireNoNulls()
        val bowWrapper = convertToIntegerArray(bow).requireNoNulls()

        EquipmentDegrade.registerSet(250, shieldWrapper)
        EquipmentDegrade.registerSet(250, bowWrapper)
    }
}