package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Music context.
 */
public class MusicContext implements Context {

    private Player player;

    private int musicId;

    private boolean secondary;

    /**
     * Instantiates a new Music context.
     *
     * @param player  the player
     * @param musicId the music id
     */
    public MusicContext(Player player, int musicId) {
        this(player, musicId, false);
    }

    /**
     * Instantiates a new Music context.
     *
     * @param player    the player
     * @param musicId   the music id
     * @param temporary the temporary
     */
    public MusicContext(Player player, int musicId, boolean temporary) {
        this.player = player;
        this.musicId = musicId;
        this.secondary = temporary;
    }

    /**
     * Gets music id.
     *
     * @return the music id
     */
    public final int getMusicId() {
        return musicId;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Is secondary boolean.
     *
     * @return the boolean
     */
    public boolean isSecondary() {
        return secondary;
    }

    /**
     * Sets secondary.
     *
     * @param secondary the secondary
     */
    public void setSecondary(boolean secondary) {
        this.secondary = secondary;
    }

}