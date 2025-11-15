package content.global.skill.construction.decoration

import content.global.skill.construction.Decoration
import shared.consts.Animations

/**
 * Enum representing all chairs, benches, and thrones in the player-owned house (POH).
 *
 * Entry:
 *  - [objectId]: the id of the object.
 *  - [anim]: the animation id when the player starts sitting down.
 *  - [sitAnim]: the animation id for the final seated position.
 */
enum class Chairs(
    val objectId: Int,
    val anim: Int,
    val sitAnim: Int
) {
    CRUDE_CHAIR(Decoration.CRUDE_CHAIR.objectId, Animations.SITTING_IN_CRUDE_CHAIR_4073, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),
    WOODEN_CHAIR(Decoration.WOODEN_CHAIR.objectId, Animations.SITTING_IN_CHAIR_4075, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),
    ROCKING_CHAIR(Decoration.ROCKING_CHAIR.objectId, Animations.ROCKING_CHAIR_4079, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),
    OAK_CHAIR(Decoration.OAK_CHAIR.objectId, Animations.OAK_CHAIR_4081, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),
    OAK_ARMCHAIR(Decoration.OAK_ARMCHAIR.objectId, Animations.OAK_CHAIR_4083, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),
    TEAK_ARMCHAIR(Decoration.TEAK_ARMCHAIR.objectId, Animations.TEAK_CHAIR_4085, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),
    MAHOGANY_ARMCHAIR(Decoration.MAHOGANY_ARMCHAIR.objectId, Animations.MAHOGANY_CHAIR_4087, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),

    BENCH_WOODEN(Decoration.BENCH_WOODEN.objectId, Animations.BENCH_4089, Animations.SITTING_DOWN_4104),
    BENCH_OAK(Decoration.BENCH_OAK.objectId, Animations.OAK_BENCH_4091, Animations.SITTING_DOWN_4104),
    BENCH_CARVED_OAK(Decoration.BENCH_CARVED_OAK.objectId, Animations.SIT_ON_CARVED_OAK_BENCH_4093, Animations.SITTING_DOWN_4104),
    BENCH_TEAK(Decoration.BENCH_TEAK.objectId, Animations.SITTING_ON_TEAK_BENCH_4095, Animations.SITTING_DOWN_4104),
    BENCH_CARVED_TEAK(Decoration.BENCH_CARVED_TEAK.objectId, Animations.SITTING_ON_CARVED_TEAK_BENCH_4097, Animations.SITTING_DOWN_4104),
    BENCH_MAHOGANY(Decoration.BENCH_MAHOGANY.objectId, Animations.SITTING_ON_MAHOGANY_BENCH_4099, Animations.SITTING_DOWN_4104),
    BENCH_GILDED(Decoration.BENCH_GILDED.objectId, Animations.SITTING_ON_GILDED_MAHOGANY_BENCH_4101, Animations.SITTING_DOWN_4104),

    CARVED_TEAK_BENCH(Decoration.CARVED_TEAK_BENCH.objectId, Animations.SITTING_ON_CARVED_TEAK_BENCH_4097, Animations.SITTING_DOWN_4104),
    MAHOGANY_BENCH(Decoration.MAHOGANY_BENCH.objectId, Animations.SITTING_ON_MAHOGANY_BENCH_4099, Animations.SITTING_DOWN_4104),
    GILDED_BENCH(Decoration.GILDED_BENCH.objectId, Animations.SITTING_ON_GILDED_MAHOGANY_BENCH_4101, Animations.SITTING_DOWN_4104),

    OAK_THRONE(Decoration.OAK_THRONE.objectId, Animations.SITTING_IN_THRONE_4111, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),
    TEAK_THRONE(Decoration.TEAK_THRONE.objectId, Animations.SITTING_IN_THRONE_B_4112, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),
    MAHOGANY_THRONE(Decoration.MAHOGANY_THRONE.objectId, Animations.SITTING_IN_THRONE_C_4113, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),
    GILDED_THRONE(Decoration.GILDED_THRONE.objectId, Animations.SITTING_IN_THRONE_D_4114, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),
    SKELETON_THRONE(Decoration.SKELETON_THRONE.objectId, Animations.SITTING_IN_SKELETON_THRONE_4115, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),
    CRYSTAL_THRONE(Decoration.CRYSTAL_THRONE.objectId, Animations.SITTING_IN_CRYSTAL_THRONE_4116, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103),
    DEMONIC_THRONE(Decoration.DEMONIC_THRONE.objectId, Animations.SITTING_IN_DEMONIC_THRONE_4117, Animations.SITTING_DOWN_ON_POH_WORKBENCH_4103);

    companion object {
        private val map = values().associateBy { it.objectId }

        /**
         * Gets the [Chairs] entry for the given object id.
         */
        fun fromId(id: Int) = map[id]
    }
}