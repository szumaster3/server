package content.global.skill.construction.storage.box

import org.rs.consts.Items

enum class BookcaseItem(val takeId: Int,) {
    ArenaBook(Items.ARENA_BOOK_6891),
    MyNotes(Items.MY_NOTES_11339),
    CrumblingTome(Items.CRUMBLING_TOME_4707),
    PieRecipeBook(Items.PIE_RECIPE_BOOK_7162),
    GiannesCookBook(Items.GIANNES_COOK_BOOK_2167),
    GameBook(Items.GAME_BOOK_7681),
    StrongholdNotes(Items.STRONGHOLD_NOTES_9004),
    CocktailGuide(Items.COCKTAIL_GUIDE_2023),
    TarnsDiary(Items.TARNS_DIARY_10587),
    ConstructionGuide(Items.CONSTRUCTION_GUIDE_8463),
    GlassblowingBook(Items.GLASSBLOWING_BOOK_11656),
    BrewinGuide(Items.BREWIN_GUIDE_8989),
    SecurityBook(Items.SECURITY_BOOK_9003),
    QueenHelpBook(Items.QUEEN_HELP_BOOK_10562),
    AbyssalBook(Items.ABYSSAL_BOOK_5520),
    ExplorersNotes(Items.EXPLORERS_NOTES_11677),
    GoblinBook(Items.GOBLIN_BOOK_10999),
    DwarvenLore(Items.DWARVEN_LORE_4568),
    BookOPiracy(Items.BOOK_O_PIRACY_7144),
    ClockworkBook(Items.CLOCKWORK_BOOK_10594),
    ScabariteNotes(Items.SCABARITE_NOTES_11975),
    Translation(Items.TRANSLATION_4655),
    BookOnChemicals(Items.BOOK_ON_CHEMICALS_711),
    InstructionManual(Items.INSTRUCTION_MANUAL_5),
    BirdBook(Items.BIRD_BOOK_10173),
    FeatheredJournal(Items.FEATHERED_JOURNAL_10179),
    BatteredBook(Items.BATTERED_BOOK_2886),
    BeatenBook(Items.BEATEN_BOOK_9717),
    AHandwrittenBook(Items.A_HANDWRITTEN_BOOK_9627),
    VarmensNotes(Items.VARMENS_NOTES_4616);

    val labelId: Int get() = 56 + (ordinal * 2)
}
