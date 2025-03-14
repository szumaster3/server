package content.global.ame.pickaxehead;

import org.rs.consts.Items;

import java.util.HashMap;
import java.util.Map;

/**
 * The enum Pickaxe head.
 */
public enum PickaxeHead {
    /**
     * Bronze pickaxe head.
     */
    BRONZE(Items.BRONZE_PICK_HEAD_480, Items.BRONZE_PICKAXE_1265),
    /**
     * Iron pickaxe head.
     */
    IRON(Items.IRON_PICK_HEAD_482, Items.IRON_PICKAXE_1267),
    /**
     * Steel pickaxe head.
     */
    STEEL(Items.STEEL_PICK_HEAD_484, Items.STEEL_PICKAXE_1269),
    /**
     * Mithril pickaxe head.
     */
    MITHRIL(Items.MITHRIL_PICK_HEAD_486, Items.MITHRIL_PICKAXE_1273),
    /**
     * Adamant pickaxe head.
     */
    ADAMANT(Items.ADAMANT_PICK_HEAD_488, Items.ADAMANT_PICKAXE_1271),
    /**
     * Rune pickaxe head.
     */
    RUNE(Items.RUNE_PICK_HEAD_490, Items.RUNE_PICKAXE_1275);

    private final int head;
    private final int pickaxe;

    private static final Map<Integer, PickaxeHead> product = new HashMap<>();

    static {
        for (PickaxeHead pickaxeHead : PickaxeHead.values()) {
            product.put(pickaxeHead.getPickaxe(), pickaxeHead);
        }
    }

    PickaxeHead(int head, int pickaxe) {
        this.head = head;
        this.pickaxe = pickaxe;
    }

    /**
     * Gets head.
     *
     * @return the head
     */
    public int getHead() {
        return head;
    }

    /**
     * Gets pickaxe.
     *
     * @return the pickaxe
     */
    public int getPickaxe() {
        return pickaxe;
    }

    /**
     * From head id pickaxe head.
     *
     * @param headId the head id
     * @return the pickaxe head
     */
    public static PickaxeHead fromHeadId(int headId) {
        for (PickaxeHead pickaxeHead : PickaxeHead.values()) {
            if (pickaxeHead.getHead() == headId) {
                return pickaxeHead;
            }
        }
        return null;
    }

    /**
     * Gets product.
     *
     * @return the product
     */
    public static Map<Integer, PickaxeHead> getProduct() {
        return product;
    }
}
