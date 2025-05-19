package content.global.activity.ttrail.clues

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.scrolls.AnagramClueScroll
import content.global.activity.ttrail.puzzle.PuzzleBox
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the anagram clues.
 */
open class AnagramClue : AnagramClueScroll {

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
     * @return this instance of [AnagramClue]
     */
    override fun newInstance(arg: Any?): Plugin<Any> {
        register(AnagramClue("a_bas", Items.CLUE_SCROLL_7236, "A BAS", NPCs.SABA_1070, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("a_zen_she", Items.CLUE_SCROLL_7238, "A ZEN SHE", NPCs.ZENESHA_589, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("ace_match_elm", Items.CLUE_SCROLL_7239, "ACE MATCH ELM", NPCs.CAM_THE_CAMEL_2812, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("aha_jar", Items.CLUE_SCROLL_7241, "AHA JAR", NPCs.JARAAH_962, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("an_paint_tonic", Items.CLUE_SCROLL_7243, "AN PAINT TONIC", NPCs.CAPTAIN_NINTO_4594, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("arc_o_line", Items.CLUE_SCROLL_7245, "ARC O LINE", NPCs.CAROLINE_696, ClueLevel.MEDIUM, Items.CLUE_SCROLL_7309))
        register(AnagramClue("are_col", Items.CLUE_SCROLL_7247, "ARE COL", NPCs.ORACLE_746, ClueLevel.MEDIUM, Items.CLUE_SCROLL_10182))
        register(AnagramClue("arr_so_i_am_a_crust", Items.CLUE_SCROLL_7248, "ARR! SO I AM A CRUST, AND?", NPCs.RAMARA_DU_CROISSANT_3827, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("bail_trims", Items.CLUE_SCROLL_7249, "BAIL TRIMS", NPCs.BRIMSTAIL_171, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("by_look", Items.CLUE_SCROLL_7250, "BY LOOK", NPCs.BOLKOY_471, ClueLevel.MEDIUM, Items.CLUE_SCROLL_7311))
        register(AnagramClue("c_on_game_hoc", Items.CLUE_SCROLL_7251, "C ON GAME HOC", NPCs.GNOME_COACH_2802, ClueLevel.MEDIUM, Items.CLUE_SCROLL_7313))
        register(AnagramClue("dt_run_b", Items.CLUE_SCROLL_7252, "DT RUN B", NPCs.BRUNDT_THE_CHIEFTAIN_1294, ClueLevel.MEDIUM, Items.CLUE_SCROLL_7317))
        register(AnagramClue("eek_zero_op", Items.CLUE_SCROLL_7253, "EEK ZERO OP", NPCs.ZOO_KEEPER_28, ClueLevel.MEDIUM, Items.CLUE_SCROLL_7301))
        register(AnagramClue("el_ow", Items.CLUE_SCROLL_7254, "EL OW", NPCs.LOWE_550, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("err_cure_it", Items.CLUE_SCROLL_7255, "ERR CURE IT", NPCs.RECRUITER_720, ClueLevel.MEDIUM, Items.CLUE_SCROLL_7315))
        register(AnagramClue("goblin_kern", Items.CLUE_SCROLL_7256, "GOBLIN KERN", NPCs.KING_BOLREN_469, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("got_a_boy", Items.CLUE_SCROLL_7258, "GOT A BOY", NPCs.GABOOTY_2520, ClueLevel.MEDIUM, Items.CLUE_SCROLL_7305))
        register(AnagramClue("gulag_run", Items.CLUE_SCROLL_7260, "GULAG RUN", NPCs.UGLUG_NAR_2039, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("halt_us", Items.CLUE_SCROLL_7262, "HALT US", NPCs.LUTHAS_379, ClueLevel.MEDIUM, Items.CLUE_SCROLL_7303))
        register(AnagramClue("he_do_pose", Items.CLUE_SCROLL_7264, "HE DO POSE. IT IS CULTRRL, MK?", NPCs.RIKI_THE_SCULPTORS_MODEL_2143, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("icy_fe", Items.CLUE_SCROLL_7266, "ICY FE", NPCs.FYCIE_1011, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("i_eat_its_chart_hints_do_u", Items.CLUE_SCROLL_7268, "I EAT ITS CHART HINTS DO U", NPCs.SHIRATTI_THE_CUSTODIAN_3044, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("i_faffy_run", Items.CLUE_SCROLL_7270, "I FAFFY RUN", NPCs.FAIRY_NUFF_3303, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("land_doomd", Items.CLUE_SCROLL_7272, "LAND DOOMD", NPCs.ODD_OLD_MAN_3670, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("lark_in_dog", Items.CLUE_SCROLL_7274, "LARK IN DOG", NPCs.KING_ROALD_648, ClueLevel.MEDIUM, Items.CLUE_SCROLL_7304))
        register(AnagramClue("me_am_the_calc", Items.CLUE_SCROLL_7276, "ME AM THE CALC", NPCs.CAM_THE_CAMEL_2813, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("me_if", Items.CLUE_SCROLL_7278, "ME IF", NPCs.FEMI_676, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("nod_med", Items.CLUE_SCROLL_7280, "NOD MED", NPCs.EDMOND_714, ClueLevel.MEDIUM, Items.CLUE_SCROLL_10180))
        register(AnagramClue("o_birdz_a_zany_en_pc", Items.CLUE_SCROLL_7282, "O BIRDZ A ZANY EN PC", NPCs.CAPN_IZZY_NO_BEARD_437, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("ok_co", Items.CLUE_SCROLL_7284, "OK CO", NPCs.COOK_278, ClueLevel.MEDIUM, Items.CLUE_SCROLL_7307))
        register(AnagramClue("or_zinc_fumes_ward", Items.CLUE_SCROLL_7286, "OR ZINC FUMES WARD", NPCs.WIZARD_CROMPERTY_2328, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("peaty_pert", Items.CLUE_SCROLL_7288, "PEATY PERT", NPCs.PARTY_PETE_659, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("profs_lose_wrong_pie", Items.CLUE_SCROLL_7290, "PROFS LOSE WRONG PIE", NPCs.PROFESSOR_ODDENSTEIN_286, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("r_ak_mi", Items.CLUE_SCROLL_7292, "R AK MI", NPCs.KARIM_543, ClueLevel.MEDIUM, Items.CLUE_SCROLL_10184))
        register(AnagramClue("red_art_tans", Items.CLUE_SCROLL_7294, "RED ART TANS", NPCs.TRADER_STAN_4650, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("sequin_dirge", Items.CLUE_SCROLL_7296, "SEQUIN DIRGE", NPCs.QUEEN_SIGRID_1359, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("snah", Items.CLUE_SCROLL_7298, "SNAH", NPCs.HANS_0, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        register(AnagramClue("them_call_came", Items.CLUE_SCROLL_7300, "THEM CALL CAME", NPCs.CAM_THE_CAMEL_2813, ClueLevel.HARD, PuzzleBox.getRandomPuzzleBox()))
        return this
    }
}