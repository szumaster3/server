package core.api

import core.game.node.entity.player.Player
import org.json.simple.JSONObject

/**
 * Interface for managing player data that needs to be saved and loaded during player sessions.
 *
 * - **Parsing** occurs *after* [LoginListener] is executed.
 * - **Saving** occurs *after* [LogoutListener] is executed.
 */
interface PersistPlayer : ContentInterface {
    /**
     * **NOTE**: This should **NOT** reference nonstatic class-local variables.
     * You need to fetch a player's specific instance of the data and save from that.
     *
     * @see [content.global.skill.slayer.SlayerManager]
     */
    fun savePlayer(
        player: Player,
        save: JSONObject,
    )

    /**
     * **NOTE**: This should **NOT** reference nonstatic class-local variables.
     * You need to fetch a player's specific instance of the data and parse to that.
     *
     * @see [content.global.skill.slayer.SlayerManager]
     */
    fun parsePlayer(
        player: Player,
        data: JSONObject,
    )
}
