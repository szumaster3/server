package content.region.asgarnia.quest.gobdip.dialogue

import core.api.getVarp
import core.api.setVarp
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items

enum class GrubFoot(
    val id: Int,
    val value: Int,
    val mail: Item,
) {
    NORMAL(4495, 1, Item(Items.GOBLIN_MAIL_288)),
    ORANGE(4497, 4, Item(Items.ORANGE_GOBLIN_MAIL_286)),
    BLUE(4498, 5, Item(Items.BLUE_GOBLIN_MAIL_287)),
    BROWN(4496, 6, Item(Items.GOBLIN_MAIL_288)),
    ;

    fun setConfig(player: Player?) {
        setVarp(player!!, 62, value)
    }

    companion object {
        @JvmStatic
        fun forConfig(player: Player?): GrubFoot {
            val config = getVarp(player!!, 62)
            for (foot in values()) {
                if (foot.value == config) {
                    if (foot.ordinal + 1 >= values().size) {
                        return BROWN
                    }
                    return values()[foot.ordinal + 1]
                }
            }
            return NORMAL
        }
    }
}
