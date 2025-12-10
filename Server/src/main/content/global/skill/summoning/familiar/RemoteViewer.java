package content.global.skill.summoning.familiar;

import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.net.packet.PacketRepository;
import core.net.packet.context.CameraContext;
import core.net.packet.out.CameraViewPacket;

/**
 * Handles the remote viewing functionality for a player's familiar.
 */
public final class RemoteViewer {

    /**
     * The dialogue name used to open the remote viewer interface.
     */
    public static final String DIALOGUE_NAME = "remote-view";

    /**
     * The fixed camera height used for viewing.
     */
    public static final int HEIGHT = 1000;

    /**
     * The player using the remote viewer.
     */
    private final Player player;

    /**
     * The familiar being sent to view remotely.
     */
    private final Familiar familiar;

    /**
     * The animation performed by the familiar when viewing.
     */
    private final Animation animation;

    /**
     * The type of view (direction or straight up).
     */
    private final ViewType type;

    /**
     * Constructs a new RemoteViewer instance.
     * @param player the player initiating the remote view.
     * @param familiar the familiar to be sent.
     * @param animation the animation to play for the familiar.
     * @param type the direction or type of view.
     */
    public RemoteViewer(Player player, Familiar familiar, Animation animation, ViewType type) {
        this.player = player;
        this.familiar = familiar;
        this.animation = animation;
        this.type = type;
    }

    /**
     * Creates a new RemoteViewer instance.
     * @param player the player initiating the remote view.
     * @param familiar the familiar to be sent.
     * @param animation the animation to play for the familiar.
     * @param type the direction or type of view.
     * @return a new RemoteViewer instance.
     */
    public static RemoteViewer create(final Player player, Familiar familiar, Animation animation, ViewType type) {
        return new RemoteViewer(player, familiar, animation, type);
    }

    /**
     * Starts the remote viewing process.
     */
    public void startViewing() {
        player.lock();
        familiar.animate(animation);
        player.getPacketDispatch().sendMessage("You send the " + familiar.getName().toLowerCase() + " to fly " +
                (type == ViewType.STRAIGHT_UP ? "directly up" : "to the " + type.name().toLowerCase()) + "...");

        GameWorld.getPulser().submit(new Pulse(5) {
            @Override
            public boolean pulse() {
                view();
                return true;
            }
        });
    }

    /**
     * Handles the camera view logic and schedules a reset pulse.
     */
    private void view() {
        if (!canView()) {
            return;
        }
        sendCamera(type.getXOffset(), type.getYOffset(), type.getXRot(), type.getYRot());
        GameWorld.getPulser().submit(new Pulse(13) {
            @Override
            public boolean pulse() {
                reset();
                return true;
            }
        });
    }

    /**
     * Checks if the familiar can perform the remote view.
     * @return true if the familiar is active and can fly, false otherwise
     */
    private boolean canView() {
        player.getPacketDispatch().sendMessage("There seems to be an obstruction in the direction; the familiar cannot fly there");
        return familiar.isActive();
    }

    /**
     * Resets the camera, calls the familiar back, and unlocks the player.
     */
    private void reset() {
        familiar.call();
        player.unlock();
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.RESET, 0, 0, HEIGHT, 1, 100));
    }

    /**
     * Sends the camera position and rotation to the client.
     * @param xOffset the x-axis offset from the familiar's target location
     * @param yOffset the y-axis offset from the familiar's target location
     * @param xRot the x-axis rotation
     * @param yRot the y-axis rotation
     */
    private void sendCamera(int xOffset, int yOffset, final int xRot, final int yRot) {
        final Location location = type.getLocationTransform(player);
        final int x = location.getX() + xOffset;
        final int y = location.getY() + yOffset;

        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.POSITION, x, y, HEIGHT, 1, 100));
        PacketRepository.send(CameraViewPacket.class, new CameraContext(player, CameraContext.CameraType.ROTATION, x + xRot, y + yRot, HEIGHT, 1, 90));
    }

    /**
     * Opens the remote view dialogue for a player and their familiar.
     * @param player the player opening the dialogue
     * @param familiar the familiar to use
     */
    public static void openDialogue(final Player player, final Familiar familiar) {
        player.getDialogueInterpreter().open(DIALOGUE_NAME, familiar);
    }

    /**
     * @return the player using the remote viewer
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the familiar being used for remote viewing
     */
    public Familiar getFamiliar() {
        return familiar;
    }

    /**
     * @return the animation played by the familiar
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * @return the type of view (direction or straight up)
     */
    public ViewType getType() {
        return type;
    }

    /**
     * Represents the type of remote view (directional or straight up).
     */
    public enum ViewType {

        NORTH(Direction.NORTH, 0, 0, 0, 0),
        EAST(Direction.WEST, 0, 0, 0, 0),
        SOUTH(Direction.SOUTH, 0, 0, 0, 0),
        WEST(Direction.EAST, 0, 0, 0, 0),
        STRAIGHT_UP(null, 0, 0, 0, 0);

        private final Direction direction;
        private final int[] data;

        ViewType(Direction direction, int... data) {
            this.direction = direction;
            this.data = data;
        }

        /**
         * Computes the target location for this view type relative to the player.
         * @param player the player performing the remote view
         * @return the target location for the camera
         */
        public Location getLocationTransform(final Player player) {
            if (this == STRAIGHT_UP) {
                return player.getLocation();
            }
            return player.getLocation().transform(direction, 10);
        }

        /**
         * @return the direction associated with this view type
         */
        public Direction getDirection() {
            return direction;
        }

        /**
         * @return the x-axis offset for the camera
         */
        public int getXOffset() {
            return data[0];
        }

        /**
         * @return the y-axis offset for the camera
         */
        public int getYOffset() {
            return data[1];
        }

        /**
         * @return the x-axis rotation for the camera
         */
        public int getXRot() {
            return data[2];
        }

        /**
         * @return the y-axis rotation for the camera
         */
        public int getYRot() {
            return data[3];
        }

        /**
         * @return the raw data array for offsets and rotations
         */
        public int[] getData() {
            return data;
        }
    }
}
