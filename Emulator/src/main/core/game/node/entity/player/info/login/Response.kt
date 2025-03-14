package core.game.node.entity.player.info.login

enum class Response(
    private val opcode: Int,
) {
    UNEXPECTED_RESPONSE(0),

    COULD_NOT_DISPLAY_AD(1),

    SUCCESSFUL(2),

    INVALID_CREDENTIALS(3),

    ACCOUNT_DISABLED(4),

    ALREADY_ONLINE(5),

    UPDATED(6),

    FULL_WORLD(7),

    LOGIN_SERVER_OFFLINE(8),

    LOGIN_LIMIT_EXCEEDED(9),

    BAD_SESSION_ID(10),

    WEAK_PASSWORD(11),

    MEMBERS_WORLD(12),

    COULD_NOT_LOGIN(13),

    UPDATING(14),

    TOO_MANY_INCORRECT_LOGINS(16),

    STANDING_IN_MEMBER(17),

    LOCKED(18),

    CLOSED_BETA(19),

    INVALID_LOGIN_SERVER(20),

    MOVING_WORLD(21),

    ERROR_LOADING_PROFILE(24),

    BANNED(26),
    ;

    fun opcode(): Int {
        return opcode // Returns the opcode of the response.
    }

    companion object {
        @JvmStatic
        fun get(opcode: Int): Response? {
            for (r in values()) {
                if (r.opcode == opcode) {
                    return r
                }
            }
            return null
        }
    }
}
