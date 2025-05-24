package content.global.travel.balloon

import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.Vars

/**
 * Represents balloon travel data.
 */
enum class Balloons(val areaName: String, val destination: Location, val logId: Int, val requiredLevel: Int, val varbitId: Int, val componentId: Int, val button: Int, val wrapperId: Int) {
    ENTRANA("Entrana", Location(2809, 3356), Items.LOGS_1511, 20, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_ENTRANA_BALLOON_2867, 25, 17, 19133),
    TAVERLEY("Taverley", Location(2940, 3420), Items.LOGS_1511, 20, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_TAVERLEY_BALLOON_2868, 22, 18, 19135),
    CRAFT_GUILD("Crafting Guild", Location(2924, 3303), Items.OAK_LOGS_1521, 30, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_CRAFTING_GUILD_BALLOON_2871, 20, 16, 19141),
    VARROCK("Varrock", Location(3298, 3481), Items.WILLOW_LOGS_1519, 40, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_VARROCK_BALLOON_2872, 21, 19, 19143),
    CASTLE_WARS("Castle Wars", Location(2462, 3108), Items.YEW_LOGS_1515, 50, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_CASTLE_WARS_BALLOON_2869, 24, 14, 19137),
    GRAND_TREE("Grand Tree", Location(2480, 3458), Items.MAGIC_LOGS_1513, 60, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_GRAND_TREE_BALLOON_2870, 23, 15, 19139);

    companion object {
        /**
         * A button map id for balloon locations.
         */
        private val buttonToBalloon: Map<Int, Balloons> by lazy {
            values().associateBy { it.button }
        }

        /**
         * Returns the balloon location to the given interface button id.
         *
         * @param buttonId the ID of the interface button
         * @return the matching [Balloons] instance or null if no match is found
         */
        fun fromButtonId(buttonId: Int): Balloons? = buttonToBalloon[buttonId]

        /**
         * A map that associates scenery object id with their balloon locations.
         */
        private val sceneryToBalloon: Map<Int, Balloons> by lazy {
            values().associateBy { it.wrapperId }
        }

        /**
         * Returns the balloon location to the given scenery id.
         *
         * @param id the ID of the scenery object
         * @return the matching [Balloons] instance or null if no match is found
         */
        fun fromSceneryId(id: Int): Balloons? = sceneryToBalloon[id]

        /**
         * Animation map.
         */
        private val size = values().size

        /**
         * Represents animation ids for balloon travel routes.
         */
        private val animations: Array<IntArray> = Array(size) { IntArray(size) { 0 } }.apply {
            this[ENTRANA.ordinal][TAVERLEY.ordinal] = 5110
            this[TAVERLEY.ordinal][ENTRANA.ordinal] = 5111
            this[ENTRANA.ordinal][CRAFT_GUILD.ordinal] = 5112
            this[CRAFT_GUILD.ordinal][ENTRANA.ordinal] = 5113
            this[ENTRANA.ordinal][VARROCK.ordinal] = 5114
            this[VARROCK.ordinal][ENTRANA.ordinal] = 5115
            this[ENTRANA.ordinal][GRAND_TREE.ordinal] = 5116
            this[GRAND_TREE.ordinal][ENTRANA.ordinal] = 5117
            this[ENTRANA.ordinal][CASTLE_WARS.ordinal] = 5118
            this[CASTLE_WARS.ordinal][ENTRANA.ordinal] = 5119
            this[VARROCK.ordinal][CRAFT_GUILD.ordinal] = 5120
            this[CRAFT_GUILD.ordinal][VARROCK.ordinal] = 5121
            this[VARROCK.ordinal][TAVERLEY.ordinal] = 5122
            this[TAVERLEY.ordinal][VARROCK.ordinal] = 5123
            this[TAVERLEY.ordinal][CRAFT_GUILD.ordinal] = 5124
            this[CRAFT_GUILD.ordinal][TAVERLEY.ordinal] = 5125
            this[TAVERLEY.ordinal][CASTLE_WARS.ordinal] = 5126
            this[CASTLE_WARS.ordinal][TAVERLEY.ordinal] = 5127
            this[CRAFT_GUILD.ordinal][CASTLE_WARS.ordinal] = 5128
            this[CASTLE_WARS.ordinal][CRAFT_GUILD.ordinal] = 5129
            this[VARROCK.ordinal][CASTLE_WARS.ordinal] = 5130
            this[CASTLE_WARS.ordinal][VARROCK.ordinal] = 5131
            this[GRAND_TREE.ordinal][CASTLE_WARS.ordinal] = 5132
            this[CASTLE_WARS.ordinal][GRAND_TREE.ordinal] = 5133
            this[GRAND_TREE.ordinal][CRAFT_GUILD.ordinal] = 5134
            this[CRAFT_GUILD.ordinal][GRAND_TREE.ordinal] = 5135
            this[TAVERLEY.ordinal][GRAND_TREE.ordinal] = 5136
            this[GRAND_TREE.ordinal][TAVERLEY.ordinal] = 5137
            this[VARROCK.ordinal][GRAND_TREE.ordinal] = 5138
            this[GRAND_TREE.ordinal][VARROCK.ordinal] = 5139
        }

        /**
         * Gets the animation id for traveling from one balloon location to another.
         *
         * @param from The origin balloon location.
         * @param to The destination balloon location.
         * @return The animation id.
         * @throws IllegalStateException if no animation exists for the given route.
         */
        fun getAnimationId(from: Balloons, to: Balloons): Int {
            return animations[from.ordinal][to.ordinal].takeIf { it != 0 }
                ?: error("No animation for route [$from] -> [$to]")
        }
    }
}