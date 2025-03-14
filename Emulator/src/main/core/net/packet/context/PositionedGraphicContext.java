package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Graphics;
import core.net.packet.Context;

/**
 * The type Positioned graphic context.
 */
public final class PositionedGraphicContext implements Context {

    private final Player player;

    private final Graphics graphics;

    private final Location location;

    /**
     * The Scene x.
     */
    public int sceneX, /**
     * The Scene y.
     */
    sceneY;
    /**
     * The Offset x.
     */
    public int offsetX, /**
     * The Offset y.
     */
    offsetY;

    /**
     * Instantiates a new Positioned graphic context.
     *
     * @param player   the player
     * @param graphics the graphics
     * @param location the location
     * @param offsetX  the offset x
     * @param offsetY  the offset y
     */
    public PositionedGraphicContext(Player player, Graphics graphics, Location location, int offsetX, int offsetY) {
        this.player = player;
        this.graphics = graphics;
        this.location = location;
        this.sceneX = location.getSceneX(player.getPlayerFlags().getLastSceneGraph());
        this.sceneY = location.getSceneY(player.getPlayerFlags().getLastSceneGraph());
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets graphic.
     *
     * @return the graphic
     */
    public Graphics getGraphic() {
        return graphics;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

}