package core.game.world.map;

import core.game.world.map.path.ClipMaskSupplier;

/**
 * The enum Direction.
 */
public enum Direction {
	/**
	 * North west direction.
	 */
	NORTH_WEST(-1, 1, 7, 0x12c0108, 0x12c0120, 0x12c0138),

	/**
	 * North direction.
	 */
	NORTH(0, 1, 0, 0x12c0120),

	/**
	 * North east direction.
	 */
	NORTH_EAST(1, 1, 4, 0x12c0180, 0x12c0120, 0x12c01e0),

	/**
	 * West direction.
	 */
	WEST(-1, 0, 3, 0x12c0108),

	/**
	 * East direction.
	 */
	EAST(1, 0, 1, 0x12c0180),

	/**
	 * South west direction.
	 */
	SOUTH_WEST(-1, -1, 6, 0x12c0108, 0x12c0102, 0x12c010e),

	/**
	 * South direction.
	 */
	SOUTH(0, -1, 2, 0x12c0102),

	/**
	 * South east direction.
	 */
	SOUTH_EAST(1, -1, 5, 0x12c0180, 0x12c0102, 0x12c0183);

	private final int stepX;

	private final int stepY;

	private final int value;

	private int[] traversal;

	Direction(int stepX, int stepY, int value, int... traversal) {
		this.stepX = stepX;
		this.stepY = stepY;
		this.value = value;
		this.setTraversal(traversal);
	}

	/**
	 * Get direction.
	 *
	 * @param rotation the rotation
	 * @return the direction
	 */
	public static Direction get(int rotation) {
		for (Direction dir : Direction.values()) {
			if (dir.value == rotation) {
				return dir;
			}
		}
		throw new IllegalArgumentException("Invalid direction value - " + rotation);
	}

	/**
	 * Gets walk point.
	 *
	 * @param direction the direction
	 * @return the walk point
	 */
	public static Point getWalkPoint(Direction direction) {
		return new Point(direction.stepX, direction.stepY);
	}

	/**
	 * Gets direction.
	 *
	 * @param location the location
	 * @param l        the l
	 * @return the direction
	 */
	public static Direction getDirection(Location location, Location l) {
		return getDirection(l.getX() - location.getX(), l.getY() - location.getY());
	}

	/**
	 * Gets direction.
	 *
	 * @param diffX the diff x
	 * @param diffY the diff y
	 * @return the direction
	 */
	public static Direction getDirection(int diffX, int diffY) {
		if (diffX < 0) {
			if (diffY < 0) {
				return SOUTH_WEST;
			} else if (diffY > 0) {
				return NORTH_WEST;
			}
			return WEST;
		} else if (diffX > 0) {
			if (diffY < 0) {
				return SOUTH_EAST;
			} else if (diffY > 0) {
				return NORTH_EAST;
			}
			return EAST;
		}
		if (diffY < 0) {
			return SOUTH;
		}
		return NORTH;
	}

	/**
	 * For walk flag direction.
	 *
	 * @param walkingFlag the walking flag
	 * @param rotation    the rotation
	 * @return the direction
	 */
	public static Direction forWalkFlag(int walkingFlag, int rotation) {
		if (rotation != 0) {
			walkingFlag = (walkingFlag << rotation & 0xf) + (walkingFlag >> 4 - rotation);
		}
		if (walkingFlag > 0) {
			if ((walkingFlag & 0x8) == 0) {
				return Direction.WEST;
			}
			if ((walkingFlag & 0x2) == 0) {
				return Direction.EAST;
			}
			if ((walkingFlag & 0x4) == 0) {
				return Direction.SOUTH;
			}
			if ((walkingFlag & 0x1) == 0) {
				return Direction.NORTH;
			}
		}
		return null;
	}

	/**
	 * Gets opposite.
	 *
	 * @return the opposite
	 */
	public Direction getOpposite() {
		return Direction.get(toInteger() + 2 & 3);
	}

	/**
	 * Gets logical direction.
	 *
	 * @param location the location
	 * @param l        the l
	 * @return the logical direction
	 */
	public static Direction getLogicalDirection(Location location, Location l) {
		int offsetX = Math.abs(l.getX() - location.getX());
		int offsetY = Math.abs(l.getY() - location.getY());
		if (offsetX > offsetY) {
			if (l.getX() > location.getX()) {
				return Direction.EAST;
			} else {
				return Direction.WEST;
			}
		} else if (l.getY() < location.getY()) {
			return Direction.SOUTH;
		}
		return Direction.NORTH;
	}

	/**
	 * To name string.
	 *
	 * @param direction the direction
	 * @return the string
	 */
	public String toName(Direction direction) {
		return direction.name().toLowerCase();
	}

	/**
	 * To integer int.
	 *
	 * @return the int
	 */
	public int toInteger() {
		return value;
	}

	/**
	 * Gets step x.
	 *
	 * @return the step x
	 */
	public int getStepX() {
		return stepX;
	}

	/**
	 * Gets step y.
	 *
	 * @return the step y
	 */
	public int getStepY() {
		return stepY;
	}

	/**
	 * Can move boolean.
	 *
	 * @param l the l
	 * @return the boolean
	 */
	public boolean canMove(Location l) {
		int flag = RegionManager.getClippingFlag(l.getZ(), l.getX(), l.getY());
		for (int f : traversal) {
			if ((flag & f) != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Can move from boolean.
	 *
	 * @param z                the z
	 * @param x                the x
	 * @param y                the y
	 * @param clipMaskSupplier the clip mask supplier
	 * @return the boolean
	 */
	public boolean canMoveFrom(int z, int x, int y, ClipMaskSupplier clipMaskSupplier) {
        int dx, dy;
        boolean ret = true;
		for (int f : traversal) {
            switch(f) {
                case 0x12c0120: dx = 0; dy = 1; break; // north
                case 0x12c0180: dx = 1; dy = 0; break; // east
                case 0x12c01e0: dx = 1; dy = 1; break; // northeast
                case 0x12c0102: dx = 0; dy = -1; break; // south
                case 0x12c0183: dx = 1; dy = -1; break; // southeast
                case 0x12c0108: dx = -1; dy = 0; break; // west
                case 0x12c010e: dx = -1; dy = -1; break; // southwest
                case 0x12c0138: dx = -1; dy = 1; break; // northwest
                default: return false;
            }
            int flag = clipMaskSupplier.getClippingFlag(z, x+dx, y+dy);
			if ((flag & f) != 0) {
				ret = false;
			}
		}
		return ret;
	}

	private void setTraversal(int[] traversal) {
		this.traversal = traversal;
	}
}
