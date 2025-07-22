package content.global.activity.ttrail.plugin

import content.global.activity.ttrail.ClueLevel
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the anagram clues.
 * @author szu
 */
open class Anagram : AnagramScroll {

    /**
     * Default constructor.
     */
    constructor() : super(null, -1, null, -1, null, null)

    /**
     * Constructs an AnagramClue with specified properties.
     *
     * @param name      The unique name identifier for the clue.
     * @param clueId    The item id of the clue scroll.
     * @param anagram   The scrambled clue phrase players must solve.
     * @param npcId     The NPC id associated with the clue.
     * @param level     The difficulty level of the clue.
     * @param challenge Optional challenge identifier linked to the clue.
     */
    constructor(
        name: String,
        clueId: Int,
        anagram: String,
        npcId: Int,
        level: ClueLevel,
        challenge: Int?
    ) : super(name, clueId, anagram, npcId, level, challenge)

    /**
     * Registers all available Anagram clues.
     *
     * @param arg optional argument, not used
     * @return this instance of [Anagram]
     */
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        register(Anagram("a_bas", Items.CLUE_SCROLL_7236, "A BAS", NPCs.SABA_1070, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("a_zen_she", Items.CLUE_SCROLL_7238, "A ZEN SHE", NPCs.ZENESHA_589, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("ace_match_elm", Items.CLUE_SCROLL_7239, "ACE MATCH ELM", NPCs.CAM_THE_CAMEL_2812, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("aha_jar", Items.CLUE_SCROLL_7241, "AHA JAR", NPCs.JARAAH_962, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("an_paint_tonic", Items.CLUE_SCROLL_7243, "AN PAINT TONIC", NPCs.CAPTAIN_NINTO_4594, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("arc_o_line", Items.CLUE_SCROLL_7245, "ARC O LINE", NPCs.CAROLINE_696, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_2854))
        register(Anagram("are_col", Items.CLUE_SCROLL_7247, "ARE COL", NPCs.ORACLE_746, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_7279))
        register(Anagram("arr_so_i_am_a_crust", Items.CLUE_SCROLL_7248, "ARR! SO I AM A CRUST, AND?", NPCs.RAMARA_DU_CROISSANT_3827, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("bail_trims", Items.CLUE_SCROLL_7249, "BAIL TRIMS", NPCs.BRIMSTAIL_171, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("by_look", Items.CLUE_SCROLL_7250, "BY LOOK", NPCs.BOLKOY_471, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_7269))
        register(Anagram("c_on_game_hoc", Items.CLUE_SCROLL_7251, "C ON GAME HOC", NPCs.GNOME_COACH_2802, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_7271))
        register(Anagram("dt_run_b", Items.CLUE_SCROLL_7252, "DT RUN B", NPCs.BRUNDT_THE_CHIEFTAIN_1294, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_7275))
        register(Anagram("eek_zero_op", Items.CLUE_SCROLL_7253, "EEK ZERO OP", NPCs.ZOO_KEEPER_28, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_2842))
        register(Anagram("el_ow", Items.CLUE_SCROLL_7254, "EL OW", NPCs.LOWE_550, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("err_cure_it", Items.CLUE_SCROLL_7255, "ERR CURE IT", NPCs.RECRUITER_720, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_7273))
        register(Anagram("goblin_kern", Items.CLUE_SCROLL_7256, "GOBLIN KERN", NPCs.KING_BOLREN_469, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("got_a_boy", Items.CLUE_SCROLL_7258, "GOT A BOY", NPCs.GABOOTY_2520, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_2850))
        register(Anagram("gulag_run", Items.CLUE_SCROLL_7260, "GULAG RUN", NPCs.UGLUG_NAR_2039, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("halt_us", Items.CLUE_SCROLL_7262, "HALT US", NPCs.LUTHAS_379, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_2844))
        register(Anagram("he_do_pose", Items.CLUE_SCROLL_7264, "HE DO POSE. IT IS CULTRRL, MK?", NPCs.RIKI_THE_SCULPTORS_MODEL_2143, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("icy_fe", Items.CLUE_SCROLL_7266, "ICY FE", NPCs.FYCIE_1011, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("i_eat_its_chart_hints_do_u", Items.CLUE_SCROLL_7268, "I EAT ITS CHART HINTS DO U", NPCs.SHIRATTI_THE_CUSTODIAN_3044, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("i_faffy_run", Items.CLUE_SCROLL_7270, "I FAFFY RUN", NPCs.FAIRY_NUFF_3303, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("land_doomd", Items.CLUE_SCROLL_7272, "LAND DOOMD", NPCs.ODD_OLD_MAN_3670, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("lark_in_dog", Items.CLUE_SCROLL_7274, "LARK IN DOG", NPCs.KING_ROALD_648, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_2846))
        register(Anagram("me_am_the_calc", Items.CLUE_SCROLL_7276, "ME AM THE CALC", NPCs.CAM_THE_CAMEL_2813, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("me_if", Items.CLUE_SCROLL_7278, "ME IF", NPCs.FEMI_676, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("nod_med", Items.CLUE_SCROLL_7280, "NOD MED", NPCs.EDMOND_714, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_7277))
        register(Anagram("o_birdz_a_zany_en_pc", Items.CLUE_SCROLL_7282, "O BIRDZ A ZANY EN PC", NPCs.CAPN_IZZY_NO_BEARD_437, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("ok_co", Items.CLUE_SCROLL_7284, "OK CO", NPCs.COOK_278, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_2852))
        register(Anagram("or_zinc_fumes_ward", Items.CLUE_SCROLL_7286, "OR ZINC FUMES WARD", NPCs.WIZARD_CROMPERTY_2328, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("peaty_pert", Items.CLUE_SCROLL_7288, "PEATY PERT", NPCs.PARTY_PETE_659, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("profs_lose_wrong_pie", Items.CLUE_SCROLL_7290, "PROFS LOSE WRONG PIE", NPCs.PROFESSOR_ODDENSTEIN_286, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("r_ak_mi", Items.CLUE_SCROLL_7292, "R AK MI", NPCs.KARIM_543, ClueLevel.MEDIUM, Items.CHALLENGE_SCROLL_7281))
        register(Anagram("red_art_tans", Items.CLUE_SCROLL_7294, "RED ART TANS", NPCs.TRADER_STAN_4650, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("sequin_dirge", Items.CLUE_SCROLL_7296, "SEQUIN DIRGE", NPCs.QUEEN_SIGRID_1359, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("snah", Items.CLUE_SCROLL_7298, "SNAH", NPCs.HANS_0, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(Anagram("them_call_came", Items.CLUE_SCROLL_7300, "THEM CALL CAME", NPCs.CAM_THE_CAMEL_2813, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        return this
    }
}