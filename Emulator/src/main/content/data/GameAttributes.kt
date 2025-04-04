package content.data

/**
 * The main and most important attributes for various interactions,
 * quests and also other categories.
 */
internal object GameAttributes {
    const val SAVE = "/save:"
    const val JOIN_DATE = "joinDate"

    // Tutorial island.
    const val TUTORIAL_COMPLETE = "tutorial:complete"
    const val TUTORIAL_STAGE = "tutorial:stage"

    // Knight's waves activity.
    const val PRAYER_LOCK = "/save:prayer:lock"

    // Construction skill.
    const val CON = "con"
    const val PORTAL = "portal"
    const val CON_HOTSPOT = "con:hotspot"
    const val POH_PORTAL = "construction:portal"
    const val CON_OBJ = "construction:objs"
    const val CON_CLOCKMAKER_DIAL = "construction:clockbench"
    const val CON_PORTAL_DIR = "construction:direct_portal"
    const val CON_REMOVE_DECO = "construction:remove_decoration"
    const val CON_TOOLS = "construction:tools"
    const val CON_ROOM = "construction:room"
    const val CON_REMOVE = "construction:remove_dialogue"
    const val CON_LAST_TASK = "construction:servant_last_task"
    const val CON_TASK = "construction:servant_task"
    const val CON_FLATPACK_TIER = "construction:flatpack:tier"

    const val FAMILY_CREST = "/save:construction:family_crest"

    // Basic random event attributes.
    const val RE = "random"
    const val RE_PAUSE = "random:pause"
    const val RE_REWARD = "random:reward"

    // Prison pete random event.
    const val RE_PRISON_1 = "/save:random:prison_pete_start"
    const val RE_PRISON_2 = "/save:random:prison_pete_index"
    const val RE_PRISON_3 = "/save:random:prison_pete_interactions"
    const val RE_PRISON_4 = "/save:random:prison_pete_wrong"

    // Pinball random event.
    const val RE_PINBALL_START = "/save:random:pinball_start"
    const val RE_PINBALL_INTER = "/save:random:pinball_interactions"
    const val RE_PINBALL_OBJ = "/save:random:pinball:objs"

    // Pattern recognition random event.
    const val RE_PATTERN_INDEX = "random:pattern_index"
    const val RE_PATTERN_CORRECT = "random:pattern_correct"
    const val RE_PATTERN_OBJ = "random:pattern:objs"

    // Freaky forester random event.
    const val RE_FREAK_TASK = "/save:random:freaky_forester:task"
    const val RE_FREAK_COMPLETE = "/save:random:freaky_forester_complete"
    const val RE_FREAK_KILLS = "/save:random:freaky_forester:kills"

    // Pillory random event.
    const val RE_PILLORY_KEYS = "/save:random:pillory_item_1"
    const val RE_PILLORY_PADLOCK = "/save:random:pillory_item_2"
    const val RE_PILLORY_CORRECT = "/save:random:pillory_correct"
    const val RE_PILLORY_TARGET = "/save:random:pillory_target"
    const val RE_PILLORY_SCORE = "/save:random:pillory_score"

    // Evil twin random event.
    const val RE_TWIN_START = "/save:random:5:start"
    const val RE_TWIN_DIAL = "/save:random:evil_twin_dialogue"
    const val RE_TWIN_OBJ_DIAL = "/save:random:evil_twin:objs_dialogue"
    const val RE_TWIN_OBJ_LOC_X = "/save:random:evil_twin_loc:x"
    const val RE_TWIN_OBJ_LOC_Y = "/save:random:evil_twin_loc:y"

    // Evil bob random event.
    const val RE_BOB_START = "/save:random:evil_bob_start"
    const val RE_BOB_COMPLETE = "/save:random:evil_bob_complete"
    const val RE_BOB_ZONE = "/save:random:evil_bob_zone"
    const val RE_BOB_SCORE = "/save:random:evil_bob_score"
    const val RE_BOB_ALERT = "/save:random:evil_bob_temporary"
    const val RE_BOB_DIAL = "/save:random:evil_bob_dialogue"
    const val RE_BOB_DIAL_INDEX = "/save:random:evil_bob_index"
    const val RE_BOB_OBJ = "/save:random:evil_bob:objs"

    // Mime random event.
    const val RE_MIME_EMOTE = "/save:random:mime_emote"
    const val RE_MIME_INDEX = "/save:random:mime_index"
    const val RE_MIME_CORRECT = "/save:random:mime_correct"
    const val RE_MIME_WRONG = "/save:random:mime_wrong"

    // Quiz master random event.
    const val RE_QUIZ_REWARD = "/save:quiz:random_reward"
    const val RE_QUIZ_SCORE = "/save:quiz:score"

    // Sandwich lady random event.
    const val S_LADY_ITEM = "random:sandwich_lady:item"
    const val S_LADY_ITEM_VALUE = "random:sandwich_lady:item_value"

    // Plant random event.
    const val PLANT_NPC = "/save:random:plant"
    const val PLANT_NPC_VALUE = "/save:random:fruits"

    // Certer random event.
    const val CERTER_REWARD = "random:certer_reward"
    const val CERTER_CORRECT = "random:certer_stage"
    const val CERTER_INDEX = "random:certer_index"

    // Drill demon random event.
    const val DRILL_OFFSET = "/save:random:drill_offset"
    const val DRILL_TASK = "/save:random:drill_task"
    const val DRILL_COUNTER = "/save:random:drill_score"

    // Swept away continuation (mini-quest)
    const val MINI_PURPLE_CAT = "/save:mini-quest:purple-cat"
    const val MINI_PURPLE_CAT_COMPLETE = "/save:mini-quest:purple-cat:complete"
    const val MINI_PURPLE_CAT_PROGRESS = "/save:mini-quest:purple-cat:score"

    // Swept away quest.
    const val QUEST_SWEPT_AWAY = "quest:swept-away"
    const val QUEST_SWEPT_AWAY_HETTY_ENCH = "/save:quest:swept-away:hetty-enchantment"
    const val QUEST_SWEPT_AWAY_BETTY_ENCH = "/save:quest:swept-away:betty-enchantment"
    const val QUEST_SWEPT_AWAY_LINE = "/save:quest:sweep-away:line"
    const val QUEST_SWEPT_AWAY_BETTY_WAND = "quest:swept-away:item"
    const val QUEST_SWEPT_AWAY_CREATURE_INTER = "/save:quest:sweep-away:creature_interactions"
    const val QUEST_SWEPT_AWAY_LABELS = "/save:quest:swept-away:labels"
    const val QUEST_SWEPT_AWAY_LOTTIE = "/save:quest:sweep-away:lottie-puzzle"
    const val QUEST_SWEPT_AWAY_LABELS_COMPLETE = "/save:quest:swept-away:labels:complete"

    const val TALK_ABOUT_SQ_IRKJUICE = "/save:broomstick:osman-talk"
    const val BROOM_ENCHANTMENT_TP = "/save:broomstick:teleport"

    // Phoenix Lair quest.
    const val PHOENIX_LAIR_VISITED = "/save:location:phoenix-lair:visit"
    const val TALK_WITH_PRIEST = "/save:quest:phoenix:talk-with-priest"

    // Tears of Guthix quest.
    const val QUEST_TOG_LAST_DATE = "/save:quest:tog:last_date"
    const val QUEST_TOG_LAST_XP_AMOUNT = "/save:quest:tog:xp_amount"
    const val QUEST_TOG_LAST_QP = "/save:quest:tog:last_qp"

    // Temple of ikov quest.
    const val QUEST_IKOV_SELECTED_END = "/save:quest:ikov:end"
    const val QUEST_IKOV_DISABLED_TRAP = "/save:quest:ikov:disabled_trap"
    const val QUEST_IKOV_WINELDA_INTER = "/save:quest:ikov:winelda_interactions"
    const val QUEST_IKOV_BRIDGE_INTER = "/save:quest:ikov:bridge_interactions"
    const val QUEST_IKOV_ICE_CHAMBER_ACCESS = "/save:quest:ikov:ice_chamber_access"
    const val QUEST_IKOV_ICE_ARROWS = "/save:quest:ikov:ice_arrows"
    const val QUEST_IKOV_CHEST_INTER = "quest:ikov:chest_interactions"
    const val QUEST_IKOV_WARRIOR_INST = "quest:ikov:warrior_instance"

    // Horror from the deep quest.
    const val QUEST_HFTD_LIGHTHOUSE_MECHANISM = "hftd:lighthouse-fixed"
    const val QUEST_HFTD_UNLOCK_BRIDGE = "/save:hftd:lighthouse-bridge"
    const val QUEST_HFTD_STRANGE_WALL_DISCOVER = "/save:hftd:strange-wall"
    const val QUEST_HFTD_UNLOCK_DOOR = "/save:hftd:item-placed"

    const val GOD_BOOKS = "/save:god_books:access"

    const val QUEST_HFTD_USE_AIR_RUNE = "/save:hftd:air"
    const val QUEST_HFTD_USE_FIRE_RUNE = "/save:hftd:fire"
    const val QUEST_HFTD_USE_EARTH_RUNE = "/save:hftd:earth"
    const val QUEST_HFTD_USE_WATER_RUNE = "/save:hftd:water"
    const val QUEST_HFTD_USE_ARROW = "/save:hftd:arrow"
    const val QUEST_HFTD_USE_SWORD = "/save:hftd:sword"

    // Black knights fortress quest.
    const val QUEST_BKF_DOSSIER_INTER = "/save:quest:fortress:read_dossier"

    // Wolf Whistle quest.
    const val WOLF_WHISTLE_STIKKLEBRIX = "/save:quest:wolf:searched-body"

    // Penguins: Hide & Seek activity.
    const val ACTIVITY_PENGUINS_HNS = "/save:phns:spy-on:enabled"
    const val ACTIVITY_PENGUINS_HNS_SCORE = "/save:phns:points"

    // Animal Magnetism quest.
    const val ITEM_AVA_DEVICE = "item:ava-device:burping"

    // Witch house quest.
    const val ITEM_TOY_MOUSE_RELEASE = "item:released:toy-mouse"

    // RuneCrafting guild.
    const val RC_GUILD_TALISMAN = "/save:rcguild:shown_talisman"
    const val RC_GUILD_TALISMAN_TASK_START = "/save:rcguild:talisman_task"
    const val RC_GUILD_TALISMAN_TASK_COMPLETE = "/save:rcguild:omni-access"

    // The lost tribe quest.
    const val QUEST_LOST_TRIBE_LUMBRIDGE_CELLAR = "/save:tlt-hole-cleared"

    // Player safety training centre access.
    const val PLAYER_SAFETY_TRAINING_CENTRE_ACCESS = "/save:training-centre-access-unlocked"

    // Agile top
    const val BARBARIAN_OUTPOST_GUNNJORN_TALK = "/save:gunnjorn:first-talk"
    const val BARBARIAN_OUTPOST_COURSE_LAPS = "/save:barbarian_outpost:obstacle_count"
    const val BARBARIAN_OUTPOST_PERFECT_LAPS = "/save:barbarian_outpost:perfect_laps_counter"
    const val BARBARIAN_OUTPOST_LAST_OBSTACLE = "barbarian-outpost-obstacle"
    const val BARBARIAN_OUTPOST_COURSE_REWARD = "/save:barbarian_course_reward:unlocked"

    // Agile legs
    const val GNOME_STRONGHOLD_GNOME_TALK = "/save:gnome:first-talk"
    const val GNOME_STRONGHOLD_COURSE_LAPS = "/save:gnome_stronghold:obstacle_count"
    const val GNOME_STRONGHOLD_PERFECT_LAPS = "/save:gnome_stronghold:perfect_laps_counter"
    const val GNOME_STRONGHOLD_LAST_OBSTACLE = "gnome-stronghold-obstacle"
    const val GNOME_STRONGHOLD_COURSE_REWARD = "/save:gnome_stronghold_reward:unlocked"

    // Holy Grail quest.
    const val BLACK_KNIGHT_TITAN = "/save:failed_to_kill_titan"

    // Merlin Crystal quest.
    const val TEMP_ATTR_MORGAN = "morgan"
    const val TEMP_ATTR_BEGGAR = "beggar"
    const val TEMP_ATTR_THRANTAX = "thrantax"
    const val TEMP_ATTR_THRANTAX_OWNER = "thrantax_owner"
    const val TEMP_ATTR_LADY = "temp_lady"
    const val TEMP_ATTR_MERLIN = "temp_merlin"

    const val ATTR_STATE_ALTAR_FINISH = "/save:finished_altar"
    const val ATTR_STATE_CLAIM_EXCALIBUR = "/save:received_excalibur"
    const val ATTR_STATE_TALK_LADY = "/save:mc_talked_lady_lake"
    const val ATTR_STATE_TALK_BEGGAR = "/save:mc_talked_beggar"
    const val ATTR_STATE_TALK_CANDLE = "/save:mc_talked_candle_maker"

    // Sea slug quest.
    const val QUEST_SEA_SLUG_TALK_WITH_KENT = "/save:seaslug:kent-dialogue"

    // Achievements alternative teleportation options.

    const val ATTRIBUTE_VARROCK_ALT_TELE = "/save:diaries:varrock:alttele"
    const val ATTRIBUTE_CAMELOT_ALT_TELE = "/save:diaries:camelot:alttele"
}
