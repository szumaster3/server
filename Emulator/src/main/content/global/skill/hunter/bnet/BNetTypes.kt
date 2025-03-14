package content.global.skill.hunter.bnet

import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.drop.DropFrequency
import core.game.node.entity.player.Player
import core.game.node.item.ChanceItem
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Items
import org.rs.consts.NPCs

enum class BNetTypes(
    val node: BNetNode,
) {
    RUBY_HARVEST(
        BNetNode(
            intArrayOf(5085),
            intArrayOf(15, 80, 75),
            doubleArrayOf(24.0, 300.0, 50.0),
            arrayOf(
                Graphics(913),
                Graphics(917),
            ),
            ChanceItem(10020),
        ),
    ),
    SAPPHIRE_GLACIALIS(
        BNetNode(
            intArrayOf(5084, 7499),
            intArrayOf(25, 85, 80),
            doubleArrayOf(34.0, 400.0, 70.0),
            arrayOf(
                Graphics(912),
                Graphics(916),
            ),
            ChanceItem(10018),
        ),
    ),
    SNOWRY_KNIGHT(
        BNetNode(
            intArrayOf(5083, 7498),
            intArrayOf(35, 90, 85),
            doubleArrayOf(44.0, 500.0, 100.0),
            arrayOf(
                Graphics(911),
                Graphics(915),
            ),
            ChanceItem(10016),
        ),
    ),
    BLACK_WARLOCK(
        BNetNode(
            intArrayOf(5082),
            intArrayOf(45, 95, 90),
            doubleArrayOf(54.0, 650.0, 125.0),
            arrayOf(
                Graphics(910),
                Graphics(914),
            ),
            ChanceItem(10014),
        ),
    ),
    BABY_IMPLING(
        ImplingNode(
            intArrayOf(1028, 6055),
            17,
            20.0,
            18.0,
            Item(11238),
            ChanceItem(1755, 1, 1, DropFrequency.COMMON),
            ChanceItem(1734, 1, 1, DropFrequency.COMMON),
            ChanceItem(1733, 1, 1, DropFrequency.COMMON),
            ChanceItem(946, 1, 1, DropFrequency.COMMON),
            ChanceItem(1985, 1, 1, DropFrequency.COMMON),
            ChanceItem(2347, 1, 1, DropFrequency.COMMON),
            ChanceItem(1759, 1, 1, DropFrequency.COMMON),
            ChanceItem(1927, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(319, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(2007, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(2007, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(1779, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(7170, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(401, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(1438, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(2355, 1, 1, DropFrequency.RARE),
            ChanceItem(1607, 1, 1, DropFrequency.RARE),
            ChanceItem(1743, 1, 1, DropFrequency.RARE),
            ChanceItem(379, 1, 1, DropFrequency.RARE),
            ChanceItem(1761, 1, 1, DropFrequency.RARE),
        ),
    ),
    YOUNG_IMPLING(
        ImplingNode(
            intArrayOf(1029, 6056),
            22,
            22.0,
            20.0,
            Item(11240),
            ChanceItem(1539, 5, 5, DropFrequency.COMMON),
            ChanceItem(1901, 1, 1, DropFrequency.COMMON),
            ChanceItem(7936, 1, 1, DropFrequency.COMMON),
            ChanceItem(361, 1, 1, DropFrequency.COMMON),
            ChanceItem(1523, 1, 1, DropFrequency.COMMON),
            ChanceItem(453, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(1777, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(2293, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(1353, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(2359, 1, 1, DropFrequency.RARE),
        ),
    ),
    GOURMET_IMPLING(
        ImplingNode(
            intArrayOf(1030, 6057),
            28,
            24.0,
            22.0,
            Item(11242),
            ChanceItem(365, 1, 1, DropFrequency.COMMON),
            ChanceItem(361, 1, 1, DropFrequency.COMMON),
            ChanceItem(2011, 1, 1, DropFrequency.COMMON),
            ChanceItem(1897, 1, 1, DropFrequency.COMMON),
            ChanceItem(2327, 1, 1, DropFrequency.COMMON),
            ChanceItem(5004, 1, 1, DropFrequency.COMMON),
            ChanceItem(2007, 1, 1, DropFrequency.COMMON),
            ChanceItem(5970, 1, 1, DropFrequency.COMMON),
            ChanceItem(365, 4, 4, DropFrequency.UNCOMMON),
            ChanceItem(3145, 2, 2, DropFrequency.RARE),
            ChanceItem(7178, 1, 5, DropFrequency.UNCOMMON),
            ChanceItem(5755, 1, 1, DropFrequency.RARE),
            ChanceItem(386, 3, 3, DropFrequency.UNCOMMON),
            ChanceItem(5406, 1, 1, DropFrequency.RARE),
            ChanceItem(10136, 1, 1, DropFrequency.RARE),
            ChanceItem(1883, 1, 1, DropFrequency.UNCOMMON),
        ),
    ),
    EARTH_IMPLING(
        ImplingNode(
            intArrayOf(1031, 6058),
            36,
            27.0,
            25.0,
            Item(11244),
            ChanceItem(6033, 6, 6, DropFrequency.COMMON),
            ChanceItem(1440, 1, 1, DropFrequency.COMMON),
            ChanceItem(5535, 1, 1, DropFrequency.COMMON),
            ChanceItem(557, 32, 32, DropFrequency.COMMON),
            ChanceItem(1442, 1, 1, DropFrequency.COMMON),
            ChanceItem(1784, 4, 4, DropFrequency.UNCOMMON),
            ChanceItem(447, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(447, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(1606, 2, 2, DropFrequency.RARE),
        ),
    ),
    ESSENCE_IMPLING(
        ImplingNode(
            intArrayOf(1032, 6059),
            42,
            29.0,
            27.0,
            Item(11246),
            ChanceItem(7937, 20, 20, DropFrequency.COMMON),
            ChanceItem(555, 30, 30, DropFrequency.COMMON),
            ChanceItem(556, 30, 30, DropFrequency.COMMON),
            ChanceItem(558, 25, 25, DropFrequency.COMMON),
            ChanceItem(559, 28, 28, DropFrequency.COMMON),
            ChanceItem(562, 4, 4, DropFrequency.COMMON),
            ChanceItem(1448, 1, 1, DropFrequency.COMMON),
            ChanceItem(564, 4, 4, DropFrequency.UNCOMMON),
            ChanceItem(565, 7, 7, DropFrequency.RARE),
            ChanceItem(563, 13, 13, DropFrequency.RARE),
            ChanceItem(566, 11, 11, DropFrequency.RARE),
        ),
    ),
    ECLECTIC_IMPLING(
        ImplingNode(
            intArrayOf(1033, 6060),
            50,
            32.0,
            30.0,
            Item(11248),
            ChanceItem(1273, 1, 1, DropFrequency.COMMON),
            ChanceItem(1199, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(2493, 1, 1, DropFrequency.RARE),
            ChanceItem(10083, 1, 1, DropFrequency.RARE),
            ChanceItem(1213, 1, 1, DropFrequency.RARE),
            ChanceItem(1391, 1, 1, DropFrequency.VERY_RARE),
            ChanceItem(5970, 1, 1, DropFrequency.COMMON),
            ChanceItem(231, 1, 1, DropFrequency.COMMON),
            ChanceItem(556, 30, 57, DropFrequency.COMMON),
            ChanceItem(8779, 4, 4, DropFrequency.COMMON),
            ChanceItem(4527, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(444, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(2358, 5, 5, DropFrequency.UNCOMMON),
            ChanceItem(7937, 20, 35, DropFrequency.UNCOMMON),
            ChanceItem(237, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(450, 10, 10, DropFrequency.RARE),
            ChanceItem(5760, 2, 2, DropFrequency.RARE),
            ChanceItem(7208, 1, 1, DropFrequency.RARE),
            ChanceItem(5321, 3, 3, DropFrequency.RARE),
            ChanceItem(1601, 1, 1, DropFrequency.VERY_RARE),
        ),
    ),
    NATURE_IMPLING(
        ImplingNode(
            intArrayOf(1034, 6061),
            58,
            36.0,
            34.0,
            Item(11250),
            ChanceItem(5303, 1, 1, DropFrequency.VERY_RARE),
            ChanceItem(270, 2, 2, DropFrequency.VERY_RARE),
            ChanceItem(5295, 1, 1, DropFrequency.RARE),
            ChanceItem(5304, 1, 1, DropFrequency.RARE),
            ChanceItem(5298, 5, 5, DropFrequency.UNCOMMON),
            ChanceItem(5299, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(5297, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(5974, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(3000, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(5285, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(5286, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(5100, 1, 1, DropFrequency.COMMON),
            ChanceItem(5104, 1, 1, DropFrequency.COMMON),
            ChanceItem(5281, 1, 1, DropFrequency.COMMON),
            ChanceItem(5294, 1, 1, DropFrequency.COMMON),
            ChanceItem(6016, 1, 1, DropFrequency.COMMON),
            ChanceItem(1513, 1, 1, DropFrequency.COMMON),
            ChanceItem(254, 4, 4, DropFrequency.COMMON),
            ChanceItem(5313, 1, 1, DropFrequency.UNCOMMON),
        ),
    ),
    MAGPIE_IMPLING(
        ImplingNode(
            intArrayOf(1035, 6062),
            65,
            216.0,
            44.0,
            Item(11252),
            500,
            ChanceItem(1682, 3, 3, DropFrequency.COMMON),
            ChanceItem(1732, 3, 3, DropFrequency.COMMON),
            ChanceItem(2569, 3, 3, DropFrequency.COMMON),
            ChanceItem(3391, 1, 1, DropFrequency.COMMON),
            ChanceItem(1347, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(2571, 4, 4, DropFrequency.UNCOMMON),
            ChanceItem(4097, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(4095, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(1215, 1, 1, DropFrequency.RARE),
            ChanceItem(1185, 1, 1, DropFrequency.RARE),
            ChanceItem(5541, 1, 1, DropFrequency.COMMON),
            ChanceItem(1748, 6, 6, DropFrequency.COMMON),
            ChanceItem(2364, 2, 2, DropFrequency.UNCOMMON),
            ChanceItem(1602, 4, 4, DropFrequency.RARE),
            ChanceItem(5287, 1, 1, DropFrequency.RARE),
            ChanceItem(985, 1, 1, DropFrequency.RARE),
            ChanceItem(987, 1, 1, DropFrequency.RARE),
            ChanceItem(993, 1, 1, DropFrequency.VERY_RARE),
            ChanceItem(5300, 1, 1, DropFrequency.VERY_RARE),
        ),
    ),
    NINJA_IMPLING(
        ImplingNode(
            intArrayOf(6053, 6063),
            74,
            240.0,
            50.0,
            Item(11254),
            2000,
            ChanceItem(4097, 1, 1, DropFrequency.COMMON),
            ChanceItem(1113, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(3385, 1, 1, DropFrequency.COMMON),
            ChanceItem(1215, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(811, 70, 70, DropFrequency.UNCOMMON),
            ChanceItem(1333, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(1347, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(9342, 2, 2, DropFrequency.UNCOMMON),
            ChanceItem(9194, 4, 4, DropFrequency.RARE),
            ChanceItem(140, 4, 4, DropFrequency.COMMON),
            ChanceItem(6155, 3, 3, DropFrequency.UNCOMMON),
            ChanceItem(1748, 10, 16, DropFrequency.COMMON),
        ),
    ),
    PIRATE_IMPLING(
        ImplingNode(
            intArrayOf(NPCs.PIRATE_IMPLING_7845, NPCs.PIRATE_IMPLING_7846),
            76,
            270.0,
            57.0,
            Item(Items.PIRATE_IMPLING_JAR_13337),
            2500,
            ChanceItem(Items.PIRATE_BOOTS_7114, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.GOLD_BAR_2358, 15, 15, DropFrequency.UNCOMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_7110, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_7122, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_7128, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_7134, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_13358, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_13360, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.STRIPY_PIRATE_SHIRT_13362, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_7116, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_7126, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_7132, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_7138, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_13364, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_13366, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_LEGGINGS_13368, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_BANDANA_7112, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_BANDANA_7124, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_BANDANA_7130, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.PIRATE_BANDANA_7136, 1, 1, DropFrequency.COMMON),
            ChanceItem(Items.BANDANA_AND_EYEPATCH_8924, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.BANDANA_AND_EYEPATCH_8926, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.BANDANA_AND_EYEPATCH_8998, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.BANDANA_AND_EYEPATCH_9000, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(Items.PIECES_OF_EIGHT_8951, 1, 10, DropFrequency.VERY_RARE),
            ChanceItem(Items.LEFT_EYEPATCH_13355, 1, 1, DropFrequency.RARE),
        ),
    ),
    DRAGON_IMPLING(
        ImplingNode(
            intArrayOf(6054, 6064),
            83,
            300.0,
            65.0,
            Item(11256),
            3000,
            ChanceItem(1705, 2, 3, DropFrequency.RARE),
            ChanceItem(4093, 1, 1, DropFrequency.VERY_RARE),
            ChanceItem(1684, 2, 3, DropFrequency.VERY_RARE),
            ChanceItem(11212, 100, 500, DropFrequency.COMMON),
            ChanceItem(9341, 3, 40, DropFrequency.COMMON),
            ChanceItem(1305, 1, 1, DropFrequency.COMMON),
            ChanceItem(5699, 3, 3, DropFrequency.UNCOMMON),
            ChanceItem(11230, 105, 350, DropFrequency.UNCOMMON),
            ChanceItem(11232, 105, 350, DropFrequency.COMMON),
            ChanceItem(11237, 100, 500, DropFrequency.COMMON),
            ChanceItem(9193, 10, 49, DropFrequency.COMMON),
            ChanceItem(535, 111, 297, DropFrequency.COMMON),
            ChanceItem(5316, 1, 1, DropFrequency.UNCOMMON),
            ChanceItem(537, 52, 99, DropFrequency.UNCOMMON),
            ChanceItem(1616, 3, 6, DropFrequency.UNCOMMON),
            ChanceItem(5300, 6, 6, DropFrequency.RARE),
            ChanceItem(7219, 5, 15, DropFrequency.RARE),
        ),
    ),
    ;

    fun handle(
        player: Player,
        npc: NPC,
    ) {
        player.pulseManager.run(BNetPulse(player, npc, node))
    }

    companion object {
        private val implings = mutableListOf<ImplingNode>()

        @JvmStatic
        fun getImpling(player: Player): ImplingNode? {
            return implings.firstOrNull { player.inventory.containsItem(it.reward) }
        }

        @JvmStatic
        fun forNpc(npc: NPC): BNetTypes? {
            return values().firstOrNull { type ->
                type.node.npcs.contains(npc.id)
            }
        }

        @JvmStatic
        fun forItem(item: Item): BNetNode? {
            return values()
                .firstOrNull { type ->
                    type.node.reward.id == item.id
                }?.node
        }

        fun getImplings(): List<ImplingNode> = implings

        init {
            implings.addAll(
                values().mapNotNull { type ->
                    type.node as? ImplingNode
                },
            )
        }
    }
}
