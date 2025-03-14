package content.global.skill.smithing

import core.game.node.entity.player.Player
import core.game.node.item.Item

enum class SmithingType(
    @JvmField val required: Int,
    @JvmField val child: Int,
    val displayName: Int,
    val button: IntArray,
    val productAmount: Int,
) {
    TYPE_DAGGER(1, 18, 19, intArrayOf(24, 23, 22, 21), 1),
    TYPE_AXE(1, 26, 27, intArrayOf(32, 31, 30, 29), 1),
    TYPE_MACE(1, 34, 35, intArrayOf(40, 39, 38, 37), 1),
    TYPE_MEDIUM_HELM(1, 42, 43, intArrayOf(48, 47, 46, 45), 1),
    TYPE_CROSSBOW_BOLT(1, 50, 51, intArrayOf(56, 55, 54, 53), 10),
    TYPE_Crossbow_Bolt(1, 251, 252, intArrayOf(257, 256, 255, 254), 10),
    TYPE_SWORD(1, 58, 59, intArrayOf(64, 63, 62, 61), 1),
    TYPE_DART_TIP(1, 66, 67, intArrayOf(72, 71, 70, 69), 10),
    TYPE_NAIL(1, 74, 75, intArrayOf(80, 79, 78, 77), 15),
    TYPE_BULLSEYE(1, 162, 163, intArrayOf(168, 167, 166, 165), 1),
    TYPE_SPIT_IRON(1, 90, 91, intArrayOf(96, 95, 94, 93), 1),
    TYPE_WIRE(1, 82, 83, intArrayOf(88, 87, 86, 85), 1),
    TYPE_ARROW_TIP(1, 106, 107, intArrayOf(112, 111, 110, 109), 15),
    TYPE_SCIMITAR(2, 114, 115, intArrayOf(120, 119, 118, 117), 1),
    TYPE_CROSSBOW_LIMB(1, 122, 123, intArrayOf(128, 127, 126, 125), 1),
    TYPE_Crossbow_Limb(1, 259, 260, intArrayOf(265, 264, 263, 262), 1),
    TYPE_LONGSWORD(2, 130, 131, intArrayOf(136, 135, 134, 133), 1),
    TYPE_THROWING_KNIFE(1, 138, 139, intArrayOf(144, 143, 142, 141), 5),
    TYPE_FULL_HELM(2, 146, 147, intArrayOf(152, 151, 150, 149), 1),
    TYPE_SQUARE_SHIELD(2, 154, 155, intArrayOf(160, 159, 158, 157), 1),
    TYPE_OIL_LANTERN(1, 162, 163, intArrayOf(168, 167, 166, 165), 1),
    TYPE_GRAPPLE_TIP(1, 170, 171, intArrayOf(175, 176, 175, 174, 173), 1),
    TYPE_STUDS(1, 98, 99, intArrayOf(104, 103, 102, 101, 100), 1),
    TYPE_WARHAMMER(3, 178, 179, intArrayOf(184, 183, 182, 181), 1),
    TYPE_BATTLE_AXE(3, 186, 187, intArrayOf(192, 191, 190, 189), 1),
    TYPE_CHAINBODY(3, 194, 195, intArrayOf(200, 199, 198, 197), 1),
    TYPE_KITE_SHIELD(3, 202, 203, intArrayOf(208, 207, 206, 205), 1),
    TYPE_CLAWS(2, 210, 211, intArrayOf(216, 215, 214, 213), 1),
    TYPE_TWO_HAND_SWORD(3, 218, 219, intArrayOf(224, 223, 222, 221), 1),
    TYPE_PLATE_SKIRT(3, 226, 227, intArrayOf(232, 231, 230, 229), 1),
    TYPE_PLATELEG(3, 234, 235, intArrayOf(240, 239, 238, 237), 1),
    TYPE_PLATEBODY(5, 242, 243, intArrayOf(248, 247, 246, 245), 1),
    TYPE_PICKAXE(2, 267, 268, intArrayOf(273, 272, 271, 270), 1),
    ;

    companion object {
        fun forButton(
            player: Player,
            bar: Bars?,
            button: Int,
            item: Int,
        ): Int {
            var count = 0
            if (bar == null) {
                return -1
            }
            for (i in bar.smithingType.button.indices) {
                if (bar.smithingType.button[i] != button) {
                    count++
                } else {
                    if (count == 0) {
                        count = 1
                    } else if (count == 1) {
                        count = 5
                    } else if (count == 2) {
                        count = -1
                    } else if (count == 3) {
                        count = player.inventory.getAmount(Item(item))
                    }
                    return count
                }
            }
            return -1
        }
    }
}
