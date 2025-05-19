package content.global.activity.ttrail.cryptic;

import content.global.activity.ttrail.ClueLevel;
import content.global.activity.ttrail.ClueScrollPlugin;
import content.global.activity.ttrail.TreasureTrailManager;
import core.api.Container;
import core.game.global.action.DigAction;
import core.game.global.action.DigSpadeHandler;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneBorders;
import org.rs.consts.Components;
import org.rs.consts.Items;

import static core.api.ContentAPIKt.*;

/**
 * A base class for cryptic clue scrolls requiring object interaction or digging at a specific location.
 */
public abstract class CrypticClueScroll extends ClueScrollPlugin {

    /**
     * The location associated with the clue.
     */
    private final Location location;

    /**
     * The id of the searchable object, or 0 if not used.
     */
    private final int object;

    /**
     * The text of the cryptic clue shown to the player.
     */
    private final String clueText;

    /**
     * The key for easy clues required to open locked object.
     */
    private final Item KEY_EASY = new Item(Items.KEY_2832);

    /**
     * The key for medium clues required to open locked object.
     */
    private final Item KEY_MEDIUM = new Item(Items.KEY_2834);

    /**
     * The key for hard clues required to open locked object.
     */
    private final Item KEY_HARD = new Item(Items.KEY_2840);

    /**
     * Creates a cryptic clue scroll with a searchable object.
     *
     * @param name     the clue name
     * @param clueId   the clue item id
     * @param level    the clue scroll difficulty
     * @param clueText the clue description
     * @param location the target location
     * @param object   the object id to search
     * @param borders  optional zone borders
     */
    public CrypticClueScroll(String name, int clueId, ClueLevel level, String clueText, Location location, final int object, ZoneBorders... borders) {
        super(name, clueId, level, Components.TRAIL_MAP09_345, borders);
        this.location = location;
        this.object = object;
        this.clueText = clueText;
    }

    /**
     * Creates a cryptic clue scroll with no object interaction (dig-only).
     *
     * @param name     the clue name
     * @param clueId   the clue item id
     * @param level    the clue scroll difficulty
     * @param clueText the clue description
     * @param location the dig location
     */
    public CrypticClueScroll(String name, int clueId, ClueLevel level, String clueText, Location location) {
        this(name, clueId, level, clueText, location, 0);
    }

    /**
     * Handles player interaction with fire.
     */
    @Override
    public boolean handleUseWith(Player player, Item used, Node with) {
        if(player.getInventory().containsItem(new Item(Items.CLUE_SCROLL_10266, 1)) && with.asScenery().getId() == 2732 && inBorders(player, 2968, 2974,2970, 2976)) {
            TreasureTrailManager manager = TreasureTrailManager.getInstance(player);
            ClueScrollPlugin clueScroll = getClueScrolls().get(clueId);

            if (clueScroll != null && removeItem(player, clueScroll.getClueId(), Container.INVENTORY)) {

                clueScroll.reward(player);

                if (manager.isCompleted()) {
                    sendItemDialogue(player, Items.CASKET_405, "You've found a casket!");
                    manager.clearTrail();
                } else {
                    Item newClue = getClue(clueScroll.getLevel());
                    if (newClue != null) {
                        sendItemDialogue(player, newClue, "You found another clue scroll.");
                        player.getInventory().add(new Item(newClue.getId(), 1));
                    }
                }
            }
            return true;
        }
        return super.handleUseWith(player, used, with);
    }

    /**
     * Handles player interaction with the npc and objects for this clue.
     */
    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if (e instanceof Player) {
            Player player = (Player) e;
            if (target.getId() == object && option.getName().equals("Search") || option.getName().equals("Talk-to")) {
                if (!player.getInventory().contains(clueId, 1) || !target.getLocation().equals(location)) {
                    player.sendMessage("Nothing interesting happens.");
                    return false;
                }

                TreasureTrailManager manager = TreasureTrailManager.getInstance(player);
                ClueScrollPlugin clueScroll = getClueScrolls().get(clueId);

                if (clueScroll != null && removeItem(player, clueScroll.getClueId(), Container.INVENTORY)) {

                    clueScroll.reward(player);

                    if (manager.isCompleted()) {
                        sendItemDialogue(player, Items.CASKET_405, "You've found a casket!");
                        manager.clearTrail();
                    } else {
                        Item newClue = getClue(clueScroll.getLevel());
                        if (newClue != null) {
                            sendItemDialogue(player, newClue, "You found another clue scroll.");
                            player.getInventory().add(new Item(newClue.getId(), 1));
                        }
                    }
                }
                return true;
            }
        }
        return super.interact(e, target, option);
    }

    /**
     * Registers the dig location for this clue.
     */
    @Override
    public void configure() {
        DigSpadeHandler.register(getLocation(), new CrypticDigAction());
        super.configure();
    }

    /**
     * Displays the clue text to the player.
     *
     * @param player the player reading the clue scroll
     */
    @Override
    public void read(Player player) {
        for (int i = 1; i < 9; i++) {
            player.getPacketDispatch().sendString("", interfaceId, i);
        }
        super.read(player);
        player.getPacketDispatch().sendString(clueText.replace("<br>", "<br><br>"), interfaceId, 1);
    }

    /**
     * Called when the player digs at the correct location.
     *
     * @param player the player digging
     */
    public void dig(Player player) {
        reward(player);
        player.getDialogueInterpreter().sendItemMessage(Items.CASKET_405, "You've found a casket!");
    }

    /**
     * Handles digging action for this clue.
     */
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

    /**
     * Checks if the player has the clue item.
     *
     * @param player the player to check
     * @return true if they have the clue item
     */
    public boolean hasRequiredItems(Player player) {
        return player.getInventory().contains(clueId, 1);
    }

    /**
     * @return the clue's target location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return the id of the object to search, or 0 if none
     */
    public int getObject() {
        return object;
    }

    /**
     * @return the clue text shown to the player
     */
    public String getClueText() {
        return clueText;
    }
}
