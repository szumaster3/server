package content.global.activity.ttrail.cryptic;

import content.global.activity.ttrail.ClueLevel;
import content.global.activity.ttrail.ClueScrollPlugin;
import core.game.global.action.DigAction;
import core.game.global.action.DigSpadeHandler;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneBorders;
import org.rs.consts.Components;
import org.rs.consts.Items;

/**
 * The Cryptic clue scroll.
 */
public abstract class CrypticClueScroll extends ClueScrollPlugin {

    private final Location location;
    private final int object;
    private final String clueText;

    public CrypticClueScroll(String name, int clueId, ClueLevel level, String clueText, Location location, final int object, ZoneBorders... borders) {
        super(name, clueId, level, Components.TRAIL_MAP09_345, borders);
        this.location = location;
        this.object = object;
        this.clueText = clueText;
    }

    public CrypticClueScroll(String name, int clueId, ClueLevel level, String clueText, Location location) {
        this(name, clueId, level, clueText, location, 0);
    }

    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if (e instanceof Player) {
            Player p = e.asPlayer();
            if (target.getId() == object && option.getName().equals("Search")) {
                if (!p.getInventory().contains(clueId, 1) || !target.getLocation().equals(location)) {
                    p.sendMessage("Nothing interesting happens.");
                    return false;
                }
                reward(p);
                return true;
            }
        }
        return super.interact(e, target, option);
    }

    @Override
    public void configure() {
        DigSpadeHandler.register(getLocation(), new CrypticDigAction());
        super.configure();
    }

    @Override
    public void read(Player player) {
        super.read(player);
        for (int i = 1; i <= 8; i++) {
            player.getPacketDispatch().sendString("", interfaceId, i);
        }

        player.getPacketDispatch().sendString(clueText, interfaceId, 1);
    }

    public void dig(Player player) {
        reward(player);
        player.getDialogueInterpreter().sendItemMessage(Items.CASKET_405, "You've found a casket!");
    }

    public final class CrypticDigAction implements DigAction {
        @Override
        public void run(Player player) {
            if (!hasRequiredItems(player)) {
                player.sendMessage("Nothing interesting happens.");
                return;
            }
            dig(player);
        }
    }

    public boolean hasRequiredItems(Player player) {
        return player.getInventory().contains(clueId, 1);
    }

    public Location getLocation() {
        return location;
    }

    public int getObject() {
        return object;
    }

    public String getClueText() {
        return clueText;
    }
}