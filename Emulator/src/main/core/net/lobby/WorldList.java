package core.net.lobby;

import core.game.world.GameWorld;
import core.net.IoSession;
import core.net.packet.IoBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * The type World list.
 */
public final class WorldList {

    /**
     * The constant COUNTRY_AUSTRALIA.
     */
    public static final int COUNTRY_AUSTRALIA = 16;

    /**
     * The constant COUNTRY_BELGIUM.
     */
    public static final int COUNTRY_BELGIUM = 22;

    /**
     * The constant COUNTRY_BRAZIL.
     */
    public static final int COUNTRY_BRAZIL = 31;

    /**
     * The constant COUNTRY_CANADA.
     */
    public static final int COUNTRY_CANADA = 38;

    /**
     * The constant COUNTRY_DENMARK.
     */
    public static final int COUNTRY_DENMARK = 58;

    /**
     * The constant COUNTRY_FINLAND.
     */
    public static final int COUNTRY_FINLAND = 69;

    /**
     * The constant COUNTRY_IRELAND.
     */
    public static final int COUNTRY_IRELAND = 101;

    /**
     * The constant COUNTRY_MEXICO.
     */
    public static final int COUNTRY_MEXICO = 152;

    /**
     * The constant COUNTRY_NETHERLANDS.
     */
    public static final int COUNTRY_NETHERLANDS = 161;

    /**
     * The constant COUNTRY_NORWAY.
     */
    public static final int COUNTRY_NORWAY = 162;

    /**
     * The constant COUNTRY_SWEDEN.
     */
    public static final int COUNTRY_SWEDEN = 191;

    /**
     * The constant COUNTRY_UK.
     */
    public static final int COUNTRY_UK = 77;

    /**
     * The constant COUNTRY_USA.
     */
    public static final int COUNTRY_USA = 225;

    /**
     * The constant FLAG_NON_MEMBERS.
     */
    public static final int FLAG_NON_MEMBERS = 0;

    /**
     * The constant FLAG_MEMBERS.
     */
    public static final int FLAG_MEMBERS = 1;

    /**
     * The constant FLAG_QUICK_CHAT.
     */
    public static final int FLAG_QUICK_CHAT = 2;

    /**
     * The constant FLAG_PVP.
     */
    public static final int FLAG_PVP = 4;

    /**
     * The constant FLAG_LOOTSHARE.
     */
    public static final int FLAG_LOOTSHARE = 8;

    private static final List<WorldDefinition> WORLD_LIST = new ArrayList<WorldDefinition>();

    private static int updateStamp = 0;

    static {
        addWorld(new WorldDefinition(1, 0, FLAG_MEMBERS | FLAG_LOOTSHARE, "2009Scape Classic", "127.0.0.1", "Anywhere, USA", COUNTRY_USA));
    }

    /**
     * Add world.
     *
     * @param def the def
     */
    public static void addWorld(WorldDefinition def) {
        WORLD_LIST.add(def);
        flagUpdate();
    }

    /**
     * Send update.
     *
     * @param session     the session
     * @param updateStamp the update stamp
     */
    public static void sendUpdate(IoSession session, int updateStamp) {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put((byte) 0);
        buf.putShort((short) 0);
        buf.put((byte) 1);
        IoBuffer buffer = new IoBuffer();
        if (updateStamp != WorldList.updateStamp) {
            buf.put((byte) 1); // Indicates an update occured.
            putWorldListinfo(buffer);
        } else {
            buf.put((byte) 0);
        }
        putPlayerInfo(buffer);
        if (buffer.toByteBuffer().position() > 0) {
            buf.put((ByteBuffer) buffer.toByteBuffer().flip());
        }
        buf.putShort(1, (short) (buf.position() - 3));
        session.queue((ByteBuffer) buf.flip());
    }

    private static void putWorldListinfo(IoBuffer buffer) {
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

    private static void putPlayerInfo(IoBuffer buffer) {
        for (WorldDefinition w : WORLD_LIST) {
            buffer.putSmart(w.getWorldId());
            buffer.putShort(w.getPlayerCount());
        }
    }

    private static void putCountryInfo(IoBuffer buffer) {
        for (WorldDefinition w : WORLD_LIST) {
            buffer.putSmart(w.getCountry());
            buffer.putJagString(w.getRegion());
        }
    }

    /**
     * Gets update stamp.
     *
     * @return the update stamp
     */
    public static int getUpdateStamp() {
        return updateStamp;
    }

    /**
     * Flag update.
     */
    public static void flagUpdate() {
        WorldList.updateStamp = GameWorld.getTicks();
    }
}
