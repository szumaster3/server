package core.game.world.map.path;

import core.game.node.entity.Entity;
import core.game.world.map.Point;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The type Path.
 */
public class Path {

    private boolean succesful;

    private boolean moveNear;

    private Deque<Point> points = new ArrayDeque<Point>();

    /**
     * Instantiates a new Path.
     */
    public Path() {
        // Empty constructor to initialize the path object
    }

    /**
     * Walk.
     *
     * @param entity the entity
     */
    public void walk(Entity entity) {
        if (entity.getLocks().isMovementLocked()) {
            return; // If the entity is locked, it cannot move
        }
        entity.getWalkingQueue().reset(); // Reset walking queue before adding new steps
        for (Point step : points) {
            entity.getWalkingQueue().addPath(step.getX(), step.getY()); // Add each step to the walking queue
        }
    }

    /**
     * Is successful boolean.
     *
     * @return the boolean
     */
    public boolean isSuccessful() {
        return succesful;
    }

    /**
     * Sets successful.
     *
     * @param successful the successful
     */
    public void setSuccessful(boolean successful) {
        this.succesful = successful;
    }

    /**
     * Gets points.
     *
     * @return the points
     */
    public Deque<Point> getPoints() {
        return points;
    }

    /**
     * Sets points.
     *
     * @param points the points
     */
    public void setPoints(Deque<Point> points) {
        this.points = points;
    }

    /**
     * Is move near boolean.
     *
     * @return the boolean
     */
    public boolean isMoveNear() {
        return moveNear;
    }

    /**
     * Sets move near.
     *
     * @param moveNear the move near
     */
    public void setMoveNear(boolean moveNear) {
        this.moveNear = moveNear;
    }
}
