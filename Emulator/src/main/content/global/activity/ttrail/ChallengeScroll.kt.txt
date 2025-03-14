package content.global.activity.ttrail

import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

open class ChallengeClue : ChallengeClueScroll {
    constructor() : super(null, -1, null, null, null, null)

    constructor(name: String, clueId: Int, level: ClueLevel, question: String, npc: Int, answer: Int) : super(
        name,
        clueId,
        level,
        question,
        npc,
        answer,
    )

    override fun newInstance(arg: Any?): Plugin<Any?>? {
        register(
            ChallengeClue(
                "zoo_keeper",
                Items.CLUE_SCROLL_7268,
                ClueLevel.HARD,
                "How many animals are in the Ardougne Zoo?",
                NPCs.ZOO_KEEPER_28,
                40,
            ),
        )
        register(
            ChallengeClue(
                "luthas",
                Items.CLUE_SCROLL_7270,
                ClueLevel.HARD,
                "How many banana trees are there in the plantation?",
                NPCs.LUTHAS_379,
                33,
            ),
        )
        register(
            ChallengeClue(
                "king_roald",
                Items.CLUE_SCROLL_7272,
                ClueLevel.MEDIUM,
                "How many bookcases are in the palace library?",
                NPCs.KING_ROALD_648,
                24,
            ),
        )
        register(
            ChallengeClue(
                "gabooty",
                Items.CLUE_SCROLL_7274,
                ClueLevel.MEDIUM,
                "How many buildings in the village?",
                NPCs.GABOOTY_2520,
                11,
            ),
        )
        register(
            ChallengeClue(
                "cook",
                Items.CLUE_SCROLL_7276,
                ClueLevel.MEDIUM,
                "How many cannons does Lumbridge castle have?",
                NPCs.COOK_278,
                9,
            ),
        )
        register(
            ChallengeClue(
                "caroline",
                Items.CLUE_SCROLL_7278,
                ClueLevel.MEDIUM,
                "How many fishermen on the fishing platform?",
                NPCs.CAROLINE_696,
                11,
            ),
        )
        register(
            ChallengeClue(
                "bolkoy",
                Items.CLUE_SCROLL_7280,
                ClueLevel.HARD,
                "How many flowers are in the clearing below this platform?",
                NPCs.BOLKOY_471,
                13,
            ),
        )
        register(
            ChallengeClue(
                "gnome_coach",
                Items.CLUE_SCROLL_7282,
                ClueLevel.HARD,
                "How many gnomes on the gnome ball field have red patches on their uniforms?",
                NPCs.GNOME_COACH_2802,
                11,
            ),
        )
        register(
            ChallengeClue(
                "recruiter",
                Items.CLUE_SCROLL_7284,
                ClueLevel.MEDIUM,
                "How many houses have a cross on the door?",
                NPCs.RECRUITER_720,
                20,
            ),
        )
        register(
            ChallengeClue(
                "brundt_the_chief",
                Items.CLUE_SCROLL_7286,
                ClueLevel.MEDIUM,
                "How many people are waiting for the next bard to perform?",
                NPCs.BRUNDT_THE_CHIEFTAIN_1294,
                4,
            ),
        )
        register(
            ChallengeClue(
                "edmond",
                Items.CLUE_SCROLL_7288,
                ClueLevel.MEDIUM,
                "How many pigeon cages are there around the back of Jerico's house?",
                NPCs.EDMOND_714,
                3,
            ),
        )
        register(
            ChallengeClue(
                "oracle",
                Items.CLUE_SCROLL_7290,
                ClueLevel.MEDIUM,
                "If x is 15 and y is 3, what is 3x + y?",
                NPCs.ORACLE_746,
                48,
            ),
        )
        register(
            ChallengeClue(
                "karim",
                Items.CLUE_SCROLL_7292,
                ClueLevel.MEDIUM,
                "I have 16 kebabs, I eat one myself and share the rest equally between 3 friends. How many do they have each?",
                NPCs.KARIM_543,
                5,
            ),
        )
        register(
            ChallengeClue(
                "hazelmere",
                Items.CLUE_SCROLL_7294,
                ClueLevel.MEDIUM,
                "What is 19 to the power of 3?",
                NPCs.HAZELMERE_669,
                6859,
            ),
        )
        register(
            ChallengeClue(
                "gnome_ball_referee",
                Items.CLUE_SCROLL_7296,
                ClueLevel.MEDIUM,
                "What is 57x89 +23?",
                NPCs.GNOME_BALL_REFEREE_635,
                5096,
            ),
        )
        return this
    }
}
