package core.game.node.entity.impl;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.GroundItem;
import core.game.node.item.Item;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.Point;
import core.game.world.map.RegionManager;
import core.tools.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import static core.api.ContentAPIKt.hasTimerActive;
import static core.api.ContentAPIKt.log;

/**
 * Handles walking queue.
 */
public final class WalkingQueue {

    /**
     * The walking queue of points the entity will traverse.
     */
    private final Deque<Point> walkingQueue = new ArrayDeque<Point>();

    /**
     * The entity whose movement is being managed.
     */
    private final Entity entity;

    /**
     * The direction of walking.
     */
    private int walkDir = -1;

    /**
     * The direction of running.
     */
    private int runDir = -1;

    /**
     * Whether the entity is currently running.
     */
    private boolean running = false;

    /**
     * Whether running is disabled.
     */
    private boolean runDisabled;

    /**
     * The location of the entity's footprint.
     */
    private Location footPrint;

    /**
     * The list of ground items that represent the path for the player to follow.
     */
    public ArrayList<GroundItem> routeItems = new ArrayList<GroundItem>();

    /**
     * Creates a new WalkingQueue for a given entity.
     *
     * @param entity The entity whose movement is being managed.
     */
    public WalkingQueue(Entity entity) {
        this.entity = entity;
        this.footPrint = entity.getLocation();
    }

    /**
     * Updates the movement of the entity based on the walking queue.
     * Handles both walking and running movements, and updates run energy if necessary.
     */
    public void update() {
        boolean isPlayer = entity instanceof Player;
        this.walkDir = -1;
        this.runDir = -1;
        if (entity.getLocation() == null) {
            return;
        }
        if (updateTeleport()) {
            return;
        }
        if (isPlayer && updateRegion(entity.getLocation(), true)) {
            return;
        }
        if (hasTimerActive(entity, "frozen")) return;
        Point point = walkingQueue.poll();
        boolean drawPath = entity.getAttribute("routedraw", false);
        if (point == null) {
            updateRunEnergy(false);
            if (isPlayer && drawPath) {
                for (GroundItem item : routeItems) {
                    if (item != null) {
                        RegionManager.getRegionPlane(item.getLocation()).remove(item);
                    }
                }
                routeItems.clear();
            }
            return;
        }
        if (isPlayer && ((Player) entity).getSettings().getRunEnergy() < 1.0) {
            running = false;
            ((Player) entity).getSettings().setRunToggled(false);
        }
        Point runPoint = null;
        if (point.getDirection() == null) {
            point = walkingQueue.poll();
        }
        int walkDirection = -1;
        int runDirection = -1;
        if (isRunningBoth() && (point == null || !point.isRunDisabled())) {
            runPoint = walkingQueue.poll();
        }
        if (point != null) {
            if (point.getDirection() == null) {
                return;
            }
            walkDirection = point.getDirection().ordinal();
        }
        if (runPoint != null) {
            runDirection = runPoint.getDirection().ordinal();
        }
        int diffX = 0;
        int diffY = 0;
        if (walkDirection != -1) {
            diffX = point.getDiffX();
            diffY = point.getDiffY();
        }
        if (runDirection != -1) {
            footPrint = entity.getLocation().transform(diffX, diffY, 0);
            diffX += runPoint.getDiffX();
            diffY += runPoint.getDiffY();
            updateRunEnergy(true);
        } else {
            updateRunEnergy(false);
        }
        if (diffX != 0 || diffY != 0) {
            Location walk = entity.getLocation();
            if (point != null) {
                walk = walk.transform(point.getDiffX(), point.getDiffY(), 0);
                if (!entity.getZoneMonitor().move(entity.getLocation(), walk)) {
                    reset();

                    return;
                }
            }
            Location dest = entity.getLocation().transform(diffX, diffY, 0);
            if (runPoint != null) {
                if (!entity.getZoneMonitor().move(walk, dest)) {
                    dest = dest.transform(-runPoint.getDiffX(), -runPoint.getDiffY(), 0);
                    runPoint = null;
                    runDirection = -1;
                    reset();

                }
            }
            if (runPoint != null) {
                entity.setDirection(runPoint.getDirection());
            } else if (point != null) {
                entity.setDirection(point.getDirection());
            }
            footPrint = entity.getLocation();
            entity.setLocation(dest);
            RegionManager.move(entity);
        }
        this.walkDir = walkDirection;
        this.runDir = runDirection;
    }

    /**
     * Returns the rate at which energy is drained for running, based on the player's weight and conditions.
     *
     * @param player The player whose energy drain rate is being calculated.
     * @return The energy drain rate.
     */
    private double getEnergyDrainRate(Player player) {
        double rate = 0.55;
        if (player.getSettings().getWeight() > 0.0) {
            rate *= 1 + (player.getSettings().getWeight() / 100);
        }
        if (hasTimerActive(player, "hamstrung")) {
            rate *= 4;
        }
        return rate;
    }

    /**
     * Returns the rate at which energy is restored for running, based on the player's agility.
     *
     * @param player The player whose energy restore rate is being calculated.
     * @return The energy restore rate.
     */
    private double getEnergyRestore(Player player) {
        double rate = 100 / ((175 - (player.getSkills().getLevel(Skills.AGILITY))) / 0.6);
        return rate;
    }

    /**
     * Updates the run energy of the player, either decreasing or restoring it.
     *
     * @param decrease Whether to decrease (true) or restore (false) the energy.
     */
    public void updateRunEnergy(boolean decrease) {
        if (!(entity instanceof Player)) {
            return;
        }
        Player p = (Player) entity;
        if (!decrease && p.getSettings().getRunEnergy() >= 100.0) {
            return;
        }
        double drain = decrease ? getEnergyDrainRate(p) : -getEnergyRestore(p);
        p.getSettings().updateRunEnergy(drain);
    }

    /**
     * Checks if the entity is in the process of teleporting, and if so, updates its location accordingly.
     *
     * @return True if the entity is teleporting, otherwise false.
     */
    public boolean updateTeleport() {
        if (entity.getProperties().getTeleportLocation() != null) {
            reset(false);
            entity.setLocation(entity.getProperties().getTeleportLocation());
            entity.getProperties().setTeleportLocation(null);
            if (entity instanceof Player) {
                Player p = (Player) entity;
                Location last = p.getPlayerFlags().getLastSceneGraph();
                if (last == null) {
                    last = p.getLocation();
                }
                if ((last.getRegionX() - entity.getLocation().getRegionX()) >= 4 || (last.getRegionX() - entity.getLocation().getRegionX()) <= -4) {
                    p.getPlayerFlags().setUpdateSceneGraph(true);
                } else if ((last.getRegionY() - entity.getLocation().getRegionY()) >= 4 || (last.getRegionY() - entity.getLocation().getRegionY()) <= -4) {
                    p.getPlayerFlags().setUpdateSceneGraph(true);
                }
            }
            RegionManager.move(entity);
            footPrint = entity.getLocation();
            entity.getProperties().setTeleporting(true);
            return true;
        }
        return false;
    }

    /**
     * Updates the region for the given location, checking whether a scene graph update is needed.
     *
     * @param location The location to check the region for.
     * @param move     Whether to move the entity to the new region.
     * @return True if a region update is needed, otherwise false.
     */
    public boolean updateRegion(Location location, boolean move) {
        Player p = (Player) entity;
        Location lastRegion = p.getPlayerFlags().getLastSceneGraph();
        if (lastRegion == null) {
            lastRegion = location;
        }
        int rx = lastRegion.getRegionX();
        int ry = lastRegion.getRegionY();
        int cx = location.getRegionX();
        int cy = location.getRegionY();
        if ((rx - cx) >= 4) {
            p.getPlayerFlags().setUpdateSceneGraph(true);
        } else if ((rx - cx) <= -4) {
            p.getPlayerFlags().setUpdateSceneGraph(true);
        }
        if ((ry - cy) >= 4) {
            p.getPlayerFlags().setUpdateSceneGraph(true);
        } else if ((ry - cy) <= -4) {
            p.getPlayerFlags().setUpdateSceneGraph(true);
        }
        if (move && p.getPlayerFlags().isUpdateSceneGraph()) {
            RegionManager.move(entity);
            return true;
        }
        return false;
    }

    /**
     * Moves the entity back to its footprint location.
     */
    public void walkBack() {
        entity.getPulseManager().clear();
        reset();
        addPath(footPrint.getX(), footPrint.getY());
    }

    /**
     * Adds a path to the walking queue based on the specified coordinates.
     *
     * @param x The x-coordinate to move to.
     * @param y The y-coordinate to move to.
     */
    public void addPath(int x, int y) {
        addPath(x, y, runDisabled);
    }

    /**
     * Adds a path to the walking queue with the option to disable running.
     *
     * @param x           The x-coordinate to move to.
     * @param y           The y-coordinate to move to.
     * @param runDisabled Whether running should be disabled for this path.
     */
    public void addPath(int x, int y, boolean runDisabled) {
        Point point = walkingQueue.peekLast();
        if (point == null) {
            return;
        }
        boolean drawRoute = entity.getAttribute("routedraw", false);
        if (drawRoute && entity instanceof Player) {
            Player p = (Player) entity;
            GroundItem item = new GroundItem(new Item(13444), Location.create(x, y, p.getLocation().getZ()), p);
            routeItems.add(item);
            RegionManager.getRegionPlane(item.getLocation()).add(item);
        }
        int diffX = x - point.getX(), diffY = y - point.getY();
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int i = 0; i < max; i++) {
            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++;
            } else if (diffY > 0) {
                diffY--;
            }
            addPoint(x - diffX, y - diffY, runDisabled);
        }
    }

    /**
     * Adds a point to the walking queue with the specified coordinates and run state.
     *
     * @param x           The x-coordinate of the point.
     * @param y           The y-coordinate of the point.
     * @param runDisabled Whether running should be disabled at this point.
     */
    public void addPoint(int x, int y, boolean runDisabled) {
        Point point = walkingQueue.peekLast();
        if (point == null) {
            return;
        }
        int diffX = x - point.getX(), diffY = y - point.getY();
        Direction direction = Direction.getDirection(diffX, diffY);
        if (direction != null) {
            walkingQueue.add(new Point(x, y, direction, diffX, diffY, runDisabled));
        }
    }

    /**
     * Determines if the entity is running both walk and run directions.
     *
     * @return True if the entity is running both walk and run directions, otherwise false.
     */
    public boolean isRunningBoth() {
        if (isRunDisabled()) return false;
        if (entity instanceof Player && ((Player) entity).getSettings().isRunToggled()) {
            return true;
        }
        return running;
    }

    /**
     * Checks if the entity has a path in the walking queue.
     *
     * @return True if the walking queue has a path, otherwise false.
     */
    public boolean hasPath() {
        return !walkingQueue.isEmpty();
    }

    /**
     * Checks if the entity is currently moving.
     *
     * @return True if the entity is moving, otherwise false.
     */
    public boolean isMoving() {
        return walkDir != -1 || runDir != -1;
    }

    /**
     * Resets the walking queue to the current location.
     */
    public void reset() {
        reset(running);
    }

    /**
     * Resets the walking queue to the current location, with the option to keep running enabled.
     *
     * @param running Whether the running state should be kept enabled.
     */
    public void reset(boolean running) {
        Location loc = entity.getLocation();

        if (loc == null) {
            log(this.getClass(), Log.ERR, "The entity location provided was null." + "Are you sure anything down the stack trace isn't providing an NPC with a null location?");
        }

        walkingQueue.clear();
        walkingQueue.add(new Point(loc.getX(), loc.getY()));
        this.running = running;
    }

    /**
     * Gets the walking direction for the entity.
     *
     * @return The walking direction.
     */
    public int getWalkDir() {
        return walkDir;
    }

    /**
     * Gets the running direction for the entity.
     *
     * @return The running direction.
     */
    public int getRunDir() {
        return runDir;
    }

    /**
     * Sets whether the entity is running.
     *
     * @param running The running state to set.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Checks if the entity is currently running.
     *
     * @return True if the entity is running, otherwise false.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Gets the footprint location of the entity.
     *
     * @return The footprint location.
     */
    public Location getFootPrint() {
        return footPrint;
    }

    /**
     * Sets the footprint location of the entity.
     *
     * @param footPrint The new footprint location to set.
     */
    public void setFootPrint(Location footPrint) {
        this.footPrint = footPrint;
    }

    /**
     * Gets the walking queue for the entity.
     *
     * @return The walking queue.
     */
    public Deque<Point> getQueue() {
        return walkingQueue;
    }

    /**
     * Checks if running is disabled for the entity.
     *
     * @return True if running is disabled, otherwise false.
     */
    public boolean isRunDisabled() {
        return runDisabled;
    }

    /**
     * Sets whether running is disabled for the entity.
     *
     * @param runDisabled The run-disabled state to set.
     */
    public void setRunDisabled(boolean runDisabled) {
        this.runDisabled = runDisabled;
    }
}
