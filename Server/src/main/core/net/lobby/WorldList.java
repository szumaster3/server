package core.net.lobby;

import core.game.world.GameWorld;
import core.net.IoSession;
import core.net.packet.IoBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Server distribution data:
 *
 * <p>Oceania:
 * <br>Countries: Australia, New Zealand
 * <br>Free servers: 4, 1
 * <br>Members servers: 4, 1
 *
 * <p>Europe:
 * <br>Countries: Belgium, Denmark, Finland, Ireland, Netherlands, Norway, Sweden, United Kingdom
 * <br>Free servers: 1, 1, 3, 1, 3, 1, 3, 10
 * <br>Members servers: 1, 1, 3, 1, 6, 1, 3, 10
 *
 * <p>Americas:
 * <br>Countries: Canada, United States, Mexico, Brazil
 * <br>Free servers: 8, 45, 0, 0
 * <br>Members servers: 10, 40, 1, 1
 *
 * <p>RuneScape German:
 * <br>Country: Germany
 * <br>Free servers: 3
 * <br>Members servers: 2
 *
 * <p>RuneScape France:
 * <br>Country: France
 * <br>Free servers: 4
 * <br>Members servers: 1
 *
 * <p>Totals:
 * <br>Free servers: 88
 * <br>Total servers: 175
 */
public final class WorldList {
    // Continent names.
    public static final String CONTINENT_OCEANIA = "Oceania";
    public static final String CONTINENT_EUROPE = "Europe";
    public static final String CONTINENT_AMERICAS = "Americas";
    // Country names.
    public static final String COUNTRY_AUSTRALIA = "Australia";
    public static final String COUNTRY_NEW_ZEALAND = "New Zealand";
    public static final String COUNTRY_BELGIUM = "Belgium";
    public static final String COUNTRY_DENMARK = "Denmark";
    public static final String COUNTRY_FINLAND = "Finland";
    public static final String COUNTRY_IRELAND = "Ireland";
    public static final String COUNTRY_NETHERLANDS = "Netherlands";
    public static final String COUNTRY_NORWAY = "Norway";
    public static final String COUNTRY_SWEDEN = "Sweden";
    public static final String COUNTRY_UK = "United Kingdom";
    public static final String COUNTRY_CANADA = "Canada";
    public static final String COUNTRY_USA = "United States";
    public static final String COUNTRY_MEXICO = "Mexico";
    public static final String COUNTRY_BRAZIL = "Brazil";
    public static final String COUNTRY_GERMANY = "Germany";
    public static final String COUNTRY_FRANCE = "France";
    // Country flags.
    public static final int COUNTRY_FLAG_AUSTRALIA = 16;
    public static final int COUNTRY_FLAG_BELGIUM = 22;
    public static final int COUNTRY_FLAG_BRAZIL = 31;
    public static final int COUNTRY_FLAG_CANADA = 38;
    public static final int COUNTRY_FLAG_DENMARK = 58;
    public static final int COUNTRY_FLAG_FINLAND = 69;
    public static final int COUNTRY_FLAG_IRELAND = 101;
    public static final int COUNTRY_FLAG_MEXICO = 152;
    public static final int COUNTRY_FLAG_NETHERLANDS = 161;
    public static final int COUNTRY_FLAG_NORWAY = 162;
    public static final int COUNTRY_FLAG_SWEDEN = 191;
    public static final int COUNTRY_FLAG_UK = 77;
    public static final int COUNTRY_FLAG_USA = 225;
    // Server mode.
    public static final int FLAG_NON_MEMBERS = 0;
    public static final int FLAG_MEMBERS = 1;
    public static final int FLAG_QUICK_CHAT = 2;
    public static final int FLAG_PVP = 4;
    public static final int FLAG_LOOTSHARE = 8;

    /**
     * Holds currently loaded worlds.
     */
    private static final List<WorldDefinition> WORLD_LIST = new ArrayList<>();

    /**
     * Last update time stamp (in server ticks).
     */
    private static int updateStamp = 0;

    /**
     * Themed worlds list.
     */
    public static final String[] THEMED_WORLDS = {
            "Trade - F2P",
            "Trade - Members",
            "Barbarian Assault",
            "Castle Wars",
            "Running - Air Runes",
            "<col=ffff00>Bounty Hunter</col>",
            "Fist of Guthix",
            "House Parties",
            "Running - Nature Runes",
            "Role-Playing Server",
            "Burthope Games Room",
            "Vinesweeper",
            "<col=ffff00>High Lvl Duel - Tournaments</col>",
            "Blast Furnace",
            "Great Orb Project",
            "Running - Law Runes",
            "Pest Control",
            "Runecrafting - ZMI Altar",
            "Shades of Mort'ton",
            "Tzhaar Fight Pits",
            "<col=ffff00>Duel - Tournaments",
            "Group Questing",
            "Falador Party Room",
            "Trouble Brewing",
            "Fishing Trawler",
            "Duel - Staked/Friendly",
            "Rat Pits"
    };

    static {
        addWorld(new WorldDefinition(
                1, 0,
                FLAG_MEMBERS | FLAG_LOOTSHARE,
                THEMED_WORLDS[new Random().nextInt(THEMED_WORLDS.length)],
                "127.0.0.1",
                COUNTRY_SWEDEN,
                COUNTRY_FLAG_SWEDEN
        ));
    }

    /**
     * Adds a world to the world list.
     *
     * @param def The world definition to add.
     */
    public static void addWorld(WorldDefinition def) {
        WORLD_LIST.add(def);
        flagUpdate();
    }

    /**
     * Sends the packet to update the world list in the lobby.
     *
     * @param session           The session to send the update to.
     * @param clientUpdateStamp The client update stamp to compare.
     */
    public static void sendUpdate(IoSession session, int clientUpdateStamp) {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put((byte) 0);
        buf.putShort((short) 0);
        buf.put((byte) 1);

        IoBuffer buffer = new IoBuffer();

        if (clientUpdateStamp != updateStamp) {
            buf.put((byte) 1); // Indicates an update occurred.
            putWorldListInfo(buffer);
        } else {
            buf.put((byte) 0);
        }

        putPlayerInfo(buffer);
        ByteBuffer bufferByteBuf = buffer.toByteBuffer();

        if (bufferByteBuf.position() > 0) {
            buf.put((ByteBuffer) bufferByteBuf.flip());
        }

        buf.putShort(1, (short) (buf.position() - 3));
        session.queue((ByteBuffer) buf.flip());
    }

    /**
     * Adds the world configuration info to the packet buffer.
     *
     * @param buffer The packet buffer.
     */
    private static void putWorldListInfo(IoBuffer buffer) {
        buffer.putSmart(WORLD_LIST.size());
        putCountryInfo(buffer);
        buffer.putSmart(0);
        buffer.putSmart(WORLD_LIST.size());
        buffer.putSmart(WORLD_LIST.size());

        for (WorldDefinition w : WORLD_LIST) {
            buffer.putSmart(w.getWorldId());
            buffer.put(w.getLocation());
            buffer.putInt(w.getFlag());
            buffer.putJagString(w.getActivity());
            buffer.putJagString(w.getIp());
        }
        buffer.putInt(updateStamp);
    }

    /**
     * Adds the world status info to the packet buffer.
     *
     * @param buffer The packet buffer.
     */
    private static void putPlayerInfo(IoBuffer buffer) {
        for (WorldDefinition w : WORLD_LIST) {
            buffer.putSmart(w.getWorldId());
            buffer.putShort(w.getPlayerCount());
        }
    }

    /**
     * Adds country info for each world to the packet buffer.
     *
     * @param buffer The packet buffer.
     */
    private static void putCountryInfo(IoBuffer buffer) {
        for (WorldDefinition w : WORLD_LIST) {
            buffer.putSmart(w.getCountry());
            buffer.putJagString(w.getRegion());
        }
    }

    /**
     * Updates the update stamp to the current server tick.
     */
    public static void flagUpdate() {
        updateStamp = GameWorld.getTicks();
    }
}
