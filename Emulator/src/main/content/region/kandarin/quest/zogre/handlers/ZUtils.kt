package content.region.kandarin.quest.zogre.handlers

import org.rs.consts.Items

object ZUtils {
    const val UNREALIST_PORTRAIT = Items.SITHIK_PORTRAIT_4814
    const val REALIST_PORTRAIT = Items.SITHIK_PORTRAIT_4815
    const val SIGNED_PORTRAIT = Items.SIGNED_PORTRAIT_4816
    const val HAM_BOOK = Items.BOOK_OF_HAM_4829
    const val NECROMANCY_BOOK = Items.NECROMANCY_BOOK_4837
    const val PORTRAI_BOOK = Items.BOOK_OF_PORTRAITURE_4817
    const val STRANGE_POTION = Items.STRANGE_POTION_4836
    val QUEST_ITEMS =
        intArrayOf(
            Items.BOOK_OF_PORTRAITURE_4817,
            Items.BOOK_OF_HAM_4829,
            Items.NECROMANCY_BOOK_4837,
            Items.TORN_PAGE_4809,
            Items.BLACK_PRISM_4808,
            Items.DRAGON_INN_TANKARD_4811,
            Items.PAPYRUS_970,
            Items.SITHIK_PORTRAIT_4814,
            Items.SITHIK_PORTRAIT_4815,
            Items.SIGNED_PORTRAIT_4816,
            Items.STRANGE_POTION_4836,
        )

    val CHARRED_AREA = "zfe:charred-area-visited"
    val TORN_PAGE_ACQUIRED = "zfe:torn-page-acquired"
    val BLACK_PRISM_ACQUIRED = "zfe:black-prism-acquired"
    val DRAGON_TANKARD_ACQUIRED = "zfe:dragon-tankard-acquired"
    val ASK_SITHIK_ABOUT_OGRES = "zfe:sithik-ask-about-ogres"
    val ASK_SITHIK_AGAIN = "zfe:sithik-ask-sithik-again"
    val SITHIK_DIALOGUE_UNLOCK = "zfe:sithik-stage-unlocked"
    val SITHIK_TURN_INTO_OGRE = "zfe:sithik-transformation"
    val TORN_PAGE_ON_NECRO_BOOK = "zfe:missed-page"
    val TALK_ABOUT_NECRO_BOOK = "zfe:talk:0"
    val TALK_ABOUT_BLACK_PRISM = "zfe:talk:1"
    val TALK_ABOUT_TORN_PAGE = "zfe:talk:2"
    val TALK_ABOUT_TANKARD = "zfe:talk:3"
    val TALK_ABOUT_TANKARD_AGAIN = "zfe:talk:4"
    val TALK_ABOUT_SIGN_PORTRAIT = "zfe:talk:5"
    val TALK_AGAIN_ABOUT_HAM_BOOK = "zfe:talk:6"
    val TALK_AGAIN_1 = "zfe:talk:7"
    val TALK_AGAIN_2 = "zfe:talk:8"
    val TALK_AGAIN_3 = "zfe:talk:9"
    val TALK_AGAIN_4 = "zfe:talk:10"
    val TALK_AGAIN_5 = "zfe:talk:11"
    val TALK_AGAIN_6 = "zfe:talk:12"
    val TALK_WITH_SITHIK_OGRE_DONE = "zfe:sithik-stage-complete"
    val TALK_WITH_ZAVISTIC_DONE = "zfe:zavistic-stage-complete"
    val RECEIVED_KEY_FROM_GRISH = "zfe:gate-key-received"
    val NPC_ACTIVE = "zfe:zavistic-spawned"
    val SLASH_BASH_ACTIVE = "zfe:boss-spawned"
    val ZOMBIE_NPC_ACTIVE = "zfe:zombie-activated"
    val ZOMBIE_NPC_SPAWN = "zfe:zombie-spawned"
}
