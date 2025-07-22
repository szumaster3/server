package content.region.fremennik.rellekka.quest.viking.npc

import core.game.node.entity.player.Player
import org.rs.consts.NPCs

/**
 * Manages the session for the Koschei the Deathless warrior's trial during the Fremennik Trials quest.
 *
 * @property player The player participating in the trial.
 */
class KoscheiSession(
    val player: Player,
) {
    /**
     * The instance of Koschei the Deathless tied to this session.
     */
    private val koschei: KoscheiNPC =
        KoscheiNPC(
            NPCs.KOSCHEI_THE_DEATHLESS_1290,
            player.location?.transform(1, 0, 0),
            this,
        )

    init {
        /*
         * Ensures a player only has one active session by replacing any existing session.
         */
        if (player.getExtension<Any?>(KoscheiSession::class.java) != null) {
            player.removeExtension(KoscheiSession::class.java)
        }
        player.addExtension(KoscheiSession::class.java, this)
    }

    /**
     * Starts the session.
     */
    fun start() {
        koschei.init()
        player.unlock()
    }

    /**
     * Ends the session.
     */
    fun close() {
        koschei.clear()
        player.removeExtension(KoscheiSession::class.java)
    }

    companion object {

        fun create(player: Player): KoscheiSession = KoscheiSession(player)

        fun getSession(player: Player): KoscheiSession = player.getExtension(KoscheiSession::class.java)
    }
}
