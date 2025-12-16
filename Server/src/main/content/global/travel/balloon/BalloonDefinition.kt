package content.global.travel.balloon;

import core.game.world.map.Location
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Vars

/**
 * Represents balloon travel data.
 */
enum class BalloonDefinition(val destName: String, val npc: Int, val destination: Location, val logId: Int, val requiredLevel: Int, val varbitId: Int, val componentId: Int, val button: Int, val wrapperId: Int) {
    ENTRANA("in Entrana", NPCs.AUGUSTE_5049, Location(2809, 3356), Items.LOGS_1511, 20, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_ENTRANA_BALLOON_2867, 25, 17, 19133),
    TAVERLEY("in Taverley", NPCs.ASSISTANT_STAN_5057, Location(2940, 3420), Items.LOGS_1511, 20, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_TAVERLEY_BALLOON_2868, 22, 18, 19135),
    CRAFT_GUILD("at the Crafting Guild", NPCs.ASSISTANT_BROCK_5054, Location(2924, 3303), Items.OAK_LOGS_1521, 30, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_CRAFTING_GUILD_BALLOON_2871, 20, 16, 19141),
    VARROCK("in Varrock", NPCs.ASSISTANT_SERF_5053, Location(3298, 3481), Items.WILLOW_LOGS_1519, 40, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_VARROCK_BALLOON_2872, 21, 19, 19143),
    CASTLE_WARS("at Castle Wars", NPCs.ASSISTANT_MARROW_5055, Location(2462, 3108), Items.YEW_LOGS_1515, 50, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_CASTLE_WARS_BALLOON_2869, 24, 14, 19137),
    GRAND_TREE("at the Gnome Stronghold", NPCs.ASSISTANT_LE_SMITH_5056, Location(2480, 3458), Items.MAGIC_LOGS_1513, 60, Vars.VARBIT_QUEST_ENLIGHTENED_JOURNEY_GRAND_TREE_BALLOON_2870, 23, 15, 19139);

    companion object {
        /**
         * Maps NPC id to [BalloonDefinition].
         */
        private val npcMap: Map<Int, BalloonDefinition> by lazy {
            values().associateBy { it.npc }
        }

        /**
         * Maps interface button to [BalloonDefinition].
         */
        private val buttonToBalloon: Map<Int, BalloonDefinition> by lazy {
            values().associateBy { it.button }
        }

        /**
         * Gets [BalloonDefinition] for given button.
         */
        fun fromButtonId(buttonId: Int): BalloonDefinition? = buttonToBalloon[buttonId]

        /**
         * Maps scenery to [BalloonDefinition].
         */
        private val sceneryToBalloon: Map<Int, BalloonDefinition> by lazy {
            values().associateBy { it.wrapperId }
        }

        /**
         * Gets [BalloonDefinition] for given scenery.
         */
        fun fromSceneryId(id: Int): BalloonDefinition? = sceneryToBalloon[id]

        /**
         * Gets [BalloonDefinition] for given NPC.
         */
        fun fromNpcId(npcId: Int): BalloonDefinition? = npcMap[npcId]

        /**
         * Animation ids for balloon travel routes.
         */
        private val animations: Map<Pair<BalloonDefinition, BalloonDefinition>, Int> = mapOf(
            ENTRANA to TAVERLEY to 5110,
            TAVERLEY to ENTRANA to 5111,
            ENTRANA to CRAFT_GUILD to 5112,
            CRAFT_GUILD to ENTRANA to 5113,
            ENTRANA to VARROCK to 5114,
            VARROCK to ENTRANA to 5115,
            ENTRANA to GRAND_TREE to 5116,
            GRAND_TREE to ENTRANA to 5117,
            ENTRANA to CASTLE_WARS to 5118,
            CASTLE_WARS to ENTRANA to 5119,
            VARROCK to CRAFT_GUILD to 5120,
            CRAFT_GUILD to VARROCK to 5121,
            VARROCK to TAVERLEY to 5122,
            TAVERLEY to VARROCK to 5123,
            TAVERLEY to CRAFT_GUILD to 5124,
            CRAFT_GUILD to TAVERLEY to 5125,
            TAVERLEY to CASTLE_WARS to 5126,
            CASTLE_WARS to TAVERLEY to 5127,
            CRAFT_GUILD to CASTLE_WARS to 5128,
            CASTLE_WARS to CRAFT_GUILD to 5129,
            VARROCK to CASTLE_WARS to 5130,
            CASTLE_WARS to VARROCK to 5131,
            GRAND_TREE to CASTLE_WARS to 5132,
            CASTLE_WARS to GRAND_TREE to 5133,
            GRAND_TREE to CRAFT_GUILD to 5134,
            CRAFT_GUILD to GRAND_TREE to 5135,
            TAVERLEY to GRAND_TREE to 5136,
            GRAND_TREE to TAVERLEY to 5137,
            VARROCK to GRAND_TREE to 5138,
            GRAND_TREE to VARROCK to 5139
        )

        /**
         * Gets the animation id for travel.
         */
        fun getAnimationId(from: BalloonDefinition, to: BalloonDefinition): Int {
            return animations[from to to].takeIf { it != 0 }
                ?: error("No animation for route [$from] -> [$to]")
        }
    }
}