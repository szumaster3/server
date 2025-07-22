package core.game.node.entity.player.link.appearance;

import org.json.simple.JSONObject;

/**
 * The type Body part.
 */
public final class BodyPart {
    private int look;
    private int color;

    /**
     * Instantiates a new Body part.
     */
    public BodyPart() {

    }

    /**
     * Instantiates a new Body part.
     *
     * @param look  the look
     * @param color the color
     */
    public BodyPart(final int look, final int color) {
        this.look = look;
        this.color = color;
    }

    /**
     * Instantiates a new Body part.
     *
     * @param look the look
     */
    public BodyPart(final int look) {
        this(look, 0);
    }

    /**
     * Parse.
     *
     * @param part the part
     */
    public void parse(JSONObject part) {
        changeLook(Integer.parseInt(part.get("look").toString()));
        changeColor(Integer.parseInt(part.get("color").toString()));
    }

    /**
     * Change look.
     *
     * @param look the look
     */
    public void changeLook(final int look) {
        this.look = look;
    }

    /**
     * Change color.
     *
     * @param color the color
     */
    public void changeColor(final int color) {
        this.color = color;
    }

    /**
     * Gets look.
     *
     * @return the look
     */
    public int getLook() {
        return look;
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public int getColor() {
        return color;
    }

}
