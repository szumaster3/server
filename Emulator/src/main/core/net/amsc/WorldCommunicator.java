package core.net.amsc;

import core.game.node.entity.player.info.login.LoginParser;
import core.game.world.GameWorld;
import core.net.EventProducer;
import core.net.IoSession;
import core.net.NioReactor;
import core.net.producer.MSHSEventProducer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type World communicator.
 */
public final class WorldCommunicator {

    private static final EventProducer HANDSHAKE_PRODUCER = new MSHSEventProducer();
    private static final WorldStatistics[] WORLDS = new WorldStatistics[10];
    private static final Map<String, LoginParser> loginAttempts = new ConcurrentHashMap<>();
    private static ManagementServerState state = ManagementServerState.CONNECTING;
    private static IoSession session;
    private static NioReactor reactor;

    /**
     * Register.
     *
     * @param session the session
     */
    public static void register(IoSession session) {
        WorldCommunicator.session = session;
        session.setProducer(HANDSHAKE_PRODUCER);
        session.write(true);
        WORLDS[GameWorld.getSettings().getWorldId() - 1] = new WorldStatistics(GameWorld.getSettings().getWorldId());
        session.setObject(WORLDS[GameWorld.getSettings().getWorldId() - 1]);
    }

    /**
     * Connect.
     */
    public static void connect() {
        try {
            setState(ManagementServerState.CONNECTING);

            reactor = NioReactor.connect(GameWorld.getSettings().getMsAddress(), 5555);
            //}
            reactor.start();
        } catch (Throwable e) {
            e.printStackTrace();
            terminate();
        }
    }

    private static boolean isLocallyHosted() throws IOException {
        InetAddress address = InetAddress.getByName(GameWorld.getSettings().getMsAddress());
        if (address.isAnyLocalAddress() || address.isLoopbackAddress()) {
            return true;
        }
        return NetworkInterface.getByInetAddress(address) != null;
    }

    /**
     * Terminate.
     */
    public static void terminate() {
        setState(ManagementServerState.NOT_AVAILABLE);
        if (reactor != null) {
            reactor.terminate();
            reactor = null;
        }
    }

    /**
     * Finish login attempt login parser.
     *
     * @param username the username
     * @return the login parser
     */
    public static LoginParser finishLoginAttempt(String username) {
        return loginAttempts.remove(username);
    }

    /**
     * Gets local world.
     *
     * @return the local world
     */
    public static WorldStatistics getLocalWorld() {
        return WORLDS[GameWorld.getSettings().getWorldId() - 1];
    }

    /**
     * Gets world.
     *
     * @param playerName the player name
     * @return the world
     */
    public static int getWorld(String playerName) {
        for (int i = 0; i < WORLDS.length; i++) {
            if (WORLDS[i].getPlayers().contains(playerName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets world.
     *
     * @param id the id
     * @return the world
     */
    public static WorldStatistics getWorld(int id) {
        return WORLDS[id - 1];
    }

    /**
     * Gets session.
     *
     * @return the session
     */
    public static IoSession getSession() {
        return session;
    }

    /**
     * Is enabled boolean.
     *
     * @return the boolean
     */
    public static boolean isEnabled() {
        return state == ManagementServerState.AVAILABLE;
    }

    /**
     * Gets login attempts.
     *
     * @return the login attempts
     */
    public static Map<String, LoginParser> getLoginAttempts() {
        return loginAttempts;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public static ManagementServerState getState() {
        return state;
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public static void setState(ManagementServerState state) {
        if (WorldCommunicator.state != state) {
            WorldCommunicator.state = state;
            state.set();
        }
    }

    /**
     * Gets reactor.
     *
     * @return the reactor
     */
    public static NioReactor getReactor() {
        return reactor;
    }

    /**
     * Sets reactor.
     *
     * @param reactor the reactor
     */
    public static void setReactor(NioReactor reactor) {
        WorldCommunicator.reactor = reactor;
    }

}