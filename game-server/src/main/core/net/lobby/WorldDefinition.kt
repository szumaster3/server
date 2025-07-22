package core.net.lobby

import core.game.world.GameWorld
import core.game.world.repository.Repository

/**
 * Represents a world definition.
 */
class WorldDefinition(
    private val worldId: Int,
    private val location: Int,
    private val flag: Int,
    private val activity: String,
    private val ip: String,
    private val region: String,
    private val country: Int,
) {

    /**
     * Current number of players reported for this world.
     */
    private var players: Int = 0

    /**
     * The activity for this world.
     */
    fun getActivity(): String = activity

    /**
     * The coutry flag.
     */
    fun getCountry(): Int = country

    /**
     * If the world is members.
     */
    fun getFlag(): Int = flag

    /**
     * The ip-address for this world.
     */
    fun getIp(): String = ip

    /**
     * The location.
     */
    fun getLocation(): Int = location

    /**
     * The region.
     */
    fun getRegion(): String = region

    /**
     * The world id.
     */
    fun getWorldId(): Int = worldId

    /**
     * The amount of players in this world.
     */
    fun getPlayers(): Int = players

    /**
     * Sets the players.
     *
     * @param players the players to set.
     */
    fun setPlayers(players: Int) {
        this.players = players
    }

    /**
     * Gets the player count.
     *
     * @return The player count.
     */
    fun getPlayerCount(): Int {
        return if (worldId == GameWorld.settings!!.worldId) {
            Repository.players.size
        } else {
            players
        }
    }
}
