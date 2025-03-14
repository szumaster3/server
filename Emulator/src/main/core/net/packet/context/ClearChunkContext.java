package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.world.map.RegionChunk;
import core.net.packet.Context;

/**
 * The type Clear chunk context.
 */
public final class ClearChunkContext implements Context {

    private final Player player;

    private final RegionChunk chunk;

    /**
     * Instantiates a new Clear chunk context.
     *
     * @param player the player
     * @param chunk  the chunk
     */
    public ClearChunkContext(Player player, RegionChunk chunk) {
        this.player = player;
        this.chunk = chunk;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets chunk.
     *
     * @return the chunk
     */
    public RegionChunk getChunk() {
        return chunk;
    }

}
