package content.global.skill.summoning.familiar;

import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.net.packet.OutgoingContext;
import core.net.packet.PacketRepository;
import core.net.packet.out.CameraViewPacket;

/**
 * The type Remote viewer.
 */
public final class RemoteViewer {

    /**
     * The constant DIALOGUE_NAME.
     */
    public static final String DIALOGUE_NAME = "remote-view";

    /**
     * The constant HEIGHT.
     */
    public static final int HEIGHT = 1000;

    private final Player player;
    private final Familiar familiar;
    private final Animation animation;
    private final ViewType type;

    /**
     * Instantiates a new Remote viewer.
     *
     * @param player    the player
     * @param familiar  the familiar
     * @param animation the animation
     * @param type      the type
     */
    public RemoteViewer(Player player, Familiar familiar, Animation animation, ViewType type) {
        this.player = player;
        this.familiar = familiar;
        this.animation = animation;
        this.type = type;
    }

    /**
     * Create remote viewer.
     *
     * @param player    the player
     * @param familiar  the familiar
     * @param animation the animation
     * @param type      the type
     * @return the remote viewer
     */
    public static RemoteViewer create(final Player player, Familiar familiar, Animation animation, ViewType type) {
        return new RemoteViewer(player, familiar, animation, type);
    }

    /**
     * Start viewing.
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

    private boolean canView() {
        player.getPacketDispatch().sendMessage("There seems to be an obstruction in the direction; the familiar cannot fly there");
        return familiar.isActive();
    }

    private void reset() {
        familiar.call();
        player.unlock();
        PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, OutgoingContext.CameraType.RESET, 0, 0, HEIGHT, 1, 100));
    }

    private void sendCamera(int xOffset, int yOffset, final int xRot, final int yRot) {
        final Location location = type.getLocationTransform(player);
        final int x = location.getX() + xOffset;
        final int y = location.getY() + yOffset;

        PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, OutgoingContext.CameraType.POSITION, x, y, HEIGHT, 1, 100));

        PacketRepository.send(CameraViewPacket.class, new OutgoingContext.Camera(player, OutgoingContext.CameraType.ROTATION, x + xRot, y + yRot, HEIGHT, 1, 90));
    }

    /**
     * Open dialogue.
     *
     * @param player   the player
     * @param familiar the familiar
     */
    public static void openDialogue(final Player player, final Familiar familiar) {
        player.getDialogueInterpreter().open(DIALOGUE_NAME, familiar);
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
     * Gets familiar.
     *
     * @return the familiar
     */
    public Familiar getFamiliar() {
        return familiar;
    }

    /**
     * Gets animation.
     *
     * @return the animation
     */
    public Animation getAnimation() {
        return animation;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public ViewType getType() {
        return type;
    }

    /**
     * The enum View type.
     */
    public enum ViewType {

        /**
         * North view type.
         */
        NORTH(Direction.NORTH, 0, 0, 0, 0),

        /**
         * East view type.
         */
        EAST(Direction.WEST, 0, 0, 0, 0),

        /**
         * South view type.
         */
        SOUTH(Direction.SOUTH, 0, 0, 0, 0),

        /**
         * West view type.
         */
        WEST(Direction.EAST, 0, 0, 0, 0),

        /**
         * Straight up view type.
         */
        STRAIGHT_UP(null, 0, 0, 0, 0);

        private final Direction direction;
        private final int[] data;

        ViewType(Direction direction, int... data) {
            this.direction = direction;
            this.data = data;
        }

        /**
         * Gets location transform.
         *
         * @param player the player
         * @return the location transform
         */
        public Location getLocationTransform(final Player player) {
            if (this == STRAIGHT_UP) {
                return player.getLocation();
            }
            return player.getLocation().transform(direction, 10);
        }

        /**
         * Gets direction.
         *
         * @return the direction
         */
        public Direction getDirection() {
            return direction;
        }

        /**
         * Gets x offset.
         *
         * @return the x offset
         */
        public int getXOffset() {
            return data[0];
        }

        /**
         * Gets y offset.
         *
         * @return the y offset
         */
        public int getYOffset() {
            return data[1];
        }

        /**
         * Gets x rot.
         *
         * @return the x rot
         */
        public int getXRot() {
            return data[2];
        }

        /**
         * Gets y rot.
         *
         * @return the y rot
         */
        public int getYRot() {
            return data[3];
        }

        /**
         * Get data int [ ].
         *
         * @return the int [ ]
         */
        public int[] getData() {
            return data;
        }
    }
}
