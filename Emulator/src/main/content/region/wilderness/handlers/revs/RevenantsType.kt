package content.region.wilderness.handlers.revs

import core.cache.def.impl.NPCDefinition
import org.rs.consts.NPCs

enum class RevenantsType(
    val maxHit: Int,
    vararg val ids: Int,
) {
    IMP(
        8,
        NPCs.REVENANT_IMP_6604,
        NPCs.REVENANT_IMP_6635,
        NPCs.REVENANT_IMP_6655,
        NPCs.REVENANT_IMP_6666,
        NPCs.REVENANT_IMP_6677,
        NPCs.REVENANT_IMP_6697,
        NPCs.REVENANT_IMP_6703,
        NPCs.REVENANT_IMP_6715,
    ),

    GOBLIN(
        12,
        NPCs.REVENANT_GOBLIN_6605,
        NPCs.REVENANT_GOBLIN_6612,
        NPCs.REVENANT_GOBLIN_6616,
        NPCs.REVENANT_GOBLIN_6620,
        NPCs.REVENANT_GOBLIN_6636,
        NPCs.REVENANT_GOBLIN_6637,
        NPCs.REVENANT_GOBLIN_6638,
        NPCs.REVENANT_GOBLIN_6639,
        NPCs.REVENANT_GOBLIN_6651,
        NPCs.REVENANT_GOBLIN_6656,
        NPCs.REVENANT_GOBLIN_6657,
        NPCs.REVENANT_GOBLIN_6658,
        NPCs.REVENANT_GOBLIN_6667,
        NPCs.REVENANT_GOBLIN_6678,
        NPCs.REVENANT_GOBLIN_6679,
        NPCs.REVENANT_GOBLIN_6680,
        NPCs.REVENANT_GOBLIN_6681,
        NPCs.REVENANT_GOBLIN_6693,
        NPCs.REVENANT_GOBLIN_6698,
        NPCs.REVENANT_GOBLIN_6699,
        NPCs.REVENANT_GOBLIN_6704,
        NPCs.REVENANT_GOBLIN_6705,
        NPCs.REVENANT_GOBLIN_6706,
        NPCs.REVENANT_GOBLIN_6707,
        NPCs.REVENANT_GOBLIN_6716,
        NPCs.REVENANT_GOBLIN_6717,
        NPCs.REVENANT_GOBLIN_6718,
        NPCs.REVENANT_GOBLIN_6719,
    ),

    ICEFIEND(
        20,
        NPCs.REVENANT_ICEFIEND_6606,
        NPCs.REVENANT_ICEFIEND_6621,
        NPCs.REVENANT_ICEFIEND_6628,
        NPCs.REVENANT_ICEFIEND_6640,
        NPCs.REVENANT_ICEFIEND_6659,
        NPCs.REVENANT_ICEFIEND_6682,
        NPCs.REVENANT_ICEFIEND_6694,
        NPCs.REVENANT_ICEFIEND_6708,
        NPCs.REVENANT_ICEFIEND_6720,
    ),

    PYREFIEND(
        20,
        NPCs.REVENANT_PYREFIEND_6622,
        NPCs.REVENANT_PYREFIEND_6631,
        NPCs.REVENANT_PYREFIEND_6641,
        NPCs.REVENANT_PYREFIEND_6660,
        NPCs.REVENANT_PYREFIEND_6668,
        NPCs.REVENANT_PYREFIEND_6683,
        NPCs.REVENANT_PYREFIEND_6709,
        NPCs.REVENANT_PYREFIEND_6721,
    ),

    HOGOBLIN(
        20,
        NPCs.REVENANT_HOBGOBLIN_6608,
        NPCs.REVENANT_HOBGOBLIN_6642,
        NPCs.REVENANT_HOBGOBLIN_6661,
        NPCs.REVENANT_HOBGOBLIN_6684,
        NPCs.REVENANT_HOBGOBLIN_6710,
        NPCs.REVENANT_HOBGOBLIN_6722,
        NPCs.REVENANT_HOBGOBLIN_6727,
    ),

    VAMPIRE(
        22,
        NPCs.REVENANT_VAMPIRE_6613,
        NPCs.REVENANT_VAMPIRE_6623,
        NPCs.REVENANT_VAMPIRE_6643,
        NPCs.REVENANT_VAMPIRE_6652,
        NPCs.REVENANT_VAMPIRE_6662,
        NPCs.REVENANT_VAMPIRE_6669,
        NPCs.REVENANT_VAMPIRE_6671,
        NPCs.REVENANT_VAMPIRE_6674,
        NPCs.REVENANT_VAMPIRE_6685,
        NPCs.REVENANT_VAMPIRE_6695,
        NPCs.REVENANT_VAMPIRE_6700,
        NPCs.REVENANT_VAMPIRE_6711,
        NPCs.REVENANT_VAMPIRE_6723,
    ),

    WEREWOLF(
        23,
        NPCs.REVENANT_WEREWOLF_6607,
        NPCs.REVENANT_WEREWOLF_6609,
        NPCs.REVENANT_WEREWOLF_6614,
        NPCs.REVENANT_WEREWOLF_6617,
        NPCs.REVENANT_WEREWOLF_6625,
        NPCs.REVENANT_WEREWOLF_6632,
        NPCs.REVENANT_WEREWOLF_6644,
        NPCs.REVENANT_WEREWOLF_6663,
        NPCs.REVENANT_WEREWOLF_6675,
        NPCs.REVENANT_WEREWOLF_6686,
        NPCs.REVENANT_WEREWOLF_6701,
        NPCs.REVENANT_WEREWOLF_6712,
        NPCs.REVENANT_WEREWOLF_6724,
        NPCs.REVENANT_WEREWOLF_6728,
    ),

    CYCLOPS(
        24,
        NPCs.REVENANT_CYCLOPS_6645,
        NPCs.REVENANT_CYCLOPS_6687,
    ),

    HELLHOUND(
        25,
        NPCs.REVENANT_HELLHOUND_6646,
        NPCs.REVENANT_HELLHOUND_6688,
    ),

    DEMON(
        25,
        NPCs.REVENANT_DEMON_6647,
        NPCs.REVENANT_DEMON_6689,
    ),

    ORK(
        31,
        NPCs.REVENANT_ORK_6610,
        NPCs.REVENANT_ORK_6615,
        NPCs.REVENANT_ORK_6618,
        NPCs.REVENANT_ORK_6624,
        NPCs.REVENANT_ORK_6626,
        NPCs.REVENANT_ORK_6629,
        NPCs.REVENANT_ORK_6633,
        NPCs.REVENANT_ORK_6648,
        NPCs.REVENANT_ORK_6653,
        NPCs.REVENANT_ORK_6664,
        NPCs.REVENANT_ORK_6670,
        NPCs.REVENANT_ORK_6672,
        NPCs.REVENANT_ORK_6690,
        NPCs.REVENANT_ORK_6696,
        NPCs.REVENANT_ORK_6702,
        NPCs.REVENANT_ORK_6713,
        NPCs.REVENANT_ORK_6725,
        NPCs.REVENANT_ORK_6729,
    ),

    DARK_BEAST(
        36,
        NPCs.REVENANT_DARK_BEAST_6649,
        NPCs.REVENANT_DARK_BEAST_6691,
    ),

    KNIGHT(
        42,
        NPCs.REVENANT_KNIGHT_6611,
        NPCs.REVENANT_KNIGHT_6619,
        NPCs.REVENANT_KNIGHT_6627,
        NPCs.REVENANT_KNIGHT_6630,
        NPCs.REVENANT_KNIGHT_6634,
        NPCs.REVENANT_KNIGHT_6650,
        NPCs.REVENANT_KNIGHT_6654,
        NPCs.REVENANT_KNIGHT_6665,
        NPCs.REVENANT_KNIGHT_6673,
        NPCs.REVENANT_KNIGHT_6676,
        NPCs.REVENANT_KNIGHT_6692,
        NPCs.REVENANT_KNIGHT_6714,
        NPCs.REVENANT_KNIGHT_6726,
        NPCs.REVENANT_KNIGHT_6730,
    ),

    DRAGON(60, NPCs.REVENANT_DRAGON_6998, NPCs.REVENANT_DRAGON_6999),
    ;

    companion object {
        private val idToTypeMap: Map<Int, RevenantsType> =
            values()
                .flatMap { type ->
                    type.ids.toList().map { id -> id to type }
                }.toMap()

        fun forId(id: Int): RevenantsType? = idToTypeMap[id]

        private val sortedByCombatLevel: List<RevenantsType> =
            values().sortedBy { NPCDefinition.forId(it.ids[0]).combatLevel }

        fun getClosestHigherOrEqual(combatLevel: Int): RevenantsType {
            return sortedByCombatLevel.find { NPCDefinition.forId(it.ids[0]).combatLevel >= combatLevel } ?: DRAGON
        }

        val allIds: List<Int> = values().flatMap { it.ids.toList() }
    }
}
