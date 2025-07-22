package core.game.node.entity.player.link.music;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.zone.Zone;
import core.game.world.map.zone.ZoneBorders;

/**
 * Represents a music zone.
 */
public final class MusicZone implements Zone {

    /**
     * The id of the music track to unlock when a player enters this zone.
     */
    private final int musicId;

    /**
     * The geographical borders that define this music zone.
     */
    private final ZoneBorders borders;

    /**
     * Constructs a new {@code MusicZone} with the specified music ID and borders.
     *
     * @param musicId the ID of the music track associated with this zone.
     * @param borders the zone borders that define the area.
     */
    public MusicZone(int musicId, ZoneBorders borders) {
        this.musicId = musicId;
        this.borders = borders;
    }

    /**
     * Called when an entity enters the zone.
     *
     * @param e the entity entering the zone.
     * @return {@code true} if the zone was successfully entered.
     * @throws IllegalStateException if the entity is not a player.
     */
    @Override
    public boolean enter(Entity e) {
        if (!(e instanceof Player)) {
            throw new IllegalStateException("Music is for players only!");
        }
        Player player = (Player) e;
        player.getMusicPlayer().unlock(musicId);
        return true;
    }

    /**
     * Called when an entity leaves the zone.
     *
     * @param e the entity leaving the zone.
     * @param logout whether the entity is logging out.
     * @return {@code true} always.
     */
    @Override
    public boolean leave(Entity e, boolean logout) {
        return true;
    }

    /**
     * Gets the id of the music track associated with this zone.
     *
     * @return the music id.
     */
    public int getMusicId() {
        return musicId;
    }

    /**
     * Gets the borders that define the area of this zone.
     *
     * @return the zone borders.
     */
    public ZoneBorders getBorders() {
        return borders;
    }

}