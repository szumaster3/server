package core.game.world.map;

import core.api.utils.Vector;
import core.game.interaction.DestinationFlag;
import core.game.node.Node;
import core.game.world.map.path.Path;
import core.game.world.map.path.Pathfinder;
import core.tools.RandomFunction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Location.
 */
public final class Location extends Node {

	private int x;

	private int y;

	private int z;

    /**
     * Instantiates a new Location.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public Location(int x, int y, int z) {
		super(null, null);
		super.destinationFlag = DestinationFlag.LOCATION;
		this.x = x;
		this.y = y;
		if (z < 0) {
			z += 4;
		}
		this.z = z;
	}

    /**
     * Instantiates a new Location.
     *
     * @param x the x
     * @param y the y
     */
    public Location(int x, int y) {
		this(x, y, 0);
	}

    /**
     * Instantiates a new Location.
     *
     * @param x          the x
     * @param y          the y
     * @param z          the z
     * @param randomizer the randomizer
     */
    public Location(int x, int y, int z, int randomizer) {
		this(x + RandomFunction.getRandom(randomizer), y + RandomFunction.getRandom(randomizer), z);
	}

    /**
     * Create location.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the location
     */
    public static Location create(int x, int y, int z) {
		return new Location(x, y, z);
	}

    /**
     * Create location.
     *
     * @param x the x
     * @param y the y
     * @return the location
     */
    public static Location create(int x, int y) {
		return new Location(x, y, 0);
	}

    /**
     * Create location.
     *
     * @param location the location
     * @return the location
     */
    public static Location create(Location location) {
		return create(location.getX(), location.getY(), location.getZ());
	}

    /**
     * Gets delta.
     *
     * @param location the location
     * @param other    the other
     * @return the delta
     */
    public static Location getDelta(Location location, Location other) {
		return Location.create(other.x - location.x, other.y - location.y, other.z - location.z);
	}

    /**
     * Gets random location.
     *
     * @param main      the main
     * @param radius    the radius
     * @param reachable the reachable
     * @return the random location
     */
    public static Location getRandomLocation(Location main, int radius, boolean reachable) {
		Location location = RegionManager.getTeleportLocation(main, radius);
		if (!reachable) {
			return location;
		}
		Path path = Pathfinder.find(main, location, false, Pathfinder.DUMB);
		if (!path.isSuccessful()) {
			location = main;
			if (!path.getPoints().isEmpty()) {
				Point p = path.getPoints().getLast();
				location = Location.create(p.getX(), p.getY(), main.getZ());
			}
		}
		return location;
	}

	@Override
	public Location getLocation() {
		return this;
	}

    /**
     * Is next to boolean.
     *
     * @param node the node
     * @return the boolean
     */
    public boolean isNextTo(Node node) {
		Location l = node.getLocation();
		if (l.getY() == y) {
			return l.getX() - x == -1 || l.getX() - x == 1;
		}
		if (l.getX() == x) {
			return l.getY() - y == -1 || l.getY() - y == 1;
		}
		return false;
	}

    /**
     * Gets region id.
     *
     * @return the region id
     */
    public int getRegionId() {
		return (x >> 6) << 8 | (y >> 6);
	}

    /**
     * Is in region boolean.
     *
     * @param region the region
     * @return the boolean
     */
    public boolean isInRegion(int region) {
        return getRegionId() == region;
    }

    /**
     * Transform location.
     *
     * @param dir the dir
     * @return the location
     */
    public Location transform(Direction dir) {
		return transform(dir, 1);
	}

    /**
     * Transform location.
     *
     * @param dir   the dir
     * @param steps the steps
     * @return the location
     */
    public Location transform(Direction dir, int steps) {
		return new Location(x + (dir.getStepX() * steps), y + (dir.getStepY() * steps), this.z);
	}

    /**
     * Transform location.
     *
     * @param l the l
     * @return the location
     */
    public Location transform(Location l) {
		return new Location(x + l.getX(), y + l.getY(), this.z + l.getZ());
	}

    /**
     * Transform location.
     *
     * @param diffX the diff x
     * @param diffY the diff y
     * @param z     the z
     * @return the location
     */
    public Location transform(int diffX, int diffY, int z) {
		return new Location(x + diffX, y + diffY, this.z + z);
	}

    /**
     * Within distance boolean.
     *
     * @param other the other
     * @return the boolean
     */
    public boolean withinDistance(Location other) {
		return withinDistance(other, MapDistance.RENDERING.getDistance());
	}

    /**
     * Within distance boolean.
     *
     * @param other the other
     * @param dist  the dist
     * @return the boolean
     */
    public boolean withinDistance(Location other, int dist) {
	    if (other.z != z) {
	        return false;
        }

		int a = (other.x - x);
		int b = (other.y - y);
		double product = Math.sqrt((a*a) + (b*b));
		return product <= dist;
	}

    /**
     * Within maxnorm distance boolean.
     *
     * @param other the other
     * @param dist  the dist
     * @return the boolean
     */
    public boolean withinMaxnormDistance(Location other, int dist) {
	    if (other.z != z) {
	        return false;
        }

		int a = Math.abs(other.x - x);
		int b = Math.abs(other.y - y);
		double max = Math.max(a, b);
		return max <= dist;
	}

    /**
     * Gets distance.
     *
     * @param other the other
     * @return the distance
     */
    public double getDistance(Location other) {
		int xdiff = this.getX() - other.getX();
		int ydiff = this.getY() - other.getY();
		return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}

    /**
     * Gets distance.
     *
     * @param first  the first
     * @param second the second
     * @return the distance
     */
    public static double getDistance(Location first, Location second) {
		int xdiff = first.getX() - second.getX();
		int ydiff = first.getY() - second.getY();
		return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}

    /**
     * Gets surrounding tiles.
     *
     * @return the surrounding tiles
     */
    public ArrayList<Location> getSurroundingTiles() {
		ArrayList<Location> locs = new ArrayList<>();

		locs.add(transform(-1,-1,0));//SW
		locs.add(transform(0,-1,0)); //S
		locs.add(transform(1,-1,0)); //SE
		locs.add(transform(1,0,0)); //E
		locs.add(transform(1,1,0)); //NE
		locs.add(transform(0,1,0)); //N
		locs.add(transform(-1,1,0));//NW
		locs.add(transform(-1,0,0));//W

		return locs;
	}

    /**
     * Gets cardinal tiles.
     *
     * @return the cardinal tiles
     */
    public ArrayList<Location> getCardinalTiles() {
		ArrayList<Location> locs = new ArrayList<>();

		locs.add(transform(-1, 0, 0));
		locs.add(transform(0, -1, 0));
		locs.add(transform(1, 0, 0));
		locs.add(transform(0, 1, 0));
		return locs;
	}

    /**
     * Gets 3 x 3 tiles.
     *
     * @return the 3 x 3 tiles
     */
    public ArrayList<Location> get3x3Tiles() {
		ArrayList<Location> locs = new ArrayList<>();
		locs.add(transform(0,0,0)); //Center
		locs.add(transform(0,1,0)); //N
		locs.add(transform(1,1,0)); //NE
		locs.add(transform(1,0,0)); //E
		locs.add(transform(1,-1,0)); //SE
		locs.add(transform(0,-1,0)); //S
		locs.add(transform(-1,-1,0));//SW
		locs.add(transform(-1,0,0));//W
		locs.add(transform(-1,1,0));//NW
		return locs;
	}

    /**
     * Gets chunk offset x.
     *
     * @return the chunk offset x
     */
    public int getChunkOffsetX() {
		int x = getLocalX();
		//return x - ((x / RegionChunk.SIZE) * RegionChunk.SIZE);
        return x & 7;
	}

    /**
     * Gets chunk offset y.
     *
     * @return the chunk offset y
     */
    public int getChunkOffsetY() {
		int y = getLocalY();
		//return y - ((y / RegionChunk.SIZE) * RegionChunk.SIZE);
        return y & 7;
	}

    /**
     * Gets chunk base.
     *
     * @return the chunk base
     */
    public Location getChunkBase() {
		return create(getRegionX() << 3, getRegionY() << 3, z);
	}

    /**
     * Gets region x.
     *
     * @return the region x
     */
    public int getRegionX() {
		return x >> 3;
	}

    /**
     * Gets region y.
     *
     * @return the region y
     */
    public int getRegionY() {
		return y >> 3;
	}

    /**
     * Gets local x.
     *
     * @return the local x
     */
    public int getLocalX() {
		return x & 63;
	}

    /**
     * Gets local y.
     *
     * @return the local y
     */
    public int getLocalY() {
		return y & 63;
	}

    /**
     * Gets scene x.
     *
     * @return the scene x
     */
    public int getSceneX() {
		return x - ((getRegionX() - 6) << 3);
	}

    /**
     * Gets scene y.
     *
     * @return the scene y
     */
    public int getSceneY() {
		return y - ((getRegionY() - 6) << 3);
	}

    /**
     * Gets scene x.
     *
     * @param loc the loc
     * @return the scene x
     */
    public int getSceneX(Location loc) {
		return x - ((loc.getRegionX() - 6) << 3);
	}

    /**
     * Gets scene y.
     *
     * @param loc the loc
     * @return the scene y
     */
    public int getSceneY(Location loc) {
		return y - ((loc.getRegionY() - 6) << 3);
	}

    /**
     * Gets chunk x.
     *
     * @return the chunk x
     */
    public int getChunkX() {
		return getLocalX() >> 3;
	}

    /**
     * Gets chunk y.
     *
     * @return the chunk y
     */
    public int getChunkY() {
		return getLocalY() >> 3;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Location)) {
			return false;
		}
		Location loc = (Location) other;
		return loc.x == x && loc.y == y && loc.z == z;
	}

    /**
     * Equals boolean.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the boolean
     */
    public boolean equals(int x, int y, int z) {
		return equals(new Location(x, y, z));
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}

    /**
     * From string location.
     *
     * @param locString the loc string
     * @return the location
     */
    public static Location fromString(String locString) {
		String trimmed = locString.replace("[", "").replace("]", "");
		String[] tokens = trimmed.split(",");
		return Location.create(
				Integer.parseInt(tokens[0].trim()),
				Integer.parseInt(tokens[1].trim()),
				Integer.parseInt(tokens[2].trim())
		);
	}

	@Override
	public int hashCode() {
		return z << 30 | x << 15 | y;
	}

    /**
     * Gets x.
     *
     * @return the x
     */
    public int getX() {
		return x;
	}

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(int x) {
		this.x = x;
	}

    /**
     * Gets y.
     *
     * @return the y
     */
    public int getY() {
		return y;
	}

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(int y) {
		this.y = y;
	}

    /**
     * Gets z.
     *
     * @return the z
     */
    public int getZ() {
		return z % 4;
	}

    /**
     * Sets z.
     *
     * @param z the z
     */
    public void setZ(int z) {
		this.z = z;
	}

    /**
     * Gets step components.
     *
     * @param dir the dir
     * @return the step components
     */
    @NotNull
	public List<Location> getStepComponents(Direction dir) {
		List<Location> output = new ArrayList<>(2);
		int stepX = dir.getStepX();
		int stepY = dir.getStepY();

		if (stepX != 0) output.add(transform(stepX, 0, 0));
		if (stepY != 0) output.add(transform(0, stepY, 0));
		return output;
	}

    /**
     * Derive direction direction.
     *
     * @param location the location
     * @return the direction
     */
    public Direction deriveDirection(Location location) {
		int diffX = location.x - this.x;
		int diffY = location.y - this.y;

		diffX = diffX >= 0 ? Math.min(diffX, 1) : -1;
		diffY = diffY >= 0 ? Math.min(diffY, 1) : -1;

		StringBuilder sb = new StringBuilder();

		if (diffY != 0) {
			if (diffY > 0) {
				sb.append("NORTH");
			} else {
				sb.append("SOUTH");
			}
		}

		if (diffX != 0) {
			if (sb.length() > 0) sb.append("_");
			if (diffX > 0) {
				sb.append("EAST");
			} else {
				sb.append("WEST");
			}
		}

		if (sb.length() == 0) return null;
		return Direction.valueOf(sb.toString());
	}

    /**
     * Transform location.
     *
     * @param vector the vector
     * @return the location
     */
    public Location transform (Vector vector) {
            return Location.create(this.x + (int) Math.floor(vector.getX()), this.y + (int) Math.floor(vector.getY()));
        }
}
