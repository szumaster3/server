package core.net.lobby

import core.game.world.GameWorld
import core.net.IoSession
import core.net.packet.IoBuffer
import java.nio.ByteBuffer

/**
 *
 *
 * Server distribution data:
 *
 * Oceania:
 *   Countries: Australia, New Zealand
 *   Free servers: 4, 1
 *   Members servers: 4, 1
 *
 * Europe:
 *   Countries: Belgium, Denmark, Finland, Ireland, Netherlands, Norway, Sweden, United Kingdom
 *   Free servers: 1, 1, 3, 1, 3, 1, 3, 10
 *   Members servers: 1, 1, 3, 1, 6, 1, 3, 10
 *
 * Americas:
 *   Countries: Canada, United States, Mexico, Brazil
 *   Free servers: 8, 45, 0, 0
 *   Members servers: 10, 40, 1, 1
 *
 * RuneScape German
 *
 * Europe:
 *   Country: Germany
 *   Free servers: 3
 *   Members servers: 2
 *
 * RuneScape France
 *
 * Europe:
 *   Country: France
 *   Free servers: 4
 *   Members servers: 1
 *
 * Totals:
 *   Free servers: 88
 *   Total servers: 175
 */

object WorldList {
    // Continent names
    const val CONTINENT_OCEANIA = "Oceania"
    const val CONTINENT_EUROPE = "Europe"
    const val CONTINENT_AMERICAS = "Americas"

    // Country names
    const val COUNTRY_AUSTRALIA = "Australia"
    const val COUNTRY_NEW_ZEALAND = "New Zealand"
    const val COUNTRY_BELGIUM = "Belgium"
    const val COUNTRY_DENMARK = "Denmark"
    const val COUNTRY_FINLAND = "Finland"
    const val COUNTRY_IRELAND = "Ireland"
    const val COUNTRY_NETHERLANDS = "Netherlands"
    const val COUNTRY_NORWAY = "Norway"
    const val COUNTRY_SWEDEN = "Sweden"
    const val COUNTRY_UK = "United Kingdom"
    const val COUNTRY_CANADA = "Canada"
    const val COUNTRY_USA = "United States"
    const val COUNTRY_MEXICO = "Mexico"
    const val COUNTRY_BRAZIL = "Brazil"
    const val COUNTRY_GERMANY = "Germany"
    const val COUNTRY_FRANCE = "France"

    // Country flags
    const val COUNTRY_FLAG_AUSTRALIA = 16
    const val COUNTRY_FLAG_BELGIUM = 22
    const val COUNTRY_FLAG_BRAZIL = 31
    const val COUNTRY_FLAG_CANADA = 38
    const val COUNTRY_FLAG_DENMARK = 58
    const val COUNTRY_FLAG_FINLAND = 69
    const val COUNTRY_FLAG_IRELAND = 101
    const val COUNTRY_FLAG_MEXICO = 152
    const val COUNTRY_FLAG_NETHERLANDS = 161
    const val COUNTRY_FLAG_NORWAY = 162
    const val COUNTRY_FLAG_SWEDEN = 191
    const val COUNTRY_FLAG_UK = 77
    const val COUNTRY_FLAG_USA = 225

    // Modes.
    const val FLAG_NON_MEMBERS = 0
    const val FLAG_MEMBERS = 1
    const val FLAG_QUICK_CHAT = 2
    const val FLAG_PVP = 4
    const val FLAG_LOOTSHARE = 8

    // Holds currently loaded worlds.
    private val WORLD_LIST: MutableList<WorldDefinition> = ArrayList()

    // Last update time stamp (in server ticks)
    private var updateStamp: Int = 0

    /**
     * Represents the themed worlds.
     */
    val THEMED_WORLDS = arrayOf(
        "Trade - F2P",
        "Trade - Members",
        "Barbarian Assault",
        "Castle Wars",
        "Running - Air Runes",
        "${core.tools.YELLOW}Bounty Hunter",
        "Fist of Guthix",
        "House Parties",
        "Running - Nature Runes",
        "Role-Playing Server",
        "Burthope Games Room",
        "Vinesweeper",
        "${core.tools.YELLOW}High Lvl Duel - Tournaments",
        "Blast Furnace",
        "Great Orb Project",
        "Running - Law Runes",
        "Pest Control",
        "Runecrafting - ZMI Altar",
        "Shades of Mort'ton",
        "Tzhaar Fight Pits",
        "${core.tools.YELLOW}Duel - Tournaments",
        "Group Questing",
        "Falador Party Room",
        "Trouble Brewing",
        "Fishing Trawler",
        "Duel - Staked/Friendly",
        "Rat Pits",
    )

    init {
        addWorld(
            WorldDefinition(
                1, 0,
                FLAG_MEMBERS or FLAG_LOOTSHARE,
                THEMED_WORLDS.random(),
                "127.0.0.1",
                COUNTRY_SWEDEN,
                COUNTRY_FLAG_SWEDEN
            )
        )
    }

    /**
     * Adds a world to the world list.
     *
     * @param def The world definition to add.
     */
    fun addWorld(def: WorldDefinition) {
        WORLD_LIST.add(def)
        flagUpdate()
    }

    /**
     * Sends the packet to update the world list in the lobby.
     *
     * @param session The session to send the update to.
     * @param updateStamp The client update stamp to compare.
     */
    fun sendUpdate(session: IoSession, updateStamp: Int) {
        val buf = ByteBuffer.allocate(1024)
        buf.put(0.toByte())
        buf.putShort(0)
        buf.put(1.toByte())
        val buffer = IoBuffer()
        if (updateStamp != this.updateStamp) {
            buf.put(1.toByte()) // Indicates an update occurred.
            putWorldListInfo(buffer)
        } else {
            buf.put(0.toByte())
        }
        putPlayerInfo(buffer)
        val bufferByteBuf = buffer.toByteBuffer()
        if (bufferByteBuf.position() > 0) {
            buf.put(bufferByteBuf.flip() as ByteBuffer)
        }
        buf.putShort(1, (buf.position() - 3).toShort())
        session.queue(buf.flip() as ByteBuffer)
    }

    /**
     * Adds the world configuration info to the packet buffer.
     *
     * @param buffer The packet buffer.
     */
    private fun putWorldListInfo(buffer: IoBuffer) {
        buffer.putSmart(WORLD_LIST.size)
        putCountryInfo(buffer)
        buffer.putSmart(0)
        buffer.putSmart(WORLD_LIST.size)
        buffer.putSmart(WORLD_LIST.size)
        for (w in WORLD_LIST) {
            buffer.putSmart(w.getWorldId())
            buffer.put(w.getLocation())
            buffer.putInt(w.getFlag())
            buffer.putJagString(w.getActivity())
            buffer.putJagString(w.getIp())
        }
        buffer.putInt(updateStamp)
    }

    /**
     * Adds the world status info to the packet buffer.
     *
     * @param buffer The packet buffer.
     */
    private fun putPlayerInfo(buffer: IoBuffer) {
        for (w in WORLD_LIST) {
            buffer.putSmart(w.getWorldId())
            buffer.putShort(w.getPlayerCount())
        }
    }

    /**
     * Adds country info for each world to the packet buffer.
     *
     * @param buffer The packet buffer.
     */
    private fun putCountryInfo(buffer: IoBuffer) {
        for (w in WORLD_LIST) {
            buffer.putSmart(w.getCountry())
            buffer.putJagString(w.getRegion())
        }
    }

    /**
     * Updates the update stamp to the current server tick.
     */
    @JvmStatic
    fun flagUpdate() {
        updateStamp = GameWorld.ticks
    }
}
