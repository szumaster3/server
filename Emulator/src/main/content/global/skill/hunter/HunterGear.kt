package content.global.skill.hunter

import core.api.anyInEquipment
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

enum class HunterGear(
    val chanceRate: Double,
    vararg equipment: Item,
) {
    GLOVE_OF_SILENCE(3.0, Item(Items.GLOVES_OF_SILENCE_10075)),
    SPOTIER_CAPE(5.0, Item(Items.SPOTTIER_CAPE_10071)),
    SPOTTED_CAPE(5.0, Item(Items.SPOTTED_CAPE_10069)),
    LARUPIA(10.00, Item(Items.LARUPIA_HAT_10045), Item(Items.LARUPIA_TOP_10043), Item(Items.LARUPIA_LEGS_10041)),
    DESERT_GEAR(
        10.00,
        Item(Items.DAVY_KEBBIT_HAT_12568),
        Item(Items.DESERT_CAMO_LEGS_10063),
        Item(Items.DESERT_CAMO_TOP_10061),
    ),
    GRAAHK_GEAR(10.00, Item(Items.GRAAHK_HEADDRESS_10051), Item(Items.GRAAHK_LEGS_10047), Item(Items.GRAAHK_TOP_10049)),
    KYATT_GEAR(10.00, Item(Items.KYATT_HAT_10039), Item(Items.KYATT_LEGS_10035), Item(Items.KYATT_TOP_10037)),
    JUNGLE_GEAR(8.00, Item(Items.JUNGLE_CAMO_LEGS_10059), Item(Items.JUNGLE_CAMO_TOP_10057)),
    POLAR_GEAR(8.00, Item(Items.POLAR_CAMO_TOP_10065), Item(Items.POLAR_CAMO_LEGS_10067)),
    ;

    val equipment: Array<Item> = equipment as Array<Item>

    companion object {
        @JvmStatic
        fun inGear(player: Player): HunterGear? {
            var contained = 0
            for (type in values()) {
                for (i in type.equipment) {
                    if (anyInEquipment(player, i.id)) {
                        contained += 1
                    }
                    if (contained == type.equipment.size) {
                        return type
                    }
                }
            }
            return null
        }

        @JvmStatic
        fun getChanceRate(player: Player): Double {
            val gear = inGear(player) ?: return 0.0
            return gear.chanceRate
        }
    }
}
