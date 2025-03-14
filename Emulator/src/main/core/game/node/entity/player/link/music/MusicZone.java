package core.game.node.entity.player.link.music;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.zone.Zone;
import core.game.world.map.zone.ZoneBorders;

/**
 * The type Music zone.
 */
public final class MusicZone implements Zone {

    private final int musicId;

    private final ZoneBorders borders;

    /**
     * Instantiates a new Music zone.
     *
     * @param musicId the music id
     * @param borders the borders
     */
    public MusicZone(int musicId, ZoneBorders borders) {
        this.musicId = musicId;
        this.borders = borders;
    }

    @Override
    public boolean enter(Entity e) {
        if (!(e instanceof Player)) {
            throw new IllegalStateException("Music is for players only!");
        }
        Player player = (Player) e;
        player.getMusicPlayer().unlock(musicId);
        return true;
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        return true;
    }

    /**
     * Gets music id.
     *
     * @return the music id
     */
    public int getMusicId() {
        return musicId;
    }

    /**
     * Gets borders.
     *
     * @return the borders
     */
    public ZoneBorders getBorders() {
        return borders;
    }

}