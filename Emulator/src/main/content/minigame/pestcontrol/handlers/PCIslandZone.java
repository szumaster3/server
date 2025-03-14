package content.minigame.pestcontrol.handlers;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneRestriction;

/**
 * The type Pc island zone.
 */
public final class PCIslandZone extends MapZone {

    /**
     * Instantiates a new Pc island zone.
     */
    public PCIslandZone() {
        super("pest control island", true, ZoneRestriction.CANNON, ZoneRestriction.FIRES, ZoneRestriction.RANDOM_EVENTS);
    }

    @Override
    public boolean death(Entity e, Entity killer) {
        if (e instanceof Player) {
            e.getProperties().setTeleportLocation(e.getLocation());
            return true;
        }
        return false;
    }

    @Override
    public void configure() {
        registerRegion(10537);
    }

}