package content.global.skill.crafting.items.armour.headdress;

import org.rs.consts.Items;

import java.util.HashMap;

/**
 * The enum Feather headdress.
 */
public enum FeatherHeaddress {

    /**
     * Feather headdress blue feather headdress.
     */
    FEATHER_HEADDRESS_BLUE(Items.BLUE_FEATHER_10089, Items.FEATHER_HEADDRESS_12210),

    /**
     * Feather headdress orange feather headdress.
     */
    FEATHER_HEADDRESS_ORANGE(Items.ORANGE_FEATHER_10091, Items.FEATHER_HEADDRESS_12222),

    /**
     * Feather headdress red feather headdress.
     */
    FEATHER_HEADDRESS_RED(Items.RED_FEATHER_10088, Items.FEATHER_HEADDRESS_12216),

    /**
     * Feather headdress stripy feather headdress.
     */
    FEATHER_HEADDRESS_STRIPY(Items.STRIPY_FEATHER_10087, Items.FEATHER_HEADDRESS_12219),

    /**
     * Feather headdress yellow feather headdress.
     */
    FEATHER_HEADDRESS_YELLOW(Items.YELLOW_FEATHER_10090, Items.FEATHER_HEADDRESS_12213);

    private final int base;
    private final int product;

    private static final HashMap<Integer, FeatherHeaddress> baseToHeaddressMap = new HashMap<>();

    static {
        for (FeatherHeaddress headdress : values()) {
            baseToHeaddressMap.put(headdress.getBase(), headdress);
        }
    }

    FeatherHeaddress(int base, int product) {
        this.base = base;
        this.product = product;
    }

    /**
     * Gets base.
     *
     * @return the base
     */
    public int getBase() {
        return base;
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
     * For base feather headdress.
     *
     * @param baseId the base id
     * @return the feather headdress
     */
    public static FeatherHeaddress forBase(int baseId) {
        return baseToHeaddressMap.get(baseId);
    }
}
