package core.game.node.entity.impl;

import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.update.flag.EntityFlag;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.ForceMoveCtx;

/**
 * This class represents a force movement action for an entity in the game world.
 * It handles the animation, pathfinding, speed, and movement directions for an entity during forced movement.
 * The movement consists of an initial animation, followed by the entity moving to a destination with a specified speed.
 */
public class ForceMovement extends Pulse {

    /**
     * Constant speed for walking.
     */
    public static final int WALKING_SPEED = 10;

    /**
     * Constant speed for running.
     */
    public static final int RUNNING_SPEED = 20;

    /**
     * Animation for walking.
     */
    public static final Animation WALK_ANIMATION = Animation.create(819);

    /**
     * The entity that is being moved.
     */
    protected Entity entity;

    /** The starting location of the movement. */
    private Location start;

    /** The destination location of the movement. */
    private Location destination;

    /** The animation played at the start of the movement. */
    private Animation startAnim;

    /**
     * The animation applied while the entity is moving.
     */
    protected Animation animation;

    /** The animation played at the end of the movement. */
    private Animation endAnimation = null;

    /**
     * The direction of the movement.
     */
    protected Direction direction;

    /** The speed at which the movement commences. */
    private int commenceSpeed;

    /** The speed at which the entity moves along the path. */
    private int pathSpeed;

    /** Flag to determine if the entity should be unlocked after the movement. */
    private boolean unlockAfter;

    /**
     * Constructs a new ForceMovement object with specified parameters for forced movement.
     *
     * @param e             The entity being moved.
     * @param start         The starting location of the movement.
     * @param destination   The destination location of the movement.
     * @param startAnim     The animation to play at the start of the movement.
     * @param animation     The animation applied while moving.
     * @param direction     The direction of movement.
     * @param commenceSpeed The speed at which movement commences.
     * @param pathSpeed     The speed at which the entity moves along the path.
     * @param unlockAfter   Flag to unlock the entity after movement.
     */
    @Deprecated
    public ForceMovement(Entity e, Location start, Location destination, Animation startAnim, Animation animation, Direction direction, int commenceSpeed, int pathSpeed, boolean unlockAfter) {
        super(1, e);
        this.entity = e;
        this.start = start;
        this.destination = destination;
        this.startAnim = startAnim;
        this.animation = animation;
        this.direction = direction;
        this.commenceSpeed = commenceSpeed;
        this.pathSpeed = pathSpeed;
        this.unlockAfter = unlockAfter;
    }

    /**
     * Instantiates a new Force movement.
     *
     * @param e         the e
     * @param start     the start
     * @param end       the end
     * @param animation the animation
     * @param speed     the speed
     */
    @Deprecated
    public ForceMovement(Entity e, Location start, Location end, Animation animation, int speed) {
        this(e, start, end, WALK_ANIMATION, animation, direction(start, end), WALKING_SPEED, speed, true);
    }

    /**
     * Instantiates a new Force movement.
     *
     * @param e           the e
     * @param destination the destination
     * @param startSpeed  the start speed
     * @param animSpeed   the anim speed
     */
    @Deprecated
    public ForceMovement(Entity e, Location destination, int startSpeed, int animSpeed) {
        this(e, e.getLocation(), destination, WALK_ANIMATION, WALK_ANIMATION, direction(e.getLocation(), destination), startSpeed, animSpeed, true);
    }

    /**
     * Instantiates a new Force movement.
     *
     * @param e           the e
     * @param start       the start
     * @param destination the destination
     * @param animation   the animation
     */
    @Deprecated
    public ForceMovement(Entity e, Location start, Location destination, Animation animation) {
        this(e, start, destination, WALK_ANIMATION, animation, direction(start, destination), WALKING_SPEED, WALKING_SPEED, true);
    }

    /**
     * Instantiates a new Force movement.
     *
     * @param start       the start
     * @param destination the destination
     * @param animation   the animation
     */
    @Deprecated
    public ForceMovement(Location start, Location destination, Animation animation) {
        this(null, start, destination, WALK_ANIMATION, animation, direction(start, destination), WALKING_SPEED, WALKING_SPEED, true);
    }

    /**
     * Run force movement.
     *
     * @param e           the e
     * @param destination the destination
     * @return the force movement
     */
    @Deprecated
    public static ForceMovement run(Entity e, Location destination) {
        return run(e, e.getLocation(), destination, WALK_ANIMATION, WALK_ANIMATION, direction(e.getLocation(), destination), WALKING_SPEED, WALKING_SPEED, true);
    }

    /**
     * Run force movement.
     *
     * @param e           the e
     * @param start       the start
     * @param destination the destination
     * @return the force movement
     */
    @Deprecated
    public static ForceMovement run(Entity e, Location start, Location destination) {
        return run(e, start, destination, WALK_ANIMATION, WALK_ANIMATION, direction(e.getLocation(), destination), WALKING_SPEED, WALKING_SPEED, true);
    }

    /**
     * Run force movement.
     *
     * @param e           the e
     * @param start       the start
     * @param destination the destination
     * @param animation   the animation
     * @return the force movement
     */
    @Deprecated
    public static ForceMovement run(Entity e, Location start, Location destination, Animation animation) {
        return run(e, start, destination, WALK_ANIMATION, animation, direction(start, destination), WALKING_SPEED, WALKING_SPEED, true);
    }

    /**
     * Run force movement.
     *
     * @param e           the e
     * @param start       the start
     * @param destination the destination
     * @param animation   the animation
     * @param speed       the speed
     * @return the force movement
     */
    @Deprecated
    public static ForceMovement run(Entity e, Location start, Location destination, Animation animation, int speed) {
        return run(e, start, destination, WALK_ANIMATION, animation, direction(start, destination), WALKING_SPEED, speed, true);
    }

    /**
     * Run force movement.
     *
     * @param e           the e
     * @param start       the start
     * @param destination the destination
     * @param startAnim   the start anim
     * @param animation   the animation
     * @return the force movement
     */
    @Deprecated
    public static ForceMovement run(Entity e, Location start, Location destination, Animation startAnim, Animation animation) {
        return run(e, start, destination, startAnim, animation, direction(start, destination), WALKING_SPEED, WALKING_SPEED, true);
    }

    /**
     * Run force movement.
     *
     * @param e           the e
     * @param start       the start
     * @param destination the destination
     * @param startAnim   the start anim
     * @param animation   the animation
     * @param direction   the direction
     * @return the force movement
     */
    @Deprecated
    public static ForceMovement run(Entity e, Location start, Location destination, Animation startAnim, Animation animation, Direction direction) {
        return run(e, start, destination, startAnim, animation, direction, WALKING_SPEED, WALKING_SPEED, true);
    }

    /**
     * Run force movement.
     *
     * @param e           the e
     * @param start       the start
     * @param destination the destination
     * @param startAnim   the start anim
     * @param animation   the animation
     * @param direction   the direction
     * @param pathSpeed   the path speed
     * @return the force movement
     */
    @Deprecated
    public static ForceMovement run(Entity e, Location start, Location destination, Animation startAnim, Animation animation, Direction direction, int pathSpeed) {
        return run(e, start, destination, startAnim, animation, direction, WALKING_SPEED, pathSpeed, true);
    }

    /**
     * Run force movement.
     *
     * @param e             the e
     * @param start         the start
     * @param destination   the destination
     * @param startAnim     the start anim
     * @param animation     the animation
     * @param direction     the direction
     * @param commenceSpeed the commence speed
     * @param pathSpeed     the path speed
     * @return the force movement
     */
    @Deprecated
    public static ForceMovement run(Entity e, Location start, Location destination, Animation startAnim, Animation animation, Direction direction, int commenceSpeed, int pathSpeed) {
        return run(e, start, destination, startAnim, animation, direction, commenceSpeed, pathSpeed, true);
    }

    /**
     * Run force movement.
     *
     * @param e             the e
     * @param start         the start
     * @param destination   the destination
     * @param startAnim     the start anim
     * @param animation     the animation
     * @param direction     the direction
     * @param commenceSpeed the commence speed
     * @param pathSpeed     the path speed
     * @param unlockAfter   the unlock after
     * @return the force movement
     */
    @Deprecated
    public static ForceMovement run(Entity e, Location start, Location destination, Animation startAnim, Animation animation, Direction direction, int commenceSpeed, int pathSpeed, boolean unlockAfter) {
        if (startAnim != null) {
            startAnim.setPriority(Animator.Priority.VERY_HIGH);
        }
        if (animation != null) {
            animation.setPriority(Animator.Priority.VERY_HIGH);
        }
        ForceMovement fm = new ForceMovement(e, start, destination, startAnim, animation, direction, commenceSpeed, pathSpeed, unlockAfter);
        fm.start();
        e.lock();
        GameWorld.getPulser().submit(fm);
        return fm;
    }

    /**
     * Run force movement.
     *
     * @param e             the e
     * @param destination   the destination
     * @param commenceSpeed the commence speed
     * @param pathSpeed     the path speed
     * @return the force movement
     */
    @Deprecated
    public static ForceMovement run(Entity e, Location destination, int commenceSpeed, int pathSpeed) {
        return run(e, e.getLocation(), destination, WALK_ANIMATION, WALK_ANIMATION, direction(e.getLocation(), destination), commenceSpeed, pathSpeed, true);
    }

    /**
     * Run.
     *
     * @param e     the e
     * @param speed the speed
     */
    @Deprecated
    public void run(final Entity e, final int speed) {
        this.entity = e;
        int commence = (int) start.getDistance(e.getLocation());
        if (commence != 0 && commenceSpeed != 0) {
            commence = (int) (1 + (commence / (commenceSpeed * 0.1)));
        }
        int path = 1 + (int) Math.ceil(start.getDistance(destination) / (pathSpeed * 0.1));
        this.pathSpeed = pathSpeed == 0 ? path : speed;
        this.commenceSpeed = commence;
        start();
        e.lock();
        GameWorld.getPulser().submit(this);
    }

    /**
     * Run.
     *
     * @param e the e
     */
    public void run(final Entity e) {
        run(e, 0);
    }

    public void run() {
        run(entity);
    }

    /**
     * Direction direction.
     *
     * @param s the s
     * @param d the d
     * @return the direction
     */
    public static Direction direction(Location s, Location d) {
        Location delta = Location.getDelta(s, d);
        int x = Math.abs(delta.getX());
        int y = Math.abs(delta.getY());
        if (x > y) {
            return Direction.getDirection(delta.getX(), 0);
        }
        return Direction.getDirection(0, delta.getY());
    }

    /**
     * Starts the force movement with the given parameters.
     */
    @Override
    public void start() {
        commenceSpeed = (int) Math.ceil(start.getDistance(entity.getLocation()) / (commenceSpeed * 0.1));
        pathSpeed = (int) Math.ceil(start.getDistance(destination) / (pathSpeed * 0.1));
        if (commenceSpeed != 0) {
            entity.animate(startAnim);
            super.setDelay(commenceSpeed);
        } else {
            entity.animate(animation);
            super.setDelay(pathSpeed);
        }
        int ticks = 1 + commenceSpeed + pathSpeed;
        entity.getImpactHandler().setDisabledTicks(ticks);
        entity.getUpdateMasks().register(EntityFlag.ForceMove, new ForceMoveCtx(start, destination, commenceSpeed * 30, pathSpeed * 30, direction));
        if (entity instanceof Player) {
            entity.getWalkingQueue().updateRegion(destination, false);
        }
        super.start();
    }

    /**
     * Called on each pulse of the force movement to update the animation and movement.
     *
     * @return False if movement is not completed, true otherwise.
     */
    @Override
    public boolean pulse() {
        if (commenceSpeed != 0) {
            entity.animate(animation);
            setDelay(pathSpeed);
            commenceSpeed = 0;
            entity.getProperties().setTeleportLocation(start);
            return false;
        }
        return true;
    }

    /**
     * Stops the force movement and applies the end animation and destination teleportation.
     */
    @Override
    public void stop() {
        super.stop();
        entity.getProperties().setTeleportLocation(destination);
        if (endAnimation != null) {
            entity.animate(endAnimation);
        }
        if (unlockAfter) entity.unlock();
    }

    /**
     * Gets the start location of the movement.
     *
     * @return The start location.
     */
    public Location getStart() {
        return start;
    }

    /**
     * Sets the start location of the movement.
     *
     * @param start The new start location.
     */
    public void setStart(Location start) {
        this.start = start;
    }

    /**
     * Gets the destination location of the movement.
     *
     * @return The destination location.
     */
    public Location getDestination() {
        return destination;
    }

    /**
     * Sets the destination location of the movement.
     *
     * @param destination The new destination location.
     */
    public void setDestination(Location destination) {
        this.destination = destination;
    }

    /**
     * Gets the direction of the movement.
     *
     * @return The direction of the movement.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the direction of the movement.
     *
     * @param direction The new direction of the movement.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Gets the commence speed of the movement.
     *
     * @return The commence speed.
     */
    public int getCommenceSpeed() {
        return commenceSpeed;
    }

    /**
     * Sets the commence speed of the movement.
     *
     * @param commenceSpeed The new commence speed.
     */
    public void setCommenceSpeed(int commenceSpeed) {
        this.commenceSpeed = commenceSpeed;
    }

    /**
     * Sets entity.
     *
     * @param entity the entity
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Gets the path speed of the movement.
     *
     * @return The path speed.
     */
    public int getPathSpeed() {
        return pathSpeed;
    }

    /**
     * Sets the path speed of the movement.
     *
     * @param pathSpeed The new path speed.
     */
    public void setPathSpeed(int pathSpeed) {
        this.pathSpeed = pathSpeed;
    }

    /**
     * Gets the entity being moved.
     *
     * @return The entity.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Gets the start animation for the movement.
     *
     * @return The start animation.
     */
    public Animation getStartAnim() {
        return startAnim;
    }

    /**
     * Sets the start animation for the movement.
     *
     * @param startAnim The new start animation.
     */
    public void setStartAnim(Animation startAnim) {
        this.startAnim = startAnim;
    }

    /**
     * Gets the animation applied during the movement.
     *
     * @return The movement animation.
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * Sets the animation applied during the movement.
     *
     * @param animation The new movement animation.
     */
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    /**
     * Gets the end animation for the movement.
     *
     * @return The end animation.
     */
    public Animation getEndAnimation() {
        return endAnimation;
    }

    /**
     * Sets the end animation for the movement.
     *
     * @param endAnimation The new end animation.
     */
    public void setEndAnimation(Animation endAnimation) {
        this.endAnimation = endAnimation;
    }
}
