package core.net;

/**
 * Network constants used by the game server.
 *
 * @author Vexia
 */
public final class Constants {

    /**
     * The port the server listens on.
     */
    public static final int PORT = 43594;

    /**
     * The revision of the game server.
     */
    public static final int REVISION = 530;

    /**
     * The client build, used for notifying players about updates.
     */
    public static final int CLIENT_BUILD = 1;

    /**
     * The default Management server IP.
     */
    public static final String DEFAULT_MS_IP = "127.0.0.1";

    /**
     * Private constructor to prevent instantiation.
     */
    private Constants() {
    }
}
