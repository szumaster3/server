package content.global.skill.firemaking;

import content.global.skill.firemaking.items.Log;
import content.region.kandarin.baxtorian.BarbarianTraining;
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
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;
import shared.consts.Items;

import static core.api.ContentAPIKt.*;

/**
 * Represents making fire plugin (standard and barbarian firemaking).
 */
public final class FireMakingPlugin extends SkillPulse<Item> {

    private final Log fire;
    private GroundItem groundItem;
    private int ticks;

    private final Animation animation;
    private final Graphics graphics;
    private final boolean barbarianMode;

    /**
     * Instantiates a new Firemaking pulse.
     *
     * @param player     the player
     * @param node       the node
     * @param groundItem the ground item
     */
    public FireMakingPlugin(Player player, Item node, GroundItem groundItem, Animation animation, Graphics graphics, boolean barbarianMode) {
        super(player, node);
        this.fire = Log.forId(node.getId());
        this.animation = animation;
        this.graphics = graphics;
        this.barbarianMode = barbarianMode;

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
        GroundItem ash = new GroundItem(new Item(Items.ASHES_592), scenery.getLocation(), player);
        ash.setDecayTime(fire.getLife() + 200);
        return ash;
    }

    @Override
    public boolean checkRequirements() {
        if (fire == null) return false;


        if (barbarianMode) {
            if (!player.getSavedData().activityData.isBarbarianFiremakingBow() && getAttribute(player, BarbarianTraining.INSTANCE.getFM_START(), false)) {
                sendDialogue(player, "You must begin the relevant section of Otto Godblessed's barbarian training.");
                return false;
            }
        }
        if (RegionManager.getObject(player.getLocation()) != null || player.getZoneMonitor().isInZone("bank")) {
            player.getPacketDispatch().sendMessage("You can't light a fire here.");
            return false;
        }

        if (!inInventory(player, Items.TINDERBOX_590, 1) && !barbarianMode) {
            player.getPacketDispatch().sendMessage("You do not have the required items to light this.");
            return false;
        }

        int requiredLevel = barbarianMode ? fire.getBarbarianLevel() : fire.getDefaultLevel();
        if (player.getSkills().getLevel(Skills.FIREMAKING) < requiredLevel) {
            player.getPacketDispatch().sendMessage("You need a Firemaking level of " + requiredLevel + " to light this log.");
            return false;
        }

        if (player.getAttribute("remove-log", false)) {
            player.removeAttribute("remove-log");
            if (inInventory(player, node.getId(), 1)) {
                replaceSlot(player, node.getSlot(), new Item(node.getId(), node.getAmount() - 1), node, Container.INVENTORY);
                GroundItemManager.create(groundItem);
            }
        }

        return true;


    }

    @Override
    public void animate() {
        if (ticks == 0 && barbarianMode) {
            visualize(player, animation, graphics);
        }
    }

    @Override
    public boolean reward() {
        if (getLastFire() >= GameWorld.getTicks()) {
            createFire();
            return true;
        }


        if (ticks == 0 && !barbarianMode) player.animate(animation);
        if (++ticks % 3 != 0) return false;
        if (ticks % 12 == 0 && !barbarianMode) player.animate(animation);

        if (!success()) return false;

        createFire();
        return true;


    }

    public void createFire() {
        if (!groundItem.isActive()) return;

        Scenery o = new Scenery(83, player.getLocation());
        Scenery object = RegionManager.getObject(o.getLocation());
        Scenery scenery = new Scenery(fire.getFireId(), player.getLocation());

        SceneryBuilder.add(scenery, fire.getLife(), () -> {
            GroundItemManager.create(getAsh(player, fire, scenery));
            if (object != null) SceneryBuilder.add(object);
        });

        GroundItemManager.destroy(groundItem);
        player.moveStep();
        player.faceLocation(scenery.getFaceLocation(player.getLocation()));
        player.getSkills().addExperience(Skills.FIREMAKING, fire.getXp());

        setLastFire();
        player.dispatch(new LitFireEvent(fire.getLogId()));

        if (barbarianMode && getAttribute(player, BarbarianTraining.INSTANCE.getFM_BASE(), false)) {
            removeAttribute(player, BarbarianTraining.INSTANCE.getFM_BASE());
            player.getSavedData().activityData.setBarbarianFiremakingBow(true);
            sendDialogueLines(player, "You feel you have learned more of barbarian ways. Otto might wish", "to talk to you more.");
        }

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
        double req = barbarianMode ? fire.getBarbarianLevel() : fire.getDefaultLevel();
        double successChance = Math.ceil((level * 50 - req * 15) / req / 3 * 4);
        return successChance >= RandomFunction.random(99);
    }
}
