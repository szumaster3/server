package content.region.kandarin.feldip.jiggig.quest.zogre.plugin

import shared.consts.Items

object ZogreUtils {
    const val UNREALIST_PORTRAIT = Items.SITHIK_PORTRAIT_4814
    const val REALIST_PORTRAIT = Items.SITHIK_PORTRAIT_4815
    const val SIGNED_PORTRAIT = Items.SIGNED_PORTRAIT_4816
    const val HAM_BOOK = Items.BOOK_OF_HAM_4829
    const val NECROMANCY_BOOK = Items.NECROMANCY_BOOK_4837
    const val PORTRAI_BOOK = Items.BOOK_OF_PORTRAITURE_4817
    const val STRANGE_POTION = Items.STRANGE_POTION_4836
    val QUEST_ITEMS = intArrayOf(PORTRAI_BOOK, HAM_BOOK, NECROMANCY_BOOK, Items.TORN_PAGE_4809, Items.BLACK_PRISM_4808, Items.DRAGON_INN_TANKARD_4811, Items.PAPYRUS_970, UNREALIST_PORTRAIT, REALIST_PORTRAIT, SIGNED_PORTRAIT, STRANGE_POTION)

    const val CHARRED_AREA = "/save:zfe:charred-area-visited"
    const val TORN_PAGE_ACQUIRED = "/save:zfe:torn-page-acquired"
    const val BLACK_PRISM_ACQUIRED = "/save:zfe:black-prism-acquired"
    const val DRAGON_TANKARD_ACQUIRED = "/save:zfe:dragon-tankard-acquired"
    const val ASK_SITHIK_ABOUT_OGRES = "/save:zfe:sithik-ask-about-ogres"
    const val ASK_SITHIK_AGAIN = "/save:zfe:sithik-ask-sithik-again"
    const val SITHIK_DIALOGUE_UNLOCK = "/save:zfe:sithik-stage-unlocked"
    const val SITHIK_TURN_INTO_OGRE = "/save:zfe:sithik-transformation"
    const val TORN_PAGE_ON_NECRO_BOOK = "/save:zfe:missed-page"
    const val TALK_ABOUT_NECRO_BOOK = "/save:zfe:talk:0"
    const val TALK_ABOUT_BLACK_PRISM = "/save:zfe:talk:1"
    const val TALK_ABOUT_TORN_PAGE = "/save:zfe:talk:2"
    const val TALK_ABOUT_TANKARD = "/save:zfe:talk:3"
    const val TALK_ABOUT_TANKARD_AGAIN = "/save:zfe:talk:4"
    const val TALK_ABOUT_SIGN_PORTRAIT = "/save:zfe:talk:5"
    const val TALK_AGAIN_ABOUT_HAM_BOOK = "/save:zfe:talk:6"
    const val TALK_AGAIN_1 = "/save:zfe:talk:7"
    const val TALK_AGAIN_2 = "/save:zfe:talk:8"
    const val TALK_AGAIN_3 = "/save:zfe:talk:9"
    const val TALK_AGAIN_4 = "/save:zfe:talk:10"
    const val TALK_AGAIN_5 = "/save:zfe:talk:11"
    const val TALK_AGAIN_6 = "/save:zfe:talk:12"
    const val TALK_WITH_SITHIK_OGRE_DONE = "/save:zfe:sithik-stage-complete"
    const val TALK_WITH_ZAVISTIC_DONE = "/save:zfe:zavistic-stage-complete"
    const val RECEIVED_KEY_FROM_GRISH = "/save:zfe:gate-key-received"
    const val NPC_ACTIVE = "/save:zfe:zavistic-spawned"
    const val SLASH_BASH_ACTIVE = "/save:zfe:boss-spawned"
    const val ZOMBIE_NPC_ACTIVE = "/save:zfe:zombie-activated"
    const val ZOMBIE_NPC_SPAWN = "/save:zfe:zombie-spawned"
}
