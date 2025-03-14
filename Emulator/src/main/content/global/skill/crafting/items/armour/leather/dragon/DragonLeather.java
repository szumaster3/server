package content.global.skill.crafting.items.armour.leather.dragon;

import org.rs.consts.Items;
import java.util.HashMap;
import java.util.Map;

/**
 * The enum Dragon leather.
 */
public enum DragonLeather {

    /**
     * Green d hide vambs dragon leather.
     */
    GREEN_D_HIDE_VAMBS(Items.GREEN_D_LEATHER_1745, 1, Items.GREEN_DHIDE_VAMB_1065, 57, 62.0),

    /**
     * Green d hide chaps dragon leather.
     */
    GREEN_D_HIDE_CHAPS(Items.GREEN_D_LEATHER_1745, 2, Items.GREEN_DHIDE_CHAPS_1099, 60, 124.0),

    /**
     * Green d hide body dragon leather.
     */
    GREEN_D_HIDE_BODY(Items.GREEN_D_LEATHER_1745, 3, Items.GREEN_DHIDE_BODY_1135, 63, 186.0),

    /**
     * Blue d hide vambs dragon leather.
     */
    BLUE_D_HIDE_VAMBS(Items.BLUE_D_LEATHER_2505, 1, Items.BLUE_DHIDE_VAMB_2487, 66, 70.0),

    /**
     * Blue d hide chaps dragon leather.
     */
    BLUE_D_HIDE_CHAPS(Items.BLUE_D_LEATHER_2505, 2, Items.BLUE_DHIDE_CHAPS_2493, 68, 140.0),

    /**
     * Blue d hide body dragon leather.
     */
    BLUE_D_HIDE_BODY(Items.BLUE_D_LEATHER_2505, 3, Items.BLUE_DHIDE_BODY_2499, 71, 210.0),

    /**
     * Red d hide vambs dragon leather.
     */
    RED_D_HIDE_VAMBS(Items.RED_DRAGON_LEATHER_2507, 1, Items.RED_DHIDE_VAMB_2489, 73, 78.0),

    /**
     * Red d hide chaps dragon leather.
     */
    RED_D_HIDE_CHAPS(Items.RED_DRAGON_LEATHER_2507, 2, Items.RED_DHIDE_CHAPS_2495, 75, 156.0),

    /**
     * Red d hide body dragon leather.
     */
    RED_D_HIDE_BODY(Items.RED_DRAGON_LEATHER_2507, 3, Items.RED_DHIDE_BODY_2501, 77, 234.0),

    /**
     * Black d hide vambs dragon leather.
     */
    BLACK_D_HIDE_VAMBS(Items.BLACK_D_LEATHER_2509, 1, Items.BLACK_DHIDE_VAMB_2491, 79, 86.0),

    /**
     * Black d hide chaps dragon leather.
     */
    BLACK_D_HIDE_CHAPS(Items.BLACK_D_LEATHER_2509, 2, Items.BLACK_DHIDE_CHAPS_2497, 82, 172.0),

    /**
     * Black d hide body dragon leather.
     */
    BLACK_D_HIDE_BODY(Items.BLACK_D_LEATHER_2509, 3, Items.BLACK_DHIDE_BODY_2503, 84, 258.0);

    private final int leather;
    private final int amount;
    private final int product;
    private final int level;
    private final double experience;

    private static final Map<Integer, DragonLeather> productToDragonLeatherMap = new HashMap<>();

    static {
        for (DragonLeather leather : values()) {
            productToDragonLeatherMap.put(leather.getProduct(), leather);
        }
    }

    DragonLeather(int leather, int amount, int product, int level, double experience) {
        this.leather = leather;
        this.amount = amount;
        this.product = product;
        this.level = level;
        this.experience = experience;
    }

    /**
     * Gets leather.
     *
     * @return the leather
     */
    public int getLeather() {
        return leather;
    }

    /**
     * Gets amount.
     *
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets product.
     *
     * @return the product
     */
    public int getProduct() {
        return product;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets experience.
     *
     * @return the experience
     */
    public double getExperience() {
        return experience;
    }

    /**
     * For id dragon leather.
     *
     * @param productId the product id
     * @return the dragon leather
     */
    public static DragonLeather forId(int productId) {
        return productToDragonLeatherMap.get(productId);
    }
}
