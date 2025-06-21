package core.game.interaction;

import core.api.utils.Vector;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.impl.WalkingQueue;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.npc.NPCBehavior;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.Point;
import core.game.world.map.path.Path;
import core.game.world.map.path.Pathfinder;
import core.net.packet.OutgoingContext;
import core.net.packet.PacketRepository;
import core.net.packet.out.ClearMinimapFlag;
import kotlin.Pair;
import kotlin.jvm.functions.Function2;

import java.util.Deque;

import static core.api.ContentAPIKt.clearHintIcon;
import static core.api.ContentAPIKt.registerHintIcon;
import static core.api.movement.PathfindingAPIKt.truncateLoc;

/**
 * The type Movement pulse.
 */
public abstract class MovementPulse extends Pulse {

    /**
     * The Mover.
     */
    protected Entity mover;

    /**
     * The Destination.
     */
    protected Node destination;

    private Location last;

    private Pathfinder pathfinder;

    private boolean forceRun;

    private OptionHandler optionHandler;

    private UseWithHandler useHandler;

    private DestinationFlag destinationFlag;

    private Location interactLocation;

    private boolean near;

    private Function2<Entity, Node, Location> overrideMethod;

    private Location previousLoc;

    /**
     * Instantiates a new Movement pulse.
     *
     * @param mover       the mover
     * @param destination the destination
     */
    public MovementPulse(Entity mover, Node destination) {
        this(mover, destination, null, false);
    }

    /**
     * Instantiates a new Movement pulse.
     *
     * @param mover       the mover
     * @param destination the destination
     * @param forceRun    the force run
     */
    public MovementPulse(Entity mover, Node destination, boolean forceRun) {
        this(mover, destination, null, forceRun);
    }

    /**
     * Instantiates a new Movement pulse.
     *
     * @param mover       the mover
     * @param destination the destination
     * @param pathfinder  the pathfinder
     */
    public MovementPulse(Entity mover, Node destination, Pathfinder pathfinder) {
        this(mover, destination, pathfinder, false);
    }

    /**
     * Instantiates a new Movement pulse.
     *
     * @param mover         the mover
     * @param destination   the destination
     * @param optionHandler the option handler
     */
    public MovementPulse(Entity mover, Node destination, OptionHandler optionHandler) {
        this(mover, destination, null, false);
        this.optionHandler = optionHandler;
    }

    /**
     * Instantiates a new Movement pulse.
     *
     * @param mover       the mover
     * @param destination the destination
     * @param useHandler  the use handler
     */
    public MovementPulse(Entity mover, Node destination, UseWithHandler useHandler) {
        this(mover, destination, null, false);
        this.useHandler = useHandler;
    }

    /**
     * Instantiates a new Movement pulse.
     *
     * @param mover           the mover
     * @param destination     the destination
     * @param destinationFlag the destination flag
     */
    public MovementPulse(Entity mover, Node destination, DestinationFlag destinationFlag) {
        this(mover, destination, null, false);
        this.destinationFlag = destinationFlag;
    }

    /**
     * Instantiates a new Movement pulse.
     *
     * @param mover           the mover
     * @param destination     the destination
     * @param destinationFlag the destination flag
     * @param method          the method
     */
    public MovementPulse(Entity mover, Node destination, DestinationFlag destinationFlag, Function2<Entity, Node, Location> method) {
        this(mover, destination, null, false);
        this.destinationFlag = destinationFlag;
        this.overrideMethod = method;
    }

    /**
     * Instantiates a new Movement pulse.
     *
     * @param mover       the mover
     * @param destination the destination
     * @param pathfinder  the pathfinder
     * @param forceRun    the force run
     */
    public MovementPulse(Entity mover, Node destination, Pathfinder pathfinder, boolean forceRun) {
        super(1, mover, destination);
        this.mover = mover;
        this.destination = destination;
        if (pathfinder == null) {
            if (mover instanceof Player) {
                this.pathfinder = Pathfinder.SMART;
            } else if (mover instanceof NPC) {
                NPC npc = (NPC) mover;
                NPCBehavior behavior = npc.behavior;
                Pathfinder pf = behavior != null ? behavior.getPathfinderOverride(npc) : null;
                this.pathfinder = pf != null ? pf : Pathfinder.DUMB;
            } else {
                this.pathfinder = Pathfinder.DUMB;
            }
        } else {
            this.pathfinder = pathfinder;
        }
        this.forceRun = forceRun;

        if (destination instanceof NPC || destination instanceof Player)
            destinationFlag = DestinationFlag.ENTITY;

        if (mover.currentMovement != null) {
            mover.currentMovement.stop();
            mover.getWalkingQueue().reset();
        }
        mover.currentMovement = this;
    }

    private void clearInferiorScripts() {
        mover.scripts.removeWeakScripts();
        mover.scripts.removeNormalScripts();
    }

    @Override
    public boolean update() {
        if (!mover.getViewport().getRegion().isActive())
            return false;

        if (!isRunning()) return true;

        if (!validate()) {
            stop();
            return true;
        }

        clearInferiorScripts();

        mover.face(null);
        updatePath();

        if (tryInteract()) {
            stop();
            return true;
        }

        return false;
    }

    private boolean tryInteract() {
        Location ml = mover.getLocation();
        int radius = destination instanceof Entity && ((Entity) destination).getWalkingQueue().hasPath() ? 1 : 0;
        if (interactLocation == null)
            return false;
        if (Math.max(Math.abs(ml.getX() - interactLocation.getX()), Math.abs(ml.getY() - interactLocation.getY())) <= radius) {
            try {
                if (near || pulse()) {
                    if (mover instanceof Player) {
                        if (near) {
                            ((Player) mover).getPacketDispatch().sendMessage("I can't reach that.");
                        }
                        PacketRepository.send(ClearMinimapFlag.class, new OutgoingContext.PlayerContext((Player) mover));
                    }
                    stop();
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                stop();
            }
        }
        return false;
    }

    private boolean validate() {
        if (mover == null || destination == null || mover.getViewport().getRegion() == null || hasInactiveNode()) {
            return false;
        }
        return isRunning();
    }

    @Override
    public void stop() {
        super.stop();
        if (destination instanceof Entity) {
            mover.face(null);
        }
        last = null;
    }

    private boolean usingTruncatedPath = false;
    private boolean isMoveNearSet = false;

    /**
     * Update path.
     */
    public void updatePath() {
        if (mover instanceof NPC && mover.asNpc().isNeverWalks()) {
            return;
        }
        if (destination == null || destination.getLocation() == null) {
            return;
        }

        Location loc = null;

        if (optionHandler != null) {
            loc = optionHandler.getDestination(mover, destination);
        } else if (useHandler != null) {
            loc = useHandler.getDestination((Player) mover, destination);
        } else if (isInsideEntity(mover.getLocation())) {
            loc = findBorderLocation();
        }

        if (loc == null && destinationFlag != null && overrideMethod == null) {
            loc = destinationFlag.getDestination(mover, destination);
        } else if (loc == null && overrideMethod != null) {
            loc = overrideMethod.invoke(mover, destination);
            if (loc == destination.getLocation() && destinationFlag != null)
                loc = destinationFlag.getDestination(mover, destination);
            else if (loc == destination.getLocation()) loc = null;
        }

        if (destination instanceof NPC && mover.getProperties().getCombatPulse().getVictim() != destination)
            loc = checkForEntityPathInterrupt(loc != null ? loc : destination.getLocation());

        if (interactLocation == null)
            interactLocation = loc;
                                                                        //mover.getWalkingQueue().getQueue().size() <= 1
        if (destination instanceof Entity || interactLocation == null || (mover.getWalkingQueue().getQueue().size() <= 1 && interactLocation.getDistance(mover.getLocation()) > 0) || (usingTruncatedPath && destination.getLocation().getDistance(mover.getLocation()) < 14)) {            if (!checkAllowMovement())
                return;
            if (destination instanceof Entity && previousLoc != null && previousLoc.equals(loc) && mover.getWalkingQueue().hasPath())
                return;

            Path path;
            Pair<Boolean, Location> truncation = truncateLoc(mover, loc != null ? loc : destination.getLocation());
            if (truncation.getFirst()) {
                path = Pathfinder.find(mover, truncation.getSecond(), true, pathfinder);
                usingTruncatedPath = true;
            } else {
                path = Pathfinder.find(mover, loc != null ? loc : destination, true, pathfinder);
                interactLocation = null; //reset interactLocation so the below code can set it to the properly-pathfound last bit of path.
                usingTruncatedPath = false;
            }
            near = !path.isSuccessful() || path.isMoveNear();

            if (!path.getPoints().isEmpty()) {
                Point point = path.getPoints().getLast();
                if (forceRun) {
                    mover.getWalkingQueue().reset(forceRun);
                } else {
                    mover.getWalkingQueue().reset();
                }
                int size = path.getPoints().toArray().length;
                Deque points = path.getPoints();
                for (int i = 0; i < size; i++) {
                    point = path.getPoints().pop();
                    mover.getWalkingQueue().addPath(point.getX(), point.getY());
                    if (destination instanceof Entity) {
                        mover.face((Entity) destination);
                    } else {
                        mover.face(null);
                    }

                    if (i == size - 1 && interactLocation == null)
                        interactLocation = Location.create(point.getX(), point.getY(), mover.getLocation().getZ());
                }
            }
            previousLoc = loc;
        }
        last = destination.getLocation();
        if (mover instanceof Player && mover.getAttribute("draw-intersect", false)) {
            clearHintIcon((Player) mover);
            registerHintIcon((Player) mover, interactLocation, 5);
        }
    }

    private boolean checkAllowMovement() {
        boolean canMove = true;
        if (destination instanceof Entity) {
            Entity e = (Entity) destination;
            Location l = e.getLocation();
            Deque<Point> npcPath = e.getWalkingQueue().getQueue();
            if (e.getWalkingQueue().hasPath() && e.getProperties().getCombatPulse().isRunning() && e.getProperties().getCombatPulse().getVictim() == mover)
                canMove = false;
            if (!canMove) { //If we normally shouldn't move, but the NPC's pathfinding is not letting them move, then move.
                if (npcPath.size() == 1) {
                    Point pathElement = npcPath.peek();
                    if (pathElement.getX() == l.getX() && pathElement.getY() == l.getY())
                        canMove = true;
                }
            }
        }
        return canMove;
    }

    private Location checkForEntityPathInterrupt(Location loc) {
        Location ml = mover.getLocation();
        Location dl = destination.getLocation();
        // Lead the target if they're walking/running, unless they're already within interaction range
        if (loc != null && destination instanceof Entity) {
            WalkingQueue wq = ((Entity) destination).getWalkingQueue();
            if (wq.hasPath()) {
                Point[] points = wq.getQueue().toArray(new Point[0]);
                if (points.length > 0) {
                    Point p = points[0];
                    Point predictiveIntersection = null;
                    for (int i = 0; i < points.length; i++) {
                        Location closestBorder = getClosestBorderToPoint(points[i], loc.getZ());

                        int moverDist = Math.max(Math.abs(ml.getX() - closestBorder.getX()), Math.abs(ml.getY() - closestBorder.getY()));
                        float movementRatio = moverDist / (float) ((i + 1) / (mover.getWalkingQueue().isRunning() ? 2 : 1));
                        if (predictiveIntersection == null && movementRatio <= 1.0) { //try to predict an intersection point on the path if possible
                            predictiveIntersection = points[i];
                            break;
                        }

                        // Otherwise, we target the farthest point along target's planned movement that's within 1 tick's running,
                        // this ensures the player will run to catch up to the target if able.
                        if (moverDist <= 2) {
                            p = points[i];
                        }
                    }

                    if (predictiveIntersection != null)
                        p = predictiveIntersection;

                    Location endLoc = getClosestBorderToPoint(p, loc.getZ());
                    return endLoc;
                }
            }
        }
        return loc;
    }

    private Location getClosestBorderToPoint(Point p, int plane) {
        Vector pathDiff = Vector.betweenLocs(destination.getLocation(), Location.create(p.getX(), p.getY(), plane));
        Location predictedCenterPos = (destination.getMathematicalCenter().plus(pathDiff)).toLocation(plane);
        Vector toPlayerNormalized = Vector.betweenLocs(predictedCenterPos, mover.getCenterLocation()).normalized();
        return predictedCenterPos.transform(toPlayerNormalized.times(destination.size() + 1));
    }

    private Location findBorderLocation() {
        return findBorderLocation(destination.getLocation());
    }

    private Location findBorderLocation(Location centerDestLoc) {
        int size = destination.size();
        Location centerDest = centerDestLoc.transform(size >> 1, size >> 1, 0);
        Location center = mover.getLocation().transform(mover.size() >> 1, mover.size() >> 1, 0);
        Direction direction = Direction.getLogicalDirection(centerDest, center);
        Location delta = Location.getDelta(centerDestLoc, mover.getLocation());
        main:
        for (int i = 0; i < 4; i++) {
            int amount = 0;
            switch (direction) {
                case NORTH:
                    amount = size - delta.getY();
                    break;
                case EAST:
                    amount = size - delta.getX();
                    break;
                case SOUTH:
                    amount = mover.size() + delta.getY();
                    break;
                case WEST:
                    amount = mover.size() + delta.getX();
                    break;
                default:
                    return null;
            }
            for (int j = 0; j < amount; j++) {
                for (int s = 0; s < mover.size(); s++) {
                    switch (direction) {
                        case NORTH:
                            if (!direction.canMove(mover.getLocation().transform(s, j + mover.size(), 0))) {
                                direction = Direction.get((direction.toInteger() + 1) & 3);
                                continue main;
                            }
                            break;
                        case EAST:
                            if (!direction.canMove(mover.getLocation().transform(j + mover.size(), s, 0))) {
                                direction = Direction.get((direction.toInteger() + 1) & 3);
                                continue main;
                            }
                            break;
                        case SOUTH:
                            if (!direction.canMove(mover.getLocation().transform(s, -(j + 1), 0))) {
                                direction = Direction.get((direction.toInteger() + 1) & 3);
                                continue main;
                            }
                            break;
                        case WEST:
                            if (!direction.canMove(mover.getLocation().transform(-(j + 1), s, 0))) {
                                direction = Direction.get((direction.toInteger() + 1) & 3);
                                continue main;
                            }
                            break;
                        default:
                            return null;
                    }
                }
            }
            Location location = mover.getLocation().transform(direction, amount);
            return location;
        }
        return null;
    }

    private boolean isInsideEntity(Location l) {
        if (!(destination instanceof Entity)) {
            return false;
        }
        if (((Entity) destination).getWalkingQueue().isMoving()) {
            return false;
        }
        Location loc = destination.getLocation();
        int size = destination.size();
        return Pathfinder.isStandingIn(l.getX(), l.getY(), mover.size(), mover.size(), loc.getX(), loc.getY(), size, size);
    }

    /**
     * Is force run boolean.
     *
     * @return the boolean
     */
    public boolean isForceRun() {
        return forceRun;
    }

    /**
     * Sets force run.
     *
     * @param forceRun the force run
     */
    public void setForceRun(boolean forceRun) {
        this.forceRun = forceRun;
    }

    /**
     * Sets destination.
     *
     * @param destination the destination
     */
    public void setDestination(Node destination) {
        this.destination = destination;
    }

    /**
     * Sets last.
     *
     * @param last the last
     */
    public void setLast(Location last) {
        this.last = last;
    }

}
