package content.global.activity.ttrail.plugin

import content.global.activity.ttrail.ClueLevel
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the challenge scroll clues.
 * @author szu
 */
open class ChallengeScroll : ChallengeClueScroll {

    constructor() : super(null, -1, null, null, null, null)

    /**
     * Constructs a challenge clue scroll.
     *
     * @param name      the internal name of the clue
     * @param clueId    the item ID representing the clue scroll
     * @param level     the difficulty level of the clue
     * @param question  the question text the player must answer
     * @param npc       the NPC associated with this clue
     * @param answer    the correct answer to the challenge
     */
    constructor(name: String, clueId: Int, level: ClueLevel, question: String, npc: Int, answer: Int) : super(
        name,
        clueId,
        level,
        question,
        npc,
        answer,
    )

    /**
     * Register the challenge clues.
     */
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        register(ChallengeScroll("zoo_keeper", Items.CHALLENGE_SCROLL_2842, ClueLevel.HARD, "How many animals<br>are in the Ardougne Zoo?", NPCs.ZOO_KEEPER_28, 40))
        register(ChallengeScroll("luthas", Items.CHALLENGE_SCROLL_2844, ClueLevel.HARD, "How many banana trees<br>are there in the plantation?", NPCs.LUTHAS_379, 33))
        register(ChallengeScroll("king_roald", Items.CHALLENGE_SCROLL_2846, ClueLevel.MEDIUM, "How many bookcases<br>are in the palace library?", NPCs.KING_ROALD_648, 24))
        register(ChallengeScroll("gabooty", Items.CHALLENGE_SCROLL_2850, ClueLevel.MEDIUM, "How many buildings<br>in the village?", NPCs.GABOOTY_2520, 11))
        register(ChallengeScroll("cook", Items.CHALLENGE_SCROLL_2852, ClueLevel.MEDIUM, "How many cannons does<br>Lumbridge castle have?", NPCs.COOK_278, 9))
        register(ChallengeScroll("caroline", Items.CHALLENGE_SCROLL_2854, ClueLevel.MEDIUM, "How many fishermen<br>on the fishing platform?", NPCs.CAROLINE_696, 11))
        register(ChallengeScroll("bolkoy", Items.CHALLENGE_SCROLL_7269, ClueLevel.HARD, "How many flowers are<br>in the clearing below<br>this platform?", NPCs.BOLKOY_471, 13))
        register(ChallengeScroll("gnome_coach", Items.CHALLENGE_SCROLL_7271, ClueLevel.HARD, "How many gnomes on the<br>gnome ball field<br>have red patches on<br>their uniforms?", NPCs.GNOME_COACH_2802, 6))
        register(ChallengeScroll("recruiter", Items.CHALLENGE_SCROLL_7273, ClueLevel.MEDIUM, "How many houses have<br>a cross on the door?", NPCs.RECRUITER_720, 20))
        register(ChallengeScroll("brundt_the_chief", Items.CHALLENGE_SCROLL_7275, ClueLevel.MEDIUM, "How many people are<br>waiting for the next<br>bard to perform?", NPCs.BRUNDT_THE_CHIEFTAIN_1294, 4))
        register(ChallengeScroll("edmond", Items.CHALLENGE_SCROLL_7277, ClueLevel.MEDIUM, "How many pigeon cages<br>are there around the back<br>of Jerico's house?", NPCs.EDMOND_714, 3))
        register(ChallengeScroll("oracle", Items.CHALLENGE_SCROLL_7279, ClueLevel.MEDIUM, "If x is 15 and y is 3,<br>what is 3x + y?", NPCs.ORACLE_746, 48))
        register(ChallengeScroll("karim", Items.CHALLENGE_SCROLL_7281, ClueLevel.MEDIUM, "I have 16 kebabs, I eat one myself<br>and share the rest equally between 3 friends.<br>How many do they have each?", NPCs.KARIM_543, 5))
        register(ChallengeScroll("hazelmere", Items.CHALLENGE_SCROLL_7283, ClueLevel.MEDIUM, "What is 19 to<br>the power of 3?", NPCs.HAZELMERE_669, 6859))
        register(ChallengeScroll("gnome_ball_referee", Items.CHALLENGE_SCROLL_7285, ClueLevel.MEDIUM, "What is 57x89 + 23?", NPCs.GNOME_BALL_REFEREE_635, 5096))
        return this
    }
}
