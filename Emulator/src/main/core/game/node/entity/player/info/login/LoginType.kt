package core.game.node.entity.player.info.login

enum class LoginType(
    type: Int,
) {
    NORMAL_LOGIN(16),

    RECONNECT_TYPE(18),
    ;

    var type: Int = 0
        private set

    init {
        this.type = type
    }

    companion object {
        @JvmStatic
        fun fromType(type: Int): LoginType? {
            return values().find { it.type == type }
        }
    }
}
