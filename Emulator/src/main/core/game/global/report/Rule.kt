package core.game.global.report

import core.game.node.entity.player.Player

enum class Rule(
    val rule: Int,
) {
    OFFENSIVE_LANGUAGE(0),
    ITEM_SCAMMING(1),
    PASSWORD_SCAMMING(2),
    BUG_ABUSE(3),
    STAFF_IMPERSONATION(4),
    ACCOUNT_SHARING(5),
    MACROING(6),
    MULTIPLE_LOGGING(7),
    ENCOURAGING_TO_BREAK_RULES(8),
    MISUSE_OF_CUSTOMER_SUPPORT(9),
    ADVERTISING(10),
    REAL_WORLD_ITEM_TRADING(11),
    ASKING_PERSONAL_DETAILS(12),
    ;

    companion object {
        @JvmStatic
        fun canRequest(target: Player?): Boolean {
            return target?.let {
                it.getSavedData().globalData.getChatPing() >= System.currentTimeMillis()
            } ?: false
        }

        @JvmStatic
        fun forId(id: Int): Rule? {
            return values().find { it.rule == id }
        }
    }
}
