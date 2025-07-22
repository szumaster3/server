package core.game.node.entity.player.link;

import core.api.ContainerListener;
import core.game.container.Container;
import core.game.container.ContainerEvent;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.*;

/**
 * The type Skull manager.
 */
public final class SkullManager {

    /**
     * The enum Skull icon.
     */
    public enum SkullIcon {
        /**
         * None skull icon.
         */
        NONE(-1),
        /**
         * White skull icon.
         */
        WHITE(0),
        /**
         * Red skull icon.
         */
        RED(1),
        /**
         * Bh red 5 skull icon.
         */
        BH_RED5(2),
        /**
         * Bh blue 4 skull icon.
         */
        BH_BLUE4(3),
        /**
         * Bh green 3 skull icon.
         */
        BH_GREEN3(4),
        /**
         * Bh grey 2 skull icon.
         */
        BH_GREY2(5),
        /**
         * Bh brown 1 skull icon.
         */
        BH_BROWN1(6),
        /**
         * Scream skull icon.
         */
        SCREAM(7);

        /**
         * The Id.
         */
        public final int id;

        SkullIcon(int id) {
            this.id = id;
        }

        /**
         * For id skull icon.
         *
         * @param id the id
         * @return the skull icon
         */
        public static SkullIcon forId(int id) {
            switch (id) {
                case 0:
                    return SkullIcon.WHITE;
                case 1:
                    return SkullIcon.RED;
                case 2:
                    return SkullIcon.BH_RED5;
                case 3:
                    return SkullIcon.BH_BLUE4;
                case 4:
                    return SkullIcon.BH_GREEN3;
                case 5:
                    return SkullIcon.BH_GREY2;
                case 6:
                    return SkullIcon.BH_BROWN1;
                case 7:
                    return SkullIcon.SCREAM;
                default:
                    return SkullIcon.NONE;
            }
        }
    }

    private final Player player;

    private boolean wilderness = false;

    private boolean wildernessDisabled = false;

    private int level;

    private final List<Player> skullCauses = new ArrayList<Player>();

    private boolean skulled;

    private boolean skullCheckDisabled;

    private boolean deepWilderness;

    /**
     * Instantiates a new Skull manager.
     *
     * @param player the player
     */
    public SkullManager(Player player) {
        this.player = player;
    }

    /**
     * Check skull.
     *
     * @param other the other
     */
    public void checkSkull(Entity other) {
        if (!(other instanceof Player) || !wilderness || skullCheckDisabled) {
            return;
        }
        Player o = (Player) other;
        for (Player p : o.getSkullManager().skullCauses) {
            if (p == player) {
                return;
            }
        }
        if (skullCauses.contains(o)) {
            return;
        }
        skullCauses.add(o);
        removeTimer(player, "skulled");
        registerTimer(player, spawnTimer("skulled", 2000));
    }

    /**
     * Sets skull icon.
     *
     * @param skullIcon the skull icon
     */
    public void setSkullIcon(int skullIcon) {
        player.getAppearance().setSkullIcon(skullIcon);
        player.updateAppearance();
    }

    /**
     * Reset.
     */
    public void reset() {
        skullCauses.clear();
        setSkullIcon(-1);
        setSkulled(false);
        player.getAppearance().sync();
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
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets level.
     *
     * @param level the level
     */
    public void setLevel(int level) {
        if (!deepWilderness && level >= 49)
            setDeepWilderness(true);
        else if (deepWilderness && level < 48)
            setDeepWilderness(false);

        if (level > 20)
            player.getLocks().lockTeleport(1_000_000);
        else
            player.getLocks().unlockTeleport();

        this.level = level;
    }

    /**
     * Is wilderness boolean.
     *
     * @return the boolean
     */
    public boolean isWilderness() {
        return wilderness;
    }

    /**
     * Sets wilderness.
     *
     * @param wilderness the wilderness
     */
    public void setWilderness(boolean wilderness) {
        this.wilderness = wilderness;
    }

    /**
     * Is skull check disabled boolean.
     *
     * @return the boolean
     */
    public boolean isSkullCheckDisabled() {
        return skullCheckDisabled;
    }

    /**
     * Sets skull check disabled.
     *
     * @param skullCheckDisabled the skull check disabled
     */
    public void setSkullCheckDisabled(boolean skullCheckDisabled) {
        this.skullCheckDisabled = skullCheckDisabled;
    }

    /**
     * Is wilderness disabled boolean.
     *
     * @return the boolean
     */
    public boolean isWildernessDisabled() {
        return wildernessDisabled;
    }

    /**
     * Has wilderness protection boolean.
     *
     * @return the boolean
     */
    public boolean hasWildernessProtection() {
        return level < 49;
    }

    /**
     * Sets wilderness disabled.
     *
     * @param wildernessDisabled the wilderness disabled
     */
    public void setWildernessDisabled(boolean wildernessDisabled) {
        this.wildernessDisabled = wildernessDisabled;
    }

    /**
     * Is skulled boolean.
     *
     * @return the boolean
     */
    public boolean isSkulled() {
        return skulled || deepWilderness;
    }

    /**
     * Is deep wilderness boolean.
     *
     * @return the boolean
     */
    public boolean isDeepWilderness() {
        return deepWilderness;
    }

    /**
     * Sets deep wilderness.
     *
     * @param deepWildy the deep wildy
     */
    public void setDeepWilderness(boolean deepWildy) {
        if (deepWildy) {
            updateDWSkullIcon();
        } else {
            removeDWSkullIcon();
        }
        setSkullCheckDisabled(deepWildy);
        deepWilderness = deepWildy;
    }

    /**
     * The constant DEEP_WILD_DROP_RISK_THRESHOLD.
     */
    public static final long DEEP_WILD_DROP_RISK_THRESHOLD = 100000;

    /**
     * Update dw skull icon.
     */
    public void updateDWSkullIcon() {
        if (player.getAttribute("deepwild-value-listener") == null) {
            ContainerListener listener = new ContainerListener() {
                @Override
                public void update(Container c, ContainerEvent event) {
                    refresh(c);
                }

                @Override
                public void refresh(Container c) {
                    updateDWSkullIcon();
                }
            };
            player.setAttribute("deepwild-value-listener", listener);
            player.getInventory().getListeners().add(listener);
            player.getEquipment().getListeners().add(listener);
        }
        long value = 0;
        long maxValue = 0;
        for (Item item : player.getInventory().toArray()) {
            if (item != null) {
                long alchValue = item.getAlchemyValue();
                value += alchValue;
                maxValue = Math.max(maxValue, alchValue);
            }
        }
        for (Item item : player.getEquipment().toArray()) {
            if (item != null) {
                long alchValue = item.getAlchemyValue();
                value += alchValue;
                maxValue = Math.max(maxValue, alchValue);
            }
        }

        value -= maxValue;
        player.setAttribute("deepwild-value-risk", value);
        SkullIcon skull = SkullIcon.BH_BROWN1;
        if (value >= DEEP_WILD_DROP_RISK_THRESHOLD) {
            skull = SkullIcon.RED;
        }
        setSkullIcon(skull.id);
    }

    /**
     * Remove dw skull icon.
     */
    public void removeDWSkullIcon() {
        setSkullIcon(skulled ? 0 : -1);
        ContainerListener listener = player.getAttribute("deepwild-value-listener");
        if (listener != null) {
            player.getInventory().getListeners().remove(listener);
            player.getEquipment().getListeners().remove(listener);
        }
        player.removeAttribute("deepwild-value-listener");
        player.removeAttribute("deepwild-value-risk");
    }

    /**
     * Sets skulled.
     *
     * @param skulled the skulled
     */
    public void setSkulled(boolean skulled) {
        this.skulled = skulled;
    }
}
