package content.global.skill.crafting.items.armour.capes;

import core.game.node.item.Item;
import org.rs.consts.Items;

import java.util.HashMap;
import java.util.Map;

/**
 * The enum Dyes.
 */
public enum Dyes {
    /**
     * The Black.
     */
    BLACK(new Item(Items.BLACK_MUSHROOM_INK_4622)),
    /**
     * The Red.
     */
    RED(new Item(Items.RED_DYE_1763)),
    /**
     * The Yellow.
     */
    YELLOW(new Item(Items.YELLOW_DYE_1765)),
    /**
     * The Blue.
     */
    BLUE(new Item(Items.BLUE_DYE_1767)),
    /**
     * The Orange.
     */
    ORANGE(new Item(Items.ORANGE_DYE_1769)),
    /**
     * The Green.
     */
    GREEN(new Item(Items.GREEN_DYE_1771)),
    /**
     * The Purple.
     */
    PURPLE(new Item(Items.PURPLE_DYE_1773)),
    /**
     * The Pink.
     */
    PINK(new Item(Items.PINK_DYE_6955));

    private final Item item;

    private static final Map<Integer, Dyes> itemToDyeMap = new HashMap<>();

    static {
        for (Dyes dye : values()) {
            itemToDyeMap.put(dye.item.getId(), dye);
        }
    }

    Dyes(Item item) {
        this.item = item;
    }

    /**
     * Gets item.
     *
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * For item dyes.
     *
     * @param item the item
     * @return the dyes
     */
    public static Dyes forItem(Item item) {
        return itemToDyeMap.get(item.getId());
    }
}
