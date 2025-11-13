package content.region.kandarin.gnome_stronghold.plugin;

import content.global.skill.agility.AgilityHandler;
import content.region.kandarin.gnome_stronghold.dialogue.GnomeGateGuardDialogue;
import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.impl.ForceMovement;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.path.Pathfinder;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import shared.consts.Quests;
import shared.consts.Sounds;

import static core.api.ContentAPIKt.*;

/**
 * The type Gnome stronghold plugin.
 */
@Initializable
public final class GnomeStrongholdPlugin extends OptionHandler {

    /**
     * The constant GRAND_TREE.
     */
    public static final ZoneBorders GRAND_TREE = new ZoneBorders(2465, 3491, 2466, 3493);

    @Override
    public Plugin<java.lang.Object> newInstance(java.lang.Object arg) throws Throwable {
        SceneryDefinition.forId(190).getHandlers().put("option:open", this);
        SceneryDefinition.forId(1967).getHandlers().put("option:open", this);
        SceneryDefinition.forId(1968).getHandlers().put("option:open", this);
        SceneryDefinition.forId(9316).getHandlers().put("option:climb", this);
        SceneryDefinition.forId(9317).getHandlers().put("option:climb", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        Scenery scenery = (Scenery) node;
        switch (scenery.getId()) {
            case 9316:
            case 9317:
                final boolean scale = player.getLocation().getY() <= scenery.getLocation().getY();
                final Location end = scenery.getLocation().transform(scale ? 3 : -3, scale ? 6 : -6, 0);
                if (getQuestStage(player, Quests.THE_GRAND_TREE) == 55) {
                    sendDialogue(player, "You feel eyes upon you. It wouldn't be a good idea to enter this way right now.");
                    break;
                }
                if (!player.getSkills().hasLevel(Skills.AGILITY, 37)) {
                    player.getPacketDispatch().sendMessage("You must be level 37 agility or higher to climb down the rocks.");
                    break;
                }
                if (!scale) {
                    ForceMovement.run(player, player.getLocation(), end, Animation.create(740), Animation.create(740), Direction.SOUTH, 13).setEndAnimation(Animation.RESET);
                } else {
                    ForceMovement.run(player, player.getLocation(), end, Animation.create(1148), Animation.create(1148), Direction.SOUTH, 13).setEndAnimation(Animation.RESET);
                }
                break;
            case 1967:
            case 1968:
                if (GRAND_TREE.insideBorder(player)) {
                    openTreeDoor(player, scenery);
                } else {
                    player.getPacketDispatch().sendMessage("I can't reach that.");
                }
                return true;
            case 190:
                openGates(player, scenery);
                return true;
        }
        return true;
    }

    private void openTreeDoor(final Player player, final Scenery scenery) {
        if (scenery.getCharge() == 88) {
            return;
        }
        scenery.setCharge(88);
        playAudio(player, Sounds.TREEDOOR_OPEN_93);
        SceneryBuilder.replace(scenery, scenery.transform(scenery.getId() == 1967 ? 1969 : 1970), 4);
        AgilityHandler.walk(player, -1, player.getLocation(), player.getLocation().transform(0, player.getLocation().getY() <= 3491 ? 2 : -2, 0), new Animation(1426), 0, null, false);
        GameWorld.getPulser().submit(new Pulse(4) {
            @Override
            public boolean pulse() {
                playAudio(player, Sounds.TREEDOOR_CLOSE_92);
                scenery.setCharge(1000);
                return true;
            }
        });
    }

    private void openGates(Player player, final Scenery scenery) {
        if (inBorders(player, 2460, 3381, 2463, 3383) && getQuestStage(player, Quests.THE_GRAND_TREE) == 55) {
            openDialogue(player, new GnomeGateGuardDialogue());
        } else {
            if (scenery.getCharge() == 0) {
                return;
            }
            scenery.setCharge(0);
            SceneryBuilder.replace(scenery, scenery.transform(191), 4);
            SceneryBuilder.add(new Scenery(192, Location.create(2462, 3383, 0)), 4);
            Location start = Location.create(2461, 3382, 0);
            Location end = Location.create(2461, 3385, 0);
            if (player.getLocation().getY() > scenery.getLocation().getY()) {
                Location s = start;
                start = end;
                end = s;
            }
            Pathfinder.find(player, end).walk(player);
            GameWorld.getPulser().submit(new Pulse(4) {
                @Override
                public boolean pulse() {
                    scenery.setCharge(1000);
                    return true;
                }
            });
        }
    }

    @Override
    public Location getDestination(Node node, Node n) {
        if (n instanceof Scenery) {
            switch (n.getId()) {
                case 190:
                    if (node.getLocation().getY() < n.getLocation().getY()) {
                        return Location.create(2461, 3382, 0);
                    }
                    return Location.create(2461, 3385, 0);
            }
        }
        return null;
    }

}
