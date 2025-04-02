package core.game.world.map.path;

import core.game.node.entity.Entity;
import core.game.world.map.Point;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The type Path.
 */
public class Path {

    private boolean successful;

    private boolean moveNear;

    private Deque<Point> points = new ArrayDeque<Point>();

    /**
     * Instantiates a new Path.
     */
    public Path() {
        // Empty
    }

    /**
     * Walk.
     *
     * @param entity the entity
     */
    public void walk(Entity entity) {
        if (entity.getLocks().isMovementLocked()) {
            return;
        }
        entity.getWalkingQueue().reset();
        for (Point step : points) {
            entity.getWalkingQueue().addPath(step.getX(), step.getY());
        }
    }

    /**
     * Is successful boolean.
     *
     * @return the boolean
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Sets successful.
     *
     * @param successful the successful
     */
    public void setSuccessful(boolean successful) {
        this.successful = successful;
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
