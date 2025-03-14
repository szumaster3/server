package content.global.skill.farming

import content.global.skill.farming.timers.CropGrowth
import core.api.getOrStartTimer
import core.cache.def.impl.SceneryDefinition
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery

enum class FarmingPatch(
    val varbit: Int,
    val type: PatchType,
) {
    S_FALADOR_ALLOTMENT_NW(708, PatchType.ALLOTMENT),
    S_FALADOR_ALLOTMENT_SE(709, PatchType.ALLOTMENT),
    CATHERBY_ALLOTMENT_N(710, PatchType.ALLOTMENT),
    CATHERBY_ALLOTMENT_S(711, PatchType.ALLOTMENT),
    ARDOUGNE_ALLOTMENT_S(713, PatchType.ALLOTMENT),
    ARDOUGNE_ALLOTMENT_N(712, PatchType.ALLOTMENT),
    PORT_PHAS_ALLOTMENT_NW(714, PatchType.ALLOTMENT),
    PORT_PHAS_ALLOTMENT_SE(715, PatchType.ALLOTMENT),
    HARMONY_ISLAND_ALLOTMENT(3402, PatchType.ALLOTMENT),

    CATHERBY_HERB_CE(781, PatchType.HERB_PATCH),
    S_FALADOR_HERB_NE(780, PatchType.HERB_PATCH),
    ARDOUGNE_HERB_CE(782, PatchType.HERB_PATCH),
    PORT_PHAS_HERB_NE(783, PatchType.HERB_PATCH),
    TROLL_STRONGHOLD_HERB(2788, PatchType.HERB_PATCH),

    S_FALADOR_FLOWER_C(728, PatchType.FLOWER_PATCH),
    CATHERBY_FLOWER_C(729, PatchType.FLOWER_PATCH),
    ARDOUGNE_FLOWER_C(730, PatchType.FLOWER_PATCH),
    PORT_PHAS_FLOWER_C(731, PatchType.FLOWER_PATCH),
    WILDERNESS_FLOWER(5067, PatchType.FLOWER_PATCH),

    N_FALADOR_TREE(701, PatchType.TREE_PATCH),
    TAVERLEY_TREE(700, PatchType.TREE_PATCH),
    GNOME_STRONGHOLD_TREE(2953, PatchType.TREE_PATCH),
    LUMBRIDGE_TREE(703, PatchType.TREE_PATCH),
    VARROCK_TREE(702, PatchType.TREE_PATCH),

    GNOME_STRONGHOLD_FRUIT_TREE(704, PatchType.FRUIT_TREE_PATCH),
    CATHERBY_FRUIT_TREE(707, PatchType.FRUIT_TREE_PATCH),
    TREE_GNOME_VILLAGE_FRUIT_TREE(705, PatchType.FRUIT_TREE_PATCH),
    BRIMHAVEN_FRUIT_TREE(706, PatchType.FRUIT_TREE_PATCH),
    LLETYA_FRUIT_TREE(4317, PatchType.FRUIT_TREE_PATCH),

    ENTRANA_HOPS(717, PatchType.HOPS_PATCH),
    LUMBRIDGE_HOPS(718, PatchType.HOPS_PATCH),
    MCGRUBOR_HOPS(719, PatchType.HOPS_PATCH),
    YANILLE_HOPS(716, PatchType.HOPS_PATCH),

    CHAMPIONS_GUILD_BUSH(732, PatchType.BUSH_PATCH),
    RIMMINGTON_BUSH(733, PatchType.BUSH_PATCH),
    ARDOUGNE_BUSH(735, PatchType.BUSH_PATCH),
    ETCETERIA_BUSH(734, PatchType.BUSH_PATCH),

    ETCETERIA_SPIRIT_TREE(722, PatchType.SPIRIT_TREE_PATCH),
    PORT_SARIM_SPIRIT_TREE(720, PatchType.SPIRIT_TREE_PATCH),
    KARAMJA_SPIRIT_TREE(724, PatchType.SPIRIT_TREE_PATCH),

    DRAYNOR_BELLADONNA(748, PatchType.BELLADONNA_PATCH),
    CANIFIS_MUSHROOM(746, PatchType.MUSHROOM_PATCH),
    ALKHARID_CACTUS(744, PatchType.CACTUS_PATCH),
    EVIL_TURNIP(4291, PatchType.EVIL_TURNIP_PATCH),
    CALQUAT_TREE(726, PatchType.FRUIT_TREE_PATCH),
    ENRICHED_SEED(5533, PatchType.SPECIAL_PATCH),
    ;

    companion object {
        @JvmField
        val patches = FarmingPatch.values().map { it.varbit to it }.toMap()
        val patchNodes = ArrayList<Int>()
        val nodeMap = HashMap<Int, SceneryDefinition>()

        init {
            patchNodes.addAll(8550..8557)
            patchNodes.addAll(7847..7853)
            patchNodes.addAll(8150..8156)
            patchNodes.addAll(8388..8391)
            patchNodes.add(19147)
            patchNodes.addAll(7962..7965)
            patchNodes.addAll(8173..8176)
            patchNodes.addAll(7577..7580)
            patchNodes.add(23760)
            patchNodes.add(7572)
            patchNodes.add(8337)
            patchNodes.add(27197)
            patchNodes.add(7771)
            patchNodes.add(7807)
            patchNodes.addAll(8382..8383)
            patchNodes.add(8338)
            patchNodes.add(18816)
            patchNodes.add(41339)

            for (patch in patchNodes) {
                val def = SceneryDefinition.forId(patch)
                nodeMap[def.varbitID] = def
            }
        }

        @JvmStatic
        fun forObject(obj: Scenery): FarmingPatch? {
            return forObjectID(obj.id)
        }

        @JvmStatic
        fun forObjectID(id: Int): FarmingPatch? {
            val objDef = SceneryDefinition.forId(id)
            return patches[objDef.varbitID]
        }

        fun getSceneryDefByVarbit(id: Int): SceneryDefinition? {
            return nodeMap[id]
        }
    }

    fun getPatchFor(
        player: Player,
        addPatch: Boolean = true,
    ): Patch {
        val crops = getOrStartTimer<CropGrowth>(player)
        return crops.getPatch(this, addPatch)
    }
}
