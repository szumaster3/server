package content.global.skill.crafting.items.armour.capes;

import core.game.node.item.Item;
import org.rs.consts.Items;

import java.util.HashMap;
import java.util.Map;

/**
 * The enum Capes.
 */
public enum Capes {
    /**
     * The Black.
     */
    BLACK(Dyes.BLACK, new Item(Items.BLACK_CAPE_1019)),
    /**
     * The Red.
     */
    RED(Dyes.RED, new Item(Items.RED_CAPE_1007)),
    /**
     * The Blue.
     */
    BLUE(Dyes.BLUE, new Item(Items.BLUE_CAPE_1021)),
    /**
     * The Yellow.
     */
    YELLOW(Dyes.YELLOW, new Item(Items.YELLOW_CAPE_1023)),
    /**
     * The Green.
     */
    GREEN(Dyes.GREEN, new Item(Items.GREEN_CAPE_1027)),
    /**
     * The Purple.
     */
    PURPLE(Dyes.PURPLE, new Item(Items.PURPLE_CAPE_1029)),
    /**
     * The Orange.
     */
    ORANGE(Dyes.ORANGE, new Item(Items.ORANGE_CAPE_1031)),
    /**
     * The Pink.
     */
    PINK(Dyes.PINK, new Item(Items.PINK_CAPE_6959));

    private final Dyes dye;
    private final Item cape;

    private static final Map<Integer, Capes> dyeToCapeMap = new HashMap<>();

    static {
        for (Capes cape : values()) {
            dyeToCapeMap.put(cape.dye.getItem().getId(), cape);
        }
    }

    Capes(Dyes dye, Item cape) {
        this.dye = dye;
        this.cape = cape;
    }

    /**
     * Gets dye.
     *
     * @return the dye
     */
    public Dyes getDye() {
        return dye;
    }

    /**
     * Gets cape.
     *
     * @return the cape
     */
    public Item getCape() {
        return cape;
    }

    /**
     * For dye capes.
     *
     * @param dyeId the dye id
     * @return the capes
     */
    public static Capes forDye(int dyeId) {
        return dyeToCapeMap.get(dyeId);
    }
}
