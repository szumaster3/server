package core.game.world.map.zone;

import java.util.Objects;

/**
 * The type Region zone.
 */
public final class RegionZone {

    private final MapZone zone;

    private final ZoneBorders borders;

    /**
     * Instantiates a new Region zone.
     *
     * @param zone    the zone
     * @param borders the borders
     */
    public RegionZone(MapZone zone, ZoneBorders borders) {
        this.zone = zone;
        this.borders = borders;
    }

    /**
     * Gets borders.
     *
     * @return the borders
     */
    public ZoneBorders getBorders() {
        return borders;
    }

    /**
     * Gets zone.
     *
     * @return the zone
     */
    public MapZone getZone() {
        return zone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionZone that = (RegionZone) o;
        return Objects.equals(zone, that.zone) && Objects.equals(borders, that.borders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zone, borders);
    }
}
