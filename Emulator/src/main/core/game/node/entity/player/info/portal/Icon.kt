package core.game.node.entity.player.info.portal

enum class Icon(
    val id: Int,
    val index: Int,
) {
    NOTHING(0, 0),

    GREEN(1, 5),

    RED(2, 6),

    YELLOW(3, 7),

    BLUE(4, 8),

    ORANGE(5, 9),

    PINK(6, 10),

    PURPLE(7, 11),

    BROWN(8, 12),
    ;

    companion object {
        @JvmStatic
        fun forId(id: Int): Icon {
            for (icon in values()) {
                if (icon.id == id) {
                    return icon
                }
            }
            return GREEN
        }
    }
}
