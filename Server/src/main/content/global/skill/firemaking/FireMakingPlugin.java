package content.global.skill.firemaking;

import core.api.Container;
import core.game.event.LitFireEvent;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.GameWorld;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.tools.RandomFunction;
import shared.consts.Animations;
import shared.consts.Items;

import static core.api.ContentAPIKt.inInventory;
import static core.api.ContentAPIKt.replaceSlot;

/**
 * Represents making fire plugin.
 */
public final class FireMakingPlugin extends SkillPulse<Item> {

    private static final Animation ANIMATION = new Animation(Animations.TINDERBOX_733);

    private static final Item TINDERBOX = new Item(Items.TINDERBOX_590);

    private final Log fire;

    private final GroundItem groundItem;

    private int ticks;

    /**
     * Instantiates a new Firemaking pulse.
     *
     * @param player     the player
     * @param node       the node
     * @param groundItem the ground item
     */
    public FireMakingPlugin(Player player, Item node, GroundItem groundItem) {
        super(player, node);
        this.fire = Log.forId(node.getId());
        if (groundItem == null) {
            this.groundItem = new GroundItem(node, player.getLocation(), player);
            player.setAttribute("remove-log", true);
        } else {
            this.groundItem = groundItem;
            player.removeAttribute("remove-log");
        }
    }

    /**
     * Gets ash.
     *
     * @param player  the player
     * @param fire    the fire
     * @param scenery the scenery
     * @return the ash
     */
    public static GroundItem getAsh(final Player player, Log fire, final Scenery scenery) {
        final GroundItem ash = new GroundItem(new Item(Items.ASHES_592), scenery.getLocation(), player);
        ash.setDecayTime(fire.getLife() + 200);
        return ash;
    }

    @Override
    public boolean checkRequirements() {
        if (fire == null) {
            return false;
        }
        if (player.getIronmanManager().isIronman() && !groundItem.droppedBy(player)) {
            player.getPacketDispatch().sendMessage("You can't do that as an Ironman.");
            return false;
        }
        if (RegionManager.getObject(player.getLocation()) != null || player.getZoneMonitor().isInZone("bank")) {
            player.getPacketDispatch().sendMessage("You can't light a fire here.");
            return false;
        }
        if (!player.getInventory().containsItem(TINDERBOX)) {
            player.getPacketDispatch().sendMessage("You do not have the required items to light this.");
            return false;
        }
        if (player.getSkills().getLevel(Skills.FIREMAKING) < fire.getDefaultLevel()) {
            player.getPacketDispatch().sendMessage("You need a firemaking level of " + fire.getDefaultLevel() + " to light this log.");
            return false;
        }
        if (player.getAttribute("remove-log", false)) {
            player.removeAttribute("remove-log");
            if (inInventory(player, node.getId(), 1)) {
                replaceSlot(player, node.getSlot(), new Item(node.getId(), (node.getAmount() - 1)), node, Container.INVENTORY);
                GroundItemManager.create(groundItem);
            }
        }
        return true;
    }

    @Override
    public void animate() {
    }

    @Override
    public boolean reward() {
        if (getLastFire() >= GameWorld.getTicks()) {
            createFire();
            return true;
        }
        if (ticks == 0) {
            player.animate(ANIMATION);
        }
        if (++ticks % 3 != 0) {
            return false;
        }
        if (ticks % 12 == 0) {
            player.animate(ANIMATION);
        }
        if (!success()) {
            return false;
        }
        createFire();
        return true;
    }

    /**
     * Create fire.
     */
    public void createFire() {
        if (!groundItem.isActive()) {
            return;
        }
        final Scenery object = RegionManager.getObject(player.getLocation());
        final Scenery scenery = new Scenery(fire.getFireId(), player.getLocation());
        SceneryBuilder.add(scenery, fire.getLife(), () -> {
            GroundItemManager.create(getAsh(player, fire, scenery));
            if (object != null) {
                SceneryBuilder.add(object);
            }
        });

        GroundItemManager.destroy(groundItem);
        player.moveStep();
        player.faceLocation(scenery.getFaceLocation(player.getLocation()));
        player.getSkills().addExperience(Skills.FIREMAKING, fire.getXp());

        setLastFire();
        player.dispatch(new LitFireEvent(fire.getLogId()));
    }

    @Override
    public void message(int type) {
        String name = node.getId() == Items.JOGRE_BONES_3125 ? "bones" : "logs";
        switch (type) {
            case 0:
                player.getPacketDispatch().sendMessage("You attempt to light the " + name + "..");
                break;
            case 1:
                player.getPacketDispatch().sendMessage("The fire catches and the " + name + " begin to burn.");
                break;
        }
    }

    /**
     * Gets last fire.
     *
     * @return the last fire
     */
    public int getLastFire() {
        return player.getAttribute("last-firemake", 0);
    }

    /**
     * Sets last fire.
     */
    public void setLastFire() {
        player.setAttribute("last-firemake", GameWorld.getTicks() + 2);
    }

    private boolean success() {
        int level = 1 + player.getSkills().getLevel(Skills.FIREMAKING);
        double req = fire.getDefaultLevel();
        double successChance = Math.ceil((level * 50 - req * 15) / req / 3 * 4);
        int roll = RandomFunction.random(99);
        return successChance >= roll;
    }
}
