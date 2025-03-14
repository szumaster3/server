package core.game.activity;

import core.ServerConstants;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.Region;
import core.game.world.map.build.DynamicRegion;
import core.game.world.map.zone.*;
import core.game.world.map.zone.impl.MultiwayCombatZone;
import core.plugin.Plugin;
import core.plugin.PluginManifest;
import core.plugin.PluginType;

/**
 * The type Activity plugin.
 */
@PluginManifest(type = PluginType.ACTIVITY)
public abstract class ActivityPlugin extends MapZone implements Plugin<Player> {

    private boolean instanced;

    private boolean multicombat;

    private boolean safe;

    /**
     * The Region.
     */
    protected DynamicRegion region;

    /**
     * The Base.
     */
    protected Location base;

    /**
     * The Safe respawn.
     */
    protected Location safeRespawn = ServerConstants.HOME_LOCATION;

    /**
     * The Player.
     */
    protected Player player;

    /**
     * Instantiates a new Activity plugin.
     *
     * @param name         the name
     * @param instanced    the instanced
     * @param multicombat  the multicombat
     * @param safe         the safe
     * @param restrictions the restrictions
     */
    public ActivityPlugin(String name, boolean instanced, boolean multicombat, boolean safe, ZoneRestriction... restrictions) {
        super(name, true, ZoneRestriction.RANDOM_EVENTS);
        for (ZoneRestriction restriction : restrictions) {
            addRestriction(restriction.getFlag());
        }
        this.instanced = instanced;
        this.multicombat = multicombat;
        this.safe = safe;
        if (safe) {
            setZoneType(ZoneType.SAFE.getId());
        }
    }

    @Override
    public void register(ZoneBorders borders) {
        if (multicombat) {
            MultiwayCombatZone.Companion.getInstance().register(borders);
        }
        super.register(borders);
    }

    /**
     * Sets region base.
     */
    protected void setRegionBase() {
        if (region != null) {
            if (multicombat) {
                region.toggleMulticombat();
            }
            setBase(Location.create(region.getBorders().getSouthWestX(), region.getBorders().getSouthWestY(), 0));
        }
    }

    /**
     * Sets region base.
     *
     * @param regions the regions
     */
    protected void setRegionBase(DynamicRegion[] regions) {
        region = regions[0];
        Location l = region.getBaseLocation();
        for (DynamicRegion r : regions) {
            if (r.getX() > l.getX() || r.getY() > l.getY()) {
                l = r.getBaseLocation();
            }
        }
        ZoneBorders borders = new ZoneBorders(region.getX() << 6, region.getY() << 6, l.getX() + Region.SIZE, l.getY() + Region.SIZE);
        RegionZone multiZone = multicombat ? new RegionZone(MultiwayCombatZone.Companion.getInstance(), borders) : null;
        RegionZone zone = new RegionZone(this, borders);
        for (DynamicRegion r : regions) {
            if (multicombat) {
                r.setMulticombat(true);
                r.getRegionZones().add(multiZone);
            }
            r.getRegionZones().add(zone);
        }
        setBase(Location.create(borders.getSouthWestX(), borders.getSouthWestY(), 0));
    }

    /**
     * Start boolean.
     *
     * @param player the player
     * @param login  the login
     * @param args   the args
     * @return the boolean
     */
    public boolean start(Player player, boolean login, Object... args) {
        this.player = player;
        return true;
    }

    @Override
    public boolean enter(Entity e) {
        Location l;
        if (e instanceof Player && (l = getSpawnLocation()) != null) {
            e.getProperties().setSpawnLocation(l);
        }
        e.getProperties().setSafeZone(safe);
        e.getProperties().safeRespawn = this.safeRespawn;
        e.setAttribute("activity", this);
        return super.enter(e);
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        if (e instanceof Player) {
            e.getProperties().setSpawnLocation(ServerConstants.HOME_LOCATION);
        }
        Location l;
        if (instanced && logout && (l = getSpawnLocation()) != null) {
            e.setLocation(l);
        }
        e.getProperties().setSafeZone(false);
        e.getProperties().safeRespawn = ServerConstants.HOME_LOCATION;
        e.removeAttribute("activity");
        return super.leave(e, logout);
    }

    /**
     * Register.
     */
    public void register() {
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public abstract ActivityPlugin newInstance(Player p) throws Throwable;

    /**
     * Gets spawn location.
     *
     * @return the spawn location
     */
    public abstract Location getSpawnLocation();

    /**
     * Is instanced boolean.
     *
     * @return the boolean
     */
    public boolean isInstanced() {
        return instanced;
    }

    /**
     * Sets instanced.
     *
     * @param instanced the instanced
     */
    public void setInstanced(boolean instanced) {
        this.instanced = instanced;
    }

    /**
     * Is multicombat boolean.
     *
     * @return the boolean
     */
    public boolean isMulticombat() {
        return multicombat;
    }

    /**
     * Sets multicombat.
     *
     * @param multicombat the multicombat
     */
    public void setMulticombat(boolean multicombat) {
        this.multicombat = multicombat;
    }

    /**
     * Is safe boolean.
     *
     * @return the boolean
     */
    public boolean isSafe() {
        return safe;
    }

    /**
     * Sets safe.
     *
     * @param safe the safe
     */
    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets base.
     *
     * @return the base
     */
    public Location getBase() {
        return base;
    }

    /**
     * Sets base.
     *
     * @param base the base
     */
    public void setBase(Location base) {
        this.base = base;
    }

}