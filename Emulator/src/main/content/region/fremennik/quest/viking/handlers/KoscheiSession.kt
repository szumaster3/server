package content.region.fremennik.quest.viking.handlers

import core.game.node.entity.player.Player
import org.rs.consts.NPCs

class KoscheiSession(
    val player: Player,
) {
    private val koschei: KoscheiNPC =
        KoscheiNPC(
            NPCs.KOSCHEI_THE_DEATHLESS_1290,
            player.location?.transform(1, 0, 0),
            this,
        )

    init {
        if (player.getExtension<Any?>(KoscheiSession::class.java) != null) {
            player.removeExtension(KoscheiSession::class.java)
        }
        player.addExtension(KoscheiSession::class.java, this)
    }

    fun start() {
        koschei.init()
        player.unlock()
    }

    fun close() {
        koschei.clear()

        player.removeExtension(KoscheiSession::class.java)
    }

    companion object {
        fun create(player: Player): KoscheiSession {
            return KoscheiSession(player)
        }

        fun getSession(player: Player): KoscheiSession {
            return player.getExtension(KoscheiSession::class.java)
        }
    }
}
