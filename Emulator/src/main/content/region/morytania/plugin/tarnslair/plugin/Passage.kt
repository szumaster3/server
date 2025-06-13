package content.region.morytania.plugin.tarnslair.plugin

import core.game.world.map.Location
import core.game.node.scenery.Scenery

/**
 * Represents the tarn lair passages.
 */
enum class Passage(val passage: Scenery, val destination: Location) {
    /**
     * The Floor 1 passage 1.
     */
    FLOOR_1_PASSAGE_1(Scenery(20575, Location(3158, 4553, 0)), Location(3158, 4557, 1)),

    /**
     * The Floor 1 passage 2.
     */
    FLOOR_1_PASSAGE_2(Scenery(20575, Location(3184, 4553, 0)), Location(3184, 4557, 1)),

    /**
     * The Floor 1 passage 3.
     */
    FLOOR_1_PASSAGE_3(Scenery(20847, Location(3178, 4561, 0)), Location(3174, 4561, 1)),

    /**
     * The Floor 1 passage 4.
     */
    FLOOR_1_PASSAGE_4(Scenery(20849, Location(3195, 4571, 0)), Location(3195, 4575, 1)),

    /**
     * The Floor 1 passage 5.
     */
    FLOOR_1_PASSAGE_5(Scenery(20575, Location(3147, 4548, 0)), Location(3143, 4548, 1)),

    /**
     * The Floor 1 passage 6.
     */
    FLOOR_1_PASSAGE_6(Scenery(20824, Location(3156, 4559, 0)), Location(3161, 4559, 0)),

    /**
     * The Floor 1 passage 7.
     */
    FLOOR_1_PASSAGE_7(Scenery(20823, Location(3160, 4559, 0)), Location(3155, 4559, 0)),

    /**
     * The Floor 1 passage 8.
     */
    FLOOR_1_PASSAGE_8(Scenery(20822, Location(3163, 4561, 0)), Location(3163, 4565, 0)),

    /**
     * The Floor 1 passage 9.
     */
    FLOOR_1_PASSAGE_9(Scenery(20821, Location(3163, 4564, 0)), Location(3163, 4560, 0)),

    /**
     * The Floor 1 passage 10.
     */
    FLOOR_1_PASSAGE_10(Scenery(20575, Location(3168, 4569, 0)), Location(3172, 4569, 1)),

    /**
     * The Floor 1 passage 11.
     */
    FLOOR_1_PASSAGE_11(Scenery(20842, Location(3184, 4570, 0)), Location(3184, 4566, 1)),

    /**
     * The Floor 1 passage 12.
     */
    FLOOR_1_PASSAGE_12(Scenery(20843, Location(3184, 4580, 0)), Location(3184, 4585, 1)),

    /**
     * The Floor 1 passage 13.
     */
    FLOOR_1_PASSAGE_13(Scenery(20846, Location(3171, 4577, 0)), Location(3175, 4577, 1)),

    /**
     * The Floor 1 passage 14.
     */
    FLOOR_1_PASSAGE_14(Scenery(20855, Location(3168, 4580, 0)), Location(3168, 4586, 0)),

    /**
     * The Floor 1 passage 15.
     */
    FLOOR_1_PASSAGE_15(Scenery(20838, Location(3165, 4577, 0)), Location(3161, 4577, 0)),

    /**
     * The Floor 1 passage 16.
     */
    FLOOR_1_PASSAGE_16(Scenery(20837, Location(3162, 4577, 0)), Location(3166, 4577, 0)),

    /**
     * The Floor 1 passage 17.
     */
    FLOOR_1_PASSAGE_17(Scenery(20836, Location(3150, 4583, 0)), Location(3146, 4583, 1)),

    /**
     * The Floor 1 passage 18.
     */
    FLOOR_1_PASSAGE_18(Scenery(20856, Location(3168, 4585, 0)), Location(3168, 4579, 0)),

    /**
     * The Floor 1 passage 19.
     */
    FLOOR_1_PASSAGE_19(Scenery(20857, Location(3165, 4589, 0)), Location(3161, 4589, 1)),

    /**
     * The Floor 1 passage 20.
     */
    FLOOR_1_PASSAGE_20(Scenery(20864, Location(3168, 4593, 0)), Location(3168, 4597, 0)),

    /**
     * The Floor 1 passage 21.
     */
    FLOOR_1_PASSAGE_21(Scenery(20863, Location(3168, 4596, 0)), Location(3168, 4592, 0)),

    /**
     * The Floor 1 passage 22.
     */
    FLOOR_1_PASSAGE_22(Scenery(20862, Location(3159, 4598, 0)), Location(3149, 4598, 0)),

    /**
     * The Floor 1 passage 23.
     */
    FLOOR_1_PASSAGE_23(Scenery(20861, Location(3150, 4598, 0)), Location(3160, 4598, 0)),

    /**
     * The Floor 1 passage 24.
     */
    FLOOR_1_PASSAGE_24(Scenery(20860, Location(3145, 4593, 0)), Location(3145, 4589, 1)),

    /**
     * The Floor 1 passage 25.
     */
    FLOOR_1_PASSAGE_25(Scenery(20575, Location(3190, 4598, 0)), Location(3194, 4598, 1)),

    /**
     * The Floor 1 passage 26.
     */
    FLOOR_1_PASSAGE_26(Scenery(20871, Location(3185, 4602, 0)), Location(3149, 4644, 0)),

    /**
     * The Floor 1 passage 27.
     */
    FLOOR_1_PASSAGE_27(Scenery(20466, Location(3149, 4643, 0)), Location(3185, 4601, 0)),

    /**
     * The Floor 1 passage 28.
     */
    FLOOR_1_PASSAGE_28(Scenery(20572, Location(3149, 4659, 0)), Location(3149, 4664, 0)),

    /**
     * The Floor 1 passage 29.
     */
    FLOOR_1_PASSAGE_29(Scenery(20573, Location(3149, 4663, 0)), Location(3149, 4658, 0)),

    /**
     * The Floor 1 passage 30.
     */
    FLOOR_1_PASSAGE_30(Scenery(20573, Location(3186, 4631, 0)), Location(3186, 4626, 0)),

    /**
     * The Floor 2 passage 1.
     */
    FLOOR_2_PASSAGE_1(Scenery(20654, Location(3141, 4551, 1)), Location(3141, 4555, 2)),

    /**
     * The Floor 2 passage 2.
     */
    FLOOR_2_PASSAGE_2(Scenery(20619, Location(3144, 4548, 1)), Location(3148, 4548, 0)),

    /**
     * The Floor 2 passage 3.
     */
    FLOOR_2_PASSAGE_3(Scenery(20619, Location(3158, 4556, 1)), Location(3158, 4552, 0)),

    /**
     * The Floor 2 passage 4.
     */
    FLOOR_2_PASSAGE_4(Scenery(20654, Location(3158, 4564, 1)), Location(3158, 4568, 2)),

    /**
     * The Floor 2 passage 5.
     */
    FLOOR_2_PASSAGE_5(Scenery(20848, Location(3175, 4561, 1)), Location(3179, 4561, 0)),

    /**
     * The Floor 2 passage 6.
     */
    FLOOR_2_PASSAGE_6(Scenery(20619, Location(3171, 4569, 1)), Location(3167, 4569, 0)),

    /**
     * The Floor 2 passage 7.
     */
    FLOOR_2_PASSAGE_7(Scenery(20619, Location(3184, 4556, 1)), Location(3184, 4552, 0)),

    /**
     * The Floor 2 passage 8.
     */
    FLOOR_2_PASSAGE_8(Scenery(20841, Location(3184, 4567, 1)), Location(3184, 4571, 0)),

    /**
     * The Floor 2 passage 9.
     */
    FLOOR_2_PASSAGE_9(Scenery(20850, Location(3195, 4574, 1)), Location(3195, 4570, 0)),

    /**
     * The Floor 2 passage 10.
     */
    FLOOR_2_PASSAGE_10(Scenery(20851, Location(3193, 4577, 1)), Location(3189, 4577, 2)),

    /**
     * The Floor 2 passage 11.
     */
    FLOOR_2_PASSAGE_11(Scenery(20854, Location(3186, 4584, 1)), Location(3186, 4578, 2)),

    /**
     * The Floor 2 passage 12.
     */
    FLOOR_2_PASSAGE_12(Scenery(20844, Location(3184, 4584, 1)), Location(3184, 4579, 0)),

    /**
     * The Floor 2 passage 13.
     */
    FLOOR_2_PASSAGE_13(Scenery(20654, Location(3177, 4577, 1)), Location(3181, 4577, 2)),

    /**
     * The Floor 2 passage 14.
     */
    FLOOR_2_PASSAGE_14(Scenery(20845, Location(3174, 4577, 1)), Location(3170, 4577, 0)),

    /**
     * The Floor 2 passage 15.
     */
    FLOOR_2_PASSAGE_15(Scenery(20835, Location(3147, 4583, 1)), Location(3151, 4583, 0)),

    /**
     * The Floor 2 passage 16.
     */
    FLOOR_2_PASSAGE_16(Scenery(20834, Location(3144, 4581, 1)), Location(3144, 4577, 2)),

    /**
     * The Floor 2 passage 17.
     */
    FLOOR_2_PASSAGE_17(Scenery(20859, Location(3145, 4590, 1)), Location(3145, 4594, 0)),

    /**
     * The Floor 2 passage 18.
     */
    FLOOR_2_PASSAGE_18(Scenery(20858, Location(3162, 4589, 1)), Location(3166, 4589, 0)),

    /**
     * The Floor 2 passage 19.
     */
    FLOOR_2_PASSAGE_19(Scenery(20865, Location(3154, 4597, 1)), Location(3151, 4597, 1)),

    /**
     * The Floor 2 passage 20.
     */
    FLOOR_2_PASSAGE_20(Scenery(20866, Location(3152, 4597, 1)), Location(3155, 4597, 1)),

    /**
     * The Floor 2 passage 21.
     */
    FLOOR_2_PASSAGE_21(Scenery(20867, Location(3176, 4598, 1)), Location(3179, 4598, 1)),

    /**
     * The Floor 2 passage 22.
     */
    FLOOR_2_PASSAGE_22(Scenery(20868, Location(3178, 4598, 1)), Location(3175, 4598, 1)),

    /**
     * The Floor 2 passage 23.
     */
    FLOOR_2_PASSAGE_23(Scenery(20619, Location(3193, 4598, 1)), Location(3189, 4598, 0)),

    /**
     * The Floor 2 passage 24.
     */
    FLOOR_2_PASSAGE_24(Scenery(20830, Location(3141, 4564, 1)), Location(3141, 4560, 2)),

    /**
     * The Floor 2 passage 25.
     */
    FLOOR_2_PASSAGE_25(Scenery(20831, Location(3144, 4567, 1)), Location(3144, 4571, 2)),

    /**
     * The Floor 3 passage 1.
     */
    FLOOR_3_PASSAGE_1(Scenery(20684, Location(3141, 4554, 2)), Location(3141, 4550, 1)),

    /**
     * The Floor 3 passage 2.
     */
    FLOOR_3_PASSAGE_2(Scenery(20829, Location(3141, 4561, 2)), Location(3141, 4565, 1)),

    /**
     * The Floor 3 passage 3.
     */
    FLOOR_3_PASSAGE_3(Scenery(20684, Location(3158, 4567, 2)), Location(3158, 4563, 1)),

    /**
     * The Floor 3 passage 4.
     */
    FLOOR_3_PASSAGE_4(Scenery(20832, Location(3144, 4570, 2)), Location(3144, 4566, 1)),

    /**
     * The Floor 3 passage 5.
     */
    FLOOR_3_PASSAGE_5(Scenery(20833, Location(3144, 4578, 2)), Location(3144, 4582, 1)),

    /**
     * The Floor 3 passage 6.
     */
    FLOOR_3_PASSAGE_6(Scenery(20721, Location(3180, 4577, 2)), Location(3176, 4577, 1)),

    /**
     * The Floor 3 passage 7.
     */
    FLOOR_3_PASSAGE_7(Scenery(20853, Location(3186, 4579, 2)), Location(3186, 4585, 1)),

    /**
     * The Floor 3 passage 8.
     */
    FLOOR_3_PASSAGE_8(Scenery(20852, Location(3190, 4577, 2)), Location(3194, 4577, 1));

    companion object {
        val allPassages: HashMap<Location, Passage>
            /**
             * Gets all passages.
             *
             * @return the all passages
             */
            get() {
                val map =
                    HashMap<Location, Passage>()
                for (passage in values()) {
                    map[passage.passage.location] = passage
                }
                return map
            }
    }
}