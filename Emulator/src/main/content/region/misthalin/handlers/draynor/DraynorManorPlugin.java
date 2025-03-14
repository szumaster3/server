package content.region.misthalin.handlers.draynor;

import content.global.skill.agility.AgilityHandler;
import core.cache.def.impl.SceneryDefinition;
import core.game.dialogue.FaceAnim;
import core.game.global.action.ClimbActionHandler;
import core.game.global.action.DoorActionHandler;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.map.path.Pathfinder;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;

import static core.api.ContentAPIKt.sendMessageWithDelay;

/**
 * The type Draynor manor plugin.
 */
@Initializable
public final class DraynorManorPlugin extends OptionHandler {
    private static final Animation LEVER_ANIMATION = new Animation(834);
    private static final Location BASEMENT = new Location(3117, 9753, 0);
    private static final Animation DIG_ANIM = new Animation(830);
    private static final Animation SEARCH_ANIM = new Animation(881);
    private static final Item SPADE = new Item(952);
    private static final Item KEY = new Item(275);

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        SceneryDefinition.forId(156).getHandlers().put("option:search", this);
        SceneryDefinition.forId(155).getHandlers().put("option:search", this);
        SceneryDefinition.forId(160).getHandlers().put("option:pull", this);
        SceneryDefinition.forId(131).getHandlers().put("option:open", this);
        SceneryDefinition.forId(133).getHandlers().put("option:climb-down", this);
        SceneryDefinition.forId(134).getHandlers().put("option:open", this);
        SceneryDefinition.forId(135).getHandlers().put("option:open", this);
        SceneryDefinition.forId(152).getHandlers().put("option:search", this);
        SceneryDefinition.forId(153).getHandlers().put("option:search", this);
        SceneryDefinition.forId(11498).getHandlers().put("option:climb-up", this);
        SceneryDefinition.forId(37703).getHandlers().put("option:squeeze-through", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        int id = node.getId();
        switch (id) {
            case 11498:
                ClimbActionHandler.climb(player, null, Location.create(3108, 3366, 1));
                break;
            case 160:
            case 156:
            case 155:
                handleBookCase(player, ((Scenery) node));
                break;
            case 133:
                ClimbActionHandler.climb(player, new Animation(827), BASEMENT);
                break;
            case 134:
            case 135:
                if (player.getLocation().getY() >= 3354) {
                    player.getPacketDispatch().sendMessage("The doors won't open.");
                    return true;
                }
                sendMessageWithDelay(player, "The doors slam shut behind you.", 2);
                DoorActionHandler.handleDoor(player, (Scenery) node);
                return true;
            case 131:
                if (!player.getInventory().containsItem(KEY)) {
                    player.getPacketDispatch().sendMessage("The door is locked.");
                } else {
                    DoorActionHandler.handleAutowalkDoor(player, (Scenery) node);
                }
                break;
            case 152:
                if (!player.getInventory().containsItem(SPADE)) {
                    player.getDialogueInterpreter().sendDialogues(player, FaceAnim.FURIOUS, "I'm not looking through that with my hands!");
                    return true;
                }
                player.lock(3);
                player.animate(DIG_ANIM);
                player.getPacketDispatch().sendMessage("You dig through the compost...");
                if (!player.getInventory().containsItem(KEY)) {
                    player.getPacketDispatch().sendMessage("... and find a small key.");
                    if (!player.getInventory().add(KEY)) {
                        GroundItemManager.create(KEY, player);
                    }
                } else {
                    player.getPacketDispatch().sendMessage("... but you find nothing of interest.");
                }
                break;
            case 153:
                player.lock(3);
                player.animate(SEARCH_ANIM);
                player.getDialogueInterpreter().open(3954922);
                break;
            case 37703:
                AgilityHandler.walk(player, 0, player.getLocation(), node.getLocation().transform(player.getLocation().getX() <= 3085 ? 1 : 0, 0, 0), new Animation(1426), 0, null);
                break;
        }
        return true;
    }

    private void handleBookCase(final Player player, final Scenery scenery) {
        Location dest = null;
        if (RegionManager.getObject(Location.create(3097, 3359, 0)) == null || RegionManager.getObject(Location.create(3097, 3358, 0)) == null) {
            return;
        }
        if (player.getLocation().getX() > 3096) {
            if (player.getLocation().getY() >= 3359) {
                dest = Location.create(3096, 3359, 0);
            } else {
                dest = Location.create(3096, 3358, 0);
            }
        } else {
            if (scenery.getId() != 160) {
                return;
            }
            if (player.getLocation().getY() >= 3359) {
                dest = Location.create(3098, 3359, 0);
            } else {
                dest = Location.create(3098, 3358, 0);
            }
        }
        final Location destination = dest;
        if (scenery.getId() == 160) {
            GameWorld.getPulser().submit(new Pulse(1, player) {
                int counter = 0;

                @Override
                public boolean pulse() {
                    switch (counter++) {
                        case 1:
                            player.getPacketDispatch().sendMessage("The lever opens the secret door!");
                            player.animate(LEVER_ANIMATION);
                            break;
                        case 2:
                            SceneryBuilder.replace(scenery, scenery.transform(161), 6);
                            break;
                        case 3:
                            Pathfinder.find(player, Location.create(3096, 3358, 0)).walk(player);
                            break;
                        case 4:
                            player.faceLocation(destination);
                            break;
                        case 5:
                            SceneryBuilder.remove(RegionManager.getObject(Location.create(3097, 3359, 0)));
                            SceneryBuilder.remove(RegionManager.getObject(Location.create(3097, 3358, 0)));
                            SceneryBuilder.add(new Scenery(156, new Location(3097, 3357, 0)));
                            SceneryBuilder.add(new Scenery(157, new Location(3097, 3360, 0)));
                            break;
                        case 6:
                            AgilityHandler.walk(player, -1, player.getLocation(), destination, null, 0.0, null);
                            break;
                        case 8:
                            SceneryBuilder.remove(RegionManager.getObject(new Location(3097, 3360, 0)));
                            SceneryBuilder.remove(RegionManager.getObject(new Location(3097, 3357, 0)));
                            SceneryBuilder.add(new Scenery(155, new Location(3097, 3359, 0)));
                            SceneryBuilder.add(new Scenery(156, new Location(3097, 3358, 0)));
                            break;
                    }
                    return false;
                }
            });
            return;
        }
        GameWorld.getPulser().submit(new Pulse(1, player) {
            int count = 0;

            @Override
            public boolean pulse() {
                switch (count) {
                    case 0:
                        player.getPacketDispatch().sendMessage("You've found a secret door!");
                        SceneryBuilder.remove(RegionManager.getObject(Location.create(3097, 3359, 0)));
                        SceneryBuilder.remove(RegionManager.getObject(Location.create(3097, 3358, 0)));
                        SceneryBuilder.add(new Scenery(156, new Location(3097, 3357, 0)));
                        SceneryBuilder.add(new Scenery(157, new Location(3097, 3360, 0)));
                        break;
                    case 1:
                        AgilityHandler.walk(player, -1, player.getLocation(), destination, null, 0.0, null);
                        break;
                    case 3:
                        SceneryBuilder.remove(RegionManager.getObject(new Location(3097, 3360, 0)));
                        SceneryBuilder.remove(RegionManager.getObject(new Location(3097, 3357, 0)));
                        SceneryBuilder.add(new Scenery(155, new Location(3097, 3359, 0)));
                        SceneryBuilder.add(new Scenery(156, new Location(3097, 3358, 0)));
                        break;
                }
                count++;
                return count == 4;
            }

        });
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof Scenery) {
            final int id = n.getId();
            switch (id) {
                case 155:
                    return Location.create(3098, 3359, 0);
                case 156:
                    return Location.create(3098, 3358, 0);
                case 160:
                    return Location.create(3096, 3357, 0);
            }
        }
        return null;
    }
}
