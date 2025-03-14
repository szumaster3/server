package content.data

import core.game.node.item.Item
import org.rs.consts.Items

enum class Lamps(
    val item: Int,
    val experience: Int,
    val requiredLevel: Int = 0,
) {
    GENIE_LAMP(
        item = Items.LAMP_2528,
        experience = 10,
    ),

    STRONGHOLD_LAMP(
        item = Items.ANTIQUE_LAMP_4447,
        experience = 500,
    ),

    BLESSED_LAMP(
        item = Items.BLESSED_LAMP_10889,
        experience = 5000,
        requiredLevel = 30,
    ),

    UNASSIGNED_0(
        item = Items.ANTIQUE_LAMP_11189,
        experience = 1,
    ),

    UNASSIGNED_1(
        item = Items.ANTIQUE_LAMP_12627,
        experience = 1,
    ),

    COMBAT_LAMP(
        item = Items.COMBAT_LAMP_10586,
        experience = 7000,
    ),

    DREAMY_LAMP(
        item = Items.DREAMY_LAMP_11157,
        experience = 15000,
    ),

    MYSTERIOUS_LAMP(
        Items.MYSTERIOUS_LAMP_13227,
        experience = 10000,
        requiredLevel = 30,
    ),

    QUEST_REWARD_LAMP_1(
        item = Items.ANTIQUE_LAMP_7498,
        experience = 2500,
        requiredLevel = 30,
    ),

    QUEST_REWARD_LAMP_2(
        item = Items.ANTIQUE_LAMP_13446,
        experience = 600,
        requiredLevel = 1,
    ),

    QUEST_REWARD_LAMP_3(
        item = Items.ANTIQUE_LAMP_13447,
        experience = 5000,
        requiredLevel = 30,
    ),

    QUEST_REWARD_LAMP_4(
        item = Items.ANTIQUE_LAMP_13448,
        experience = 7000,
        requiredLevel = 50,
    ),

    QUEST_REWARD_LAMP_5(
        item = Items.ANTIQUE_LAMP_13463,
        experience = 20000,
        requiredLevel = 50,
    ),

    K_ACHIEVEMENT_1(
        item = Items.ANTIQUE_LAMP_11137,
        experience = 1000,
        requiredLevel = 30,
    ),

    K_ACHIEVEMENT_2(
        item = Items.ANTIQUE_LAMP_11139,
        experience = 5000,
        requiredLevel = 40,
    ),

    K_ACHIEVEMENT_3(
        item = Items.ANTIQUE_LAMP_11141,
        experience = 10000,
        requiredLevel = 50,
    ),

    V_ACHIEVEMENT_1(
        item = Items.ANTIQUE_LAMP_11753,
        experience = 1000,
        requiredLevel = 30,
    ),

    V_ACHIEVEMENT_2(
        item = Items.ANTIQUE_LAMP_11754,
        experience = 5000,
        requiredLevel = 40,
    ),

    V_ACHIEVEMENT_3(
        item = Items.ANTIQUE_LAMP_11755,
        experience = 10000,
        requiredLevel = 50,
    ),

    L_ACHIEVEMENT_1(
        item = Items.ANTIQUE_LAMP_11185,
        experience = 500,
        requiredLevel = 1,
    ),

    L_ACHIEVEMENT_2(
        item = Items.ANTIQUE_LAMP_11186,
        experience = 1000,
        requiredLevel = 30,
    ),

    L_ACHIEVEMENT_3(
        item = Items.ANTIQUE_LAMP_11187,
        experience = 1500,
        requiredLevel = 35,
    ),

    FALLY_ACHIEVEMENT_1(
        item = Items.ANTIQUE_LAMP_14580,
        experience = 1000,
        requiredLevel = 30,
    ),

    FALLY_ACHIEVEMENT_2(
        item = Items.ANTIQUE_LAMP_14581,
        experience = 5000,
        requiredLevel = 40,
    ),

    FALLY_ACHIEVEMENT_3(
        item = Items.ANTIQUE_LAMP_14582,
        experience = 10000,
        requiredLevel = 50,
    ),

    FREM_ACHIEVEMENT_1(
        item = Items.ANTIQUE_LAMP_14574,
        experience = 5000,
        requiredLevel = 30,
    ),

    FREM_ACHIEVEMENT_2(
        item = Items.ANTIQUE_LAMP_14575,
        experience = 10000,
        requiredLevel = 40,
    ),

    FREM_ACHIEVEMENT_3(
        item = Items.ANTIQUE_LAMP_14576,
        experience = 15000,
        requiredLevel = 50,
    ),

    SEERS_ACHIEVEMENT_1(
        item = Items.ANTIQUE_LAMP_14633,
        experience = 1000,
        requiredLevel = 30,
    ),

    SEERS_ACHIEVEMENT_2(
        item = Items.ANTIQUE_LAMP_14634,
        experience = 5000,
        requiredLevel = 40,
    ),

    SEERS_ACHIEVEMENT_3(
        item = Items.ANTIQUE_LAMP_14635,
        experience = 10000,
        requiredLevel = 50,
    ),

    EXP_BOOK_3(
        item = Items.TOME_OF_XP_3_9656,
        experience = 2000,
        requiredLevel = 30,
    ),

    EXP_BOOK_2(
        item = Items.TOME_OF_XP_2_9657,
        experience = 2000,
        requiredLevel = 30,
    ),

    EXP_BOOK_1(
        item = Items.TOME_OF_XP_1_9658,
        experience = 2000,
        requiredLevel = 30,
    ),

    EXP_BOOK_2ND_3(
        item = Items.TOME_OF_XP_2ND_ED_3_13160,
        experience = 2500,
        requiredLevel = 35,
    ),

    EXP_BOOK_2ND_2(
        item = Items.TOME_OF_XP_2ND_ED_2_13161,
        experience = 2500,
        requiredLevel = 35,
    ),

    EXP_BOOK_2ND_1(
        item = Items.TOME_OF_XP_2ND_ED_1_13162,
        experience = 2500,
        requiredLevel = 35,
    ),
    ;

    companion object {
        @JvmStatic
        fun forItem(item: Item): Lamps? {
            for (lamp in values()) {
                if (lamp.item == item.id) {
                    return lamp
                }
            }
            return null
        }
    }
}
