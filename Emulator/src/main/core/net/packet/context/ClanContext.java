package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.system.communication.ClanRepository;
import core.net.packet.Context;

/**
 * The type Clan context.
 */
public final class ClanContext implements Context {

    private final Player player;

    private final ClanRepository clan;

    private final boolean leave;

    /**
     * Instantiates a new Clan context.
     *
     * @param player the player
     * @param clan   the clan
     * @param leave  the leave
     */
    public ClanContext(Player player, ClanRepository clan, boolean leave) {
        this.player = player;
        this.clan = clan;
        this.leave = leave;
    }

    /**
     * Gets clan.
     *
     * @return the clan
     */
    public ClanRepository getClan() {
        return clan;
    }

    /**
     * Is leave boolean.
     *
     * @return the boolean
     */
    public boolean isLeave() {
        return leave;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}