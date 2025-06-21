package core.game.node.entity.player.link;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.net.packet.OutgoingContext;
import core.net.packet.PacketRepository;
import core.net.packet.out.HintIcon;

/**
 * The type Hint icon manager.
 */
public final class HintIconManager {

    /**
     * The constant MAXIMUM_SIZE.
     */
    public static final int MAXIMUM_SIZE = 8;

    /**
     * The constant DEFAULT_ARROW.
     */
    public static final int DEFAULT_ARROW = 1;

    /**
     * The constant DEFAULT_MODEL.
     */
    public static final int DEFAULT_MODEL = -1;

    /**
     * The constant ARROW_CIRCLE_MODEL.
     */
    public static final int ARROW_CIRCLE_MODEL = 40497;

    private final OutgoingContext.HintIcon[] hintIcons = new OutgoingContext.HintIcon[MAXIMUM_SIZE];

    /**
     * Instantiates a new Hint icon manager.
     */
    public HintIconManager() {

    }

    /**
     * Register hint icon int.
     *
     * @param player the player
     * @param target the target
     * @return the int
     */
    public static int registerHintIcon(Player player, Node target) {
        return registerHintIcon(player, target, DEFAULT_ARROW, DEFAULT_MODEL, player.getHintIconManager().freeSlot());
    }

    /**
     * Register height hint icon int.
     *
     * @param player the player
     * @param target the target
     * @param height the height
     * @return the int
     */
    public static int registerHeightHintIcon(Player player, Node target, int height) {
        return registerHintIcon(player, target, DEFAULT_ARROW, DEFAULT_MODEL, player.getHintIconManager().freeSlot(), height);
    }

    /**
     * Register hint icon int.
     *
     * @param player  the player
     * @param target  the target
     * @param arrowId the arrow id
     * @return the int
     */
    public static int registerHintIcon(Player player, Node target, int arrowId) {
        return registerHintIcon(player, target, arrowId, DEFAULT_MODEL, player.getHintIconManager().freeSlot());
    }

    /**
     * Register hint icon int.
     *
     * @param player  the player
     * @param target  the target
     * @param arrowId the arrow id
     * @param modelId the model id
     * @return the int
     */
    public static int registerHintIcon(Player player, Node target, int arrowId, int modelId) {
        return registerHintIcon(player, target, arrowId, modelId, player.getHintIconManager().freeSlot());
    }

    /**
     * Register hint icon int.
     *
     * @param player  the player
     * @param target  the target
     * @param arrowId the arrow id
     * @param modelId the model id
     * @param slot    the slot
     * @return the int
     */
    public static int registerHintIcon(Player player, Node target, int arrowId, int modelId, int slot) {
        if (isInvalidSlot(slot) || target == null) {
            return -1;
        }
        return registerHintIcon(player, target, arrowId, modelId, slot, 0);
    }

    /**
     * Register hint icon int.
     *
     * @param player  the player
     * @param target  the target
     * @param arrowId the arrow id
     * @param modelId the model id
     * @param slot    the slot
     * @param height  the height
     * @return the int
     */
    public static int registerHintIcon(Player player, Node target, int arrowId, int modelId, int slot, int height) {
        int type = getType(target);
        return registerHintIcon(player, target, arrowId, modelId, slot, height, type);
    }

    /**
     * Register hint icon int.
     *
     * @param player     the player
     * @param target     the target
     * @param arrowId    the arrow id
     * @param modelId    the model id
     * @param slot       the slot
     * @param height     the height
     * @param targetType the target type
     * @return the int
     */
    public static int registerHintIcon(Player player, Node target, int arrowId, int modelId, int slot, int height, int targetType) {
        if (isInvalidSlot(slot)) {
            return -1;
        }
        HintIconManager manager = player.getHintIconManager();
        OutgoingContext.HintIcon icon = new OutgoingContext.HintIcon(player, slot, arrowId, targetType, target, modelId, height);
        PacketRepository.send(HintIcon.class, icon);
        manager.hintIcons[slot] = icon;
        return slot;
    }

    /**
     * Remove hint icon.
     *
     * @param player the player
     * @param slot   the slot
     */
    public static void removeHintIcon(Player player, int slot) {
        if (isInvalidSlot(slot)) {
            return;
        }
        HintIconManager manager = player.getHintIconManager();
        OutgoingContext.HintIcon icon = manager.hintIcons[slot];
        if (icon != null) {
            icon.setTargetType(0);
            PacketRepository.send(HintIcon.class, icon);
            manager.hintIcons[slot] = null;
        }
    }

    /**
     * Clear.
     */
    public void clear() {
        for (int i = 0; i < hintIcons.length; i++) {
            if (hintIcons[i] != null) {
                removeHintIcon(hintIcons[i].getPlayer(), i);
            }
        }
    }

    /**
     * Free slot int.
     *
     * @return the int
     */
    public int freeSlot() {
        for (int i = 0; i < hintIcons.length; i++) {
            if (hintIcons[i] == null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets icon.
     *
     * @param slot the slot
     * @return the icon
     */
    public OutgoingContext.HintIcon getIcon(int slot) {
        return hintIcons[slot];
    }

    private static boolean isInvalidSlot(int slot) {
        return slot < 0;
    }

    private static int getType(Node target) {
        if (target instanceof Entity) {
            return target instanceof Player ? 10 : 1;
        }
        return 2;
    }
}